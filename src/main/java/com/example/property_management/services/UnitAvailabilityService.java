package com.example.property_management.services;

import com.example.property_management.enums.AvailabilityStatus;
import com.example.property_management.enums.UnitType;
import com.example.property_management.models.Unit;
import com.example.property_management.models.UnitAvailability;
import com.example.property_management.models.User;
import com.example.property_management.repositories.UnitAvailabilityRepository;
import com.example.property_management.repositories.UnitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UnitAvailabilityService {

    @Autowired
    UnitAvailabilityRepository unitAvailabilityRepository;

    @Autowired
    UnitRepository unitRepository;

    private boolean isAuthenticated(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return authContext!=null && authContext.isAuthenticated();
    }

    private boolean isOwner(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authContext.getPrincipal();
        return authenticatedUser.getRole().name().equals("OWNER");
    }

    private User getCurrentUser(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return (User) authContext.getPrincipal();
    }

    private boolean isBuyer(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authContext.getPrincipal();
        return authenticatedUser.getRole().name().equals("BUYER");
    }

    public ResponseEntity<Object> setUnitAvailability(UnitAvailability unitAvailability, BigInteger unitId){
        if(isAuthenticated() && isOwner()){
            if(unitAvailability.getAvailabilityType()!=null && unitAvailability.getAmount()!=0){
                if(unitAvailability.getAvailabilityType() == UnitType.RENT && unitAvailability.getMonthlyDue() == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Monthly due date is required");
                }
                if(unitAvailability.getAvailabilityType()!=UnitType.BUY && unitAvailability.getNoOfMonths() == null){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Number of months is required");
                }
                Optional<Unit> unit = unitRepository.findById(unitId);
                if(unit.isPresent()){
                    if(Objects.equals(unit.get().getProperty().getOwner().getId(), getCurrentUser().getId())){
                        unitAvailability.setUnit(unit.get());
                        if(unitAvailability.getAvailabilityType()!=UnitType.RENT){
                            unitAvailability.setMonthlyDue(null);
                        }
                        if(unitAvailability.getAvailabilityType()==UnitType.BUY){
                            unitAvailability.setNoOfMonths(null);
                            unitAvailability.setSecurityDeposit(null);
                        }
                        unitAvailabilityRepository.save(unitAvailability);
                        unit.get().setAvailability(AvailabilityStatus.AVAILABLE);
                        unitRepository.save(unit.get());
                        return ResponseEntity.status(HttpStatus.CREATED).body("Unit availability created successfully");
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unit not found");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields can't be empty");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> getAllUnitAvailabilities(BigInteger unitId){
        if(isAuthenticated()){
            Optional<Unit> unit = unitRepository.findById(unitId);
            if(unit.isPresent()){
                if(isOwner()){
                    List<UnitAvailability> unitAvailabilities = unitAvailabilityRepository.findAllByUnit(unit.get());
                    return ResponseEntity.status(HttpStatus.OK).body(unitAvailabilities);
                }
                if(isBuyer()){
                    List<UnitAvailability> unitAvailabilities = unitAvailabilityRepository.findAllByUnitAndAvailabilityType(unit.get(),UnitType.BUY);
                    return ResponseEntity.status(HttpStatus.OK).body(unitAvailabilities);
                }
                List<UnitAvailability> unitAvailabilities = unitAvailabilityRepository.findAllByUnitAndAvailabilityTypeNot(unit.get(),UnitType.BUY);
                return ResponseEntity.status(HttpStatus.OK).body(unitAvailabilities);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unit not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> updateUnitAvailability(UnitAvailability unitAvailability, BigInteger unitId){
        if(isAuthenticated() && isOwner()){
            if(unitAvailability.getAvailabilityType()!=null && unitAvailability.getAmount()!=0){
                if(unitAvailability.getAvailabilityType() == UnitType.RENT && unitAvailability.getMonthlyDue()==null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Monthly due date is required");
                }
                if(unitAvailability.getAvailabilityType()!=UnitType.BUY && unitAvailability.getNoOfMonths() == null){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Number of months is required");
                }
                Optional<Unit> unit = unitRepository.findById(unitId);
                if(unit.isPresent()){
                    if(Objects.equals(unit.get().getProperty().getOwner().getId(), getCurrentUser().getId())){
                        Optional<UnitAvailability> existingUnitAvailability = unitAvailabilityRepository.findById(unitAvailability.getId());
                        if(existingUnitAvailability.isPresent()){
                            if(existingUnitAvailability.get().getAvailabilityType().equals(unitAvailability.getAvailabilityType())){
                                unitAvailability.setUnit(unit.get());
                                if(unitAvailability.getAvailabilityType()!=UnitType.RENT){
                                    unitAvailability.setMonthlyDue(null);
                                }
                                if(unitAvailability.getAvailabilityType()==UnitType.BUY){
                                    unitAvailability.setNoOfMonths(null);
                                    unitAvailability.setSecurityDeposit(null);
                                }
                                unitAvailabilityRepository.save(unitAvailability);
//                              Trigger to update current agreement and create new agreement line item as the amount changes --> future enhancements
                                return ResponseEntity.status(HttpStatus.OK).body("Unit availability updated successfully");
                            }
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unit availability type can't be changed");
                        }
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unit availability not found");
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unit not found");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields can't be empty");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> deleteUnitAvailability(BigInteger id, BigInteger unitId){
        if(isAuthenticated() && isOwner()) {
            Optional<Unit> unit = unitRepository.findById(unitId);
            if (unit.isPresent()) {
                if (Objects.equals(unit.get().getProperty().getOwner().getId(), getCurrentUser().getId())) {
                    unitAvailabilityRepository.deleteById(id);
                    if(unitAvailabilityRepository.findAllByUnit(unit.get()).isEmpty()){
                        if(unit.get().getAvailability()==AvailabilityStatus.AVAILABLE){
                            unit.get().setAvailability(AvailabilityStatus.UNAVAILABLE);
                            unitRepository.save(unit.get());
                        }
                    }
                    return ResponseEntity.status(HttpStatus.OK).body("Unit availability deleted successfully");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unit not found");

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }
}
