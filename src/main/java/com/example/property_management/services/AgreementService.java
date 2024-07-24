package com.example.property_management.services;

import com.example.property_management.enums.AvailabilityStatus;
import com.example.property_management.enums.UnitRequestStatus;
import com.example.property_management.enums.UnitType;
import com.example.property_management.models.Agreement;
import com.example.property_management.models.Unit;
import com.example.property_management.models.UnitRequest;
import com.example.property_management.models.User;
import com.example.property_management.repositories.AgreementRepository;
import com.example.property_management.repositories.UnitRepository;
import com.example.property_management.repositories.UnitRequestRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AgreementService {

    @Autowired
    private AgreementRepository agreementRepository;
    
    @Autowired
    private UnitRequestRepository unitRequestRepository;

    @Autowired
    private UnitRepository unitRepository;

    private boolean isAuthenticated(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return authContext!=null && authContext.isAuthenticated();
    }

    private boolean isNotOwner(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authContext.getPrincipal();
        return !authenticatedUser.getRole().name().equals("OWNER");
    }

    private boolean isBuyer(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authContext.getPrincipal();
        return authenticatedUser.getRole().name().equals("BUYER");
    }

    private User getCurrentUser(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return (User) authContext.getPrincipal();
    }

    private static Agreement getRentalAgreement(Agreement agreement, UnitRequest unitRequest) {
        Agreement rentalAgreement = new Agreement();
        rentalAgreement.setStartDate(agreement.getStartDate());
        rentalAgreement.setRequest(unitRequest);
        return rentalAgreement;
    }

    private static Agreement getLeaseAgreement(Agreement agreement, UnitRequest unitRequest) {
        Agreement leaseAgreement = new Agreement();
        leaseAgreement.setRequest(unitRequest);
        leaseAgreement.setStartDate(agreement.getStartDate());
        leaseAgreement.setNumberOfYears(agreement.getNumberOfYears());
        return leaseAgreement;
    }

    private static Agreement getBuyAgreement(Agreement agreement, UnitRequest unitRequest) {
        Agreement buyAgreement = new Agreement();
        buyAgreement.setRequest(unitRequest);
        return buyAgreement;
    }

    private Agreement getLiveAgreement(){
        User user = getCurrentUser();
        Optional<Agreement> agreement = agreementRepository.findCurrentAgreement(user.getId());
        return agreement.orElse(null);
    }

    public ResponseEntity<Object> createAgreement(Agreement agreement, BigInteger requestId) {
        if (isAuthenticated() && isNotOwner()) {
            Optional<UnitRequest> unitRequest = unitRequestRepository.findById(requestId);
            if (unitRequest.isPresent()) {
                Unit unit = unitRepository.findById(unitRequest.get().getUnit().getId()).get();
                if (Objects.equals(unitRequest.get().getUser().getId(), getCurrentUser().getId())) {
                    if(unit.getAvailability()==AvailabilityStatus.AVAILABLE){
                        if (unitRequest.get().getStatus() == UnitRequestStatus.PENDING) {
                            if (unitRequest.get().getType() == UnitType.RENT && agreement.getStartDate() != null) {
                                Agreement currentAgreement = getLiveAgreement();
                                if(currentAgreement!=null){
                                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End your current agreement to create new");
                                }
                                Agreement createRentalAgreement = getRentalAgreement(agreement, unitRequest.get());
                                agreementRepository.save(createRentalAgreement);
                                unit.setAvailability(AvailabilityStatus.OCCUPIED);
                                unitRepository.save(unit);
                                return ResponseEntity.status(HttpStatus.OK).body("Rental agreement created successfully");
                            } else if (unitRequest.get().getType() == UnitType.LEASE && agreement.getStartDate() != null && agreement.getNumberOfYears() != null) {
                                Agreement currentAgreement = getLiveAgreement();
                                if(currentAgreement!=null){
                                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End your current agreement to create new");
                                }
                                Agreement createLeaseAgreement = getLeaseAgreement(agreement, unitRequest.get());
                                agreementRepository.save(createLeaseAgreement);
                                unit.setAvailability(AvailabilityStatus.OCCUPIED);
                                unitRepository.save(unit);
                                return ResponseEntity.status(HttpStatus.OK).body("Lease agreement created successfully");
                            } else if (unitRequest.get().getType() == UnitType.BUY) {
                                if(isBuyer()){
                                    Agreement createBuyAgreement = getBuyAgreement(agreement, unitRequest.get());
                                    agreementRepository.save(createBuyAgreement);
                                    unit.setAvailability(AvailabilityStatus.SOLD_OUT);
                                    unit.setSoldTo(getCurrentUser());
                                    unitRepository.save(unit);
                                    return ResponseEntity.status(HttpStatus.OK).body("Unit bought successfully");
                                }
                                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
                            }
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields can't be empty");
                        }
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request!, Can't create agreement");
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry! Unit is currently not available");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> getCurrentAgreement() {
        if(isAuthenticated()){
            if(isNotOwner()){
                Agreement agreement = getLiveAgreement();
                if(agreement!=null){
                    return ResponseEntity.status(HttpStatus.OK).body(agreement);
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No agreement created yet");
            }
            List<Agreement> agreements = agreementRepository.getCurrentAgreementsOfOwner(getCurrentUser().getId());
            return ResponseEntity.status(HttpStatus.OK).body(agreements);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> terminateAgreement(BigInteger agreementId){
        if(isAuthenticated() && isNotOwner()){
            Optional<Agreement> agreement = agreementRepository.findById(agreementId);
            if(agreement.isPresent()){
                if(Objects.equals(agreement.get().getRequest().getUser().getId(), getCurrentUser().getId())){
                    if(agreement.get().getRequest().getType() != UnitType.BUY){
                        int numberOfMonths = agreement.get().getRequest().getNoOfMonths();
                        if(agreement.get().getRequest().getType()==UnitType.LEASE){
                            numberOfMonths = agreement.get().getNumberOfYears()*12;
                        }
                        if(Period.between((new Date()).toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(),agreement.get().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getMonths() >= numberOfMonths){
                            agreement.get().setEndDate(new Date());
                            agreementRepository.save(agreement.get());
                            return ResponseEntity.status(HttpStatus.OK).body("Agreement terminated successfully");
                        }
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't terminate now (No of Months not met)");
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't terminate buy agreement type");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agreement not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }
}
