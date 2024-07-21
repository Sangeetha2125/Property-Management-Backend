package com.example.property_management.services;

import com.example.property_management.enums.UnitRequestStatus;
import com.example.property_management.enums.UnitType;
import com.example.property_management.models.Agreement;
import com.example.property_management.models.UnitRequest;
import com.example.property_management.models.User;
import com.example.property_management.repositories.AgreementRepository;
import com.example.property_management.repositories.UnitRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

@Service
public class AgreementService {

    @Autowired
    private AgreementRepository agreementRepository;
    
    @Autowired
    private UnitRequestRepository unitRequestRepository;

    private boolean isAuthenticated(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return authContext!=null && authContext.isAuthenticated();
    }

    private boolean isNotOwner(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authContext.getPrincipal();
        return !authenticatedUser.getRole().name().equals("OWNER");
    }

    private User getCurrentUser(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return (User) authContext.getPrincipal();
    }

    private static Agreement getRentalAgreement(Agreement agreement, UnitRequest unitRequest) {
        Agreement rentalAgreement = new Agreement();
        rentalAgreement.setAmount(agreement.getAmount());
        rentalAgreement.setStartDate(agreement.getStartDate());
        rentalAgreement.setEndDate(agreement.getEndDate());
        rentalAgreement.setMonthlyDue(agreement.getMonthlyDue());
        rentalAgreement.setRequest(unitRequest);
        rentalAgreement.setAmount(agreement.getAmount());
        rentalAgreement.setSecurityDeposit(agreement.getSecurityDeposit());
        return rentalAgreement;
    }

    private static Agreement getLeaseAgreement(Agreement agreement, UnitRequest unitRequest) {
        Agreement leaseAgreement = new Agreement();
        leaseAgreement.setAmount(agreement.getAmount());
        leaseAgreement.setRequest(unitRequest);
        leaseAgreement.setNumberOfYears(agreement.getNumberOfYears());
        return leaseAgreement;
    }

    public ResponseEntity<Object> createAgreement(Agreement agreement, BigInteger requestId) {
        if(isAuthenticated() && isNotOwner()){
            Optional<UnitRequest> unitRequest = unitRequestRepository.findById(requestId);
            if(unitRequest.isPresent()){
                if(Objects.equals(unitRequest.get().getUser().getId(), getCurrentUser().getId())){
                    if(unitRequest.get().getStatus()==UnitRequestStatus.ACCEPTED){
                        if(agreement.getAmount()!=null){
                            if(unitRequest.get().getType()==UnitType.RENT && agreement.getStartDate()!=null && agreement.getEndDate()!=null && agreement.getMonthlyDue()!=null){
                                Agreement createRentalAgreement = getRentalAgreement(agreement, unitRequest.get());
                                agreementRepository.save(createRentalAgreement);
                                return ResponseEntity.status(HttpStatus.OK).body("Rental agreement created successfully");
                            }
                            else if(unitRequest.get().getType()==UnitType.LEASE && agreement.getNumberOfYears()!=null){
                                Agreement createLeaseAgreement = getLeaseAgreement(agreement, unitRequest.get());
                                agreementRepository.save(createLeaseAgreement);
                                return ResponseEntity.status(HttpStatus.OK).body("Lease agreement created successfully");
                            }
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields can't be empty");
                        }
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields can't be empty");
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request!, Can't create agreement");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> cancelAgreement(BigInteger requestId) {
        if(isAuthenticated() && isNotOwner()){
            Optional<UnitRequest> unitRequest = unitRequestRepository.findById(requestId);
            if(unitRequest.isPresent()){
                if(Objects.equals(unitRequest.get().getUser().getId(), getCurrentUser().getId())){
                    if(unitRequest.get().getStatus()==UnitRequestStatus.ACCEPTED){
                        unitRequest.get().setStatus(UnitRequestStatus.DENIED_BY_USER);
                        unitRequestRepository.save(unitRequest.get());
                        return ResponseEntity.status(HttpStatus.OK).body("Agreement cancelled successfully");
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request!, Can't cancel agreement");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }
}
