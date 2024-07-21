package com.example.property_management.services;


import com.example.property_management.enums.AvailabilityStatus;
import com.example.property_management.models.*;
import com.example.property_management.repositories.PropertyRepository;
import com.example.property_management.repositories.UnitRepository;
import com.example.property_management.repositories.UnitRequestRepository;
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
public class UnitService {
    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UnitRequestRepository unitRequestRepository;

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

    private Unit getUnit(Unit requestUnit, Property property) {
        Unit unit = new Unit();
        unit.setName(requestUnit.getName());
        unit.setFloor(requestUnit.getFloor());
        unit.setSquareFootage(requestUnit.getSquareFootage());
        unit.setBedrooms(requestUnit.getBedrooms());
        unit.setBathrooms(requestUnit.getBathrooms());
        unit.setDescription(requestUnit.getDescription());
        unit.setProperty(property);
        unit.setAvailability(AvailabilityStatus.UNAVAILABLE);
        return unit;
    }

    public ResponseEntity<Object> getAllUnits(BigInteger propertyId) {
        if(isAuthenticated()){
            Optional<Property> property = propertyRepository.findById(propertyId);
            if(property.isPresent()) {
                if(isOwner()){
                    User user = getCurrentUser();
                    if(Objects.equals(property.get().getOwner().getId(), user.getId())){
                        List<Unit> units = unitRepository.findAllByProperty(property.get());
                        return ResponseEntity.status(HttpStatus.OK).body(units);
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
                }
                List<Unit> units = unitRepository.findAllByPropertyAndAvailability(property.get(), AvailabilityStatus.AVAILABLE);
                return ResponseEntity.status(HttpStatus.OK).body(units);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> addUnit(Unit requestUnit, BigInteger propertyId) {
        if(isAuthenticated() && isOwner()){
            if(requestUnit.getName()!=null && requestUnit.getFloor()!=0 && requestUnit.getBathrooms()!=0 && requestUnit.getBedrooms()!=0 && requestUnit.getDescription()!=null){
                Optional<Property> property = propertyRepository.findById(propertyId);
                if(property.isPresent()){
                    Unit unit = getUnit(requestUnit, property.get());
                    unitRepository.save(unit);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Unit created successfully");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields can't be empty");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> updateUnit(Unit requestUnit, BigInteger propertyId, BigInteger unitId) {
        if(isAuthenticated() && isOwner()){
            Optional<Property> property = propertyRepository.findById(propertyId);
            if(property.isPresent()){
                Optional<Unit> unit = unitRepository.findById(unitId);
                if(unit.isPresent()){
                    if(Objects.equals(property.get().getOwner().getId(), getCurrentUser().getId())){
                        requestUnit.setProperty(property.get());
                        unitRepository.save(requestUnit);
                        return ResponseEntity.status(HttpStatus.OK).body("Unit updated successfully");
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unit not found");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }
}