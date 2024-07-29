package com.example.property_management.services;

import com.example.property_management.enums.AvailabilityStatus;
import com.example.property_management.enums.UnitRequestStatus;
import com.example.property_management.enums.UnitType;
import com.example.property_management.models.*;
import com.example.property_management.repositories.AgreementRepository;
import com.example.property_management.repositories.UnitAvailabilityRepository;
import com.example.property_management.repositories.UnitRepository;
import com.example.property_management.repositories.UnitRequestRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;

@Service
public class AgreementService {

    private static final Logger log = LoggerFactory.getLogger(AgreementService.class);
    @Autowired
    private AgreementRepository agreementRepository;
    
    @Autowired
    private UnitRequestRepository unitRequestRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UnitAvailabilityRepository unitAvailabilityRepository;

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

    int monthsBetweenWithDayValue(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Both startDate and endDate must be provided");
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);

        int startDateDayOfMonth = startCalendar.get(Calendar.DAY_OF_MONTH);
        int startDateTotalMonths = 12 * startCalendar.get(Calendar.YEAR)
                + startCalendar.get(Calendar.MONTH);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        int endDateDayOfMonth = endCalendar.get(Calendar.DAY_OF_MONTH);
        int endDateTotalMonths = 12 * endCalendar.get(Calendar.YEAR)
                + endCalendar.get(Calendar.MONTH);

        return (startDateDayOfMonth > endDateDayOfMonth)
                ? (endDateTotalMonths - startDateTotalMonths) - 1
                : (endDateTotalMonths - startDateTotalMonths);
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
                if(unitRequest.get().getStatus()==UnitRequestStatus.ACCEPTED){
                    Unit unit = unitRepository.findById(unitRequest.get().getUnit().getId()).get();
                    if (Objects.equals(unitRequest.get().getUser().getId(), getCurrentUser().getId())) {
                        if(unit.getAvailability()==AvailabilityStatus.AVAILABLE){
                            if (unitRequest.get().getType() == UnitType.RENT && agreement.getStartDate() != null) {
                                Agreement currentAgreement = getLiveAgreement();
                                if(currentAgreement!=null){
                                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End your current agreement to create new");
                                }
                                Agreement createRentalAgreement = getRentalAgreement(agreement, unitRequest.get());
                                List<UnitRequest> currentUnitRequests = unitRequestRepository.getAllRequestsByUnit(unit.getId());
                                for(UnitRequest liveUnitRequest:currentUnitRequests){
                                    liveUnitRequest.setStatus(UnitRequestStatus.EXPIRED);
                                    unitRequestRepository.save(liveUnitRequest);
                                }
                                agreementRepository.save(createRentalAgreement);
                                unit.setAvailability(AvailabilityStatus.OCCUPIED);
                                unitRepository.save(unit);
                                return ResponseEntity.status(HttpStatus.OK).body("Rental agreement created successfully");
                            } else if (unitRequest.get().getType() == UnitType.LEASE && agreement.getStartDate() != null && agreement.getNumberOfYears() != null) {
                                Agreement currentAgreement = getLiveAgreement();
                                if(currentAgreement!=null){
                                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End your current agreement to create new");
                                }
                                if(unitRequest.get().getNoOfMonths()>agreement.getNumberOfYears()*12){
                                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No of years should obey the minimum months criteria");
                                }
                                Agreement createLeaseAgreement = getLeaseAgreement(agreement, unitRequest.get());
                                List<UnitRequest> currentUnitRequests = unitRequestRepository.getAllRequestsByUnit(unit.getId());
                                for(UnitRequest liveUnitRequest:currentUnitRequests){
                                    liveUnitRequest.setStatus(UnitRequestStatus.EXPIRED);
                                    unitRequestRepository.save(liveUnitRequest);
                                }
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
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry! Unit is currently not available");
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can create agreement for only accepted requests");
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
                    agreement.setLastPaidDate(null);
                    return ResponseEntity.status(HttpStatus.OK).body(agreement);
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No agreement created yet");
            }
            List<Agreement> agreements = agreementRepository.getCurrentAgreementsOfOwner(getCurrentUser().getId());
            for(Agreement agreement:agreements){
                agreement.setLastPaidDate(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(agreements);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> getHistoryAgreements() {
        if(isAuthenticated()){
            List<Agreement> agreements = agreementRepository.getHistoryAgreements(getCurrentUser().getId());
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
                        log.info("No of Months: {}", monthsBetweenWithDayValue(agreement.get().getStartDate(), new Date()));
                        if(monthsBetweenWithDayValue(agreement.get().getStartDate(), new Date()) >= numberOfMonths){
                            agreement.get().setEndDate(new Date());
                            List<UnitAvailability> unitAvailabilities = unitAvailabilityRepository.findAllByUnit(agreement.get().getRequest().getUnit());
                            if(unitAvailabilities.isEmpty()){
                                agreement.get().getRequest().getUnit().setAvailability(AvailabilityStatus.UNAVAILABLE);
                            }
                            agreement.get().getRequest().getUnit().setAvailability(AvailabilityStatus.AVAILABLE);
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
