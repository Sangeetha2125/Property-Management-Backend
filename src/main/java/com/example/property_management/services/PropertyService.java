package com.example.property_management.services;

import com.example.property_management.models.Property;
import com.example.property_management.models.User;
import com.example.property_management.repositories.PropertyRepository;
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
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

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

    private Property getProperty(Property requestProperty) {
        Property property = new Property();
        property.setName(requestProperty.getName());
        property.setAddress(requestProperty.getAddress());
        property.setState(requestProperty.getState());
        property.setCity(requestProperty.getCity());
        property.setPincode(requestProperty.getPincode());
        property.setNumUnits(requestProperty.getNumUnits());
        property.setType(requestProperty.getType());
        property.setOwner(getCurrentUser());
        return property;
    }

    public ResponseEntity<Object> getAllProperties(){
        if(isAuthenticated()){
            if(isOwner()){
                User user = getCurrentUser();
                List<Property> properties = propertyRepository.findAllByOwner(user);
                return ResponseEntity.status(HttpStatus.OK).body(properties);
            }
            List<Property> properties = propertyRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(properties);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> getPropertyById(BigInteger id){
        if(isAuthenticated()){
            Optional<Property> property = propertyRepository.findById(id);
            if(property.isPresent()){
                if(isOwner()){
                    if(Objects.equals(property.get().getOwner().getId(), getCurrentUser().getId())){
                        return ResponseEntity.status(HttpStatus.OK).body(property.get());
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body(property.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> addProperty(Property requestProperty){
        if(isAuthenticated() && isOwner()){
            if(requestProperty.getName()!=null && requestProperty.getCity()!=null && requestProperty.getAddress()!=null && requestProperty.getPincode()!=null && requestProperty.getState()!=null && requestProperty.getNumUnits()!=0 && requestProperty.getType()!=null){
                Property property = getProperty(requestProperty);
                propertyRepository.save(property);

                return ResponseEntity.status(HttpStatus.CREATED).body("Property created successfully");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields can't be empty");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }
    public ResponseEntity<Object> updateProperty(Property requestProperty, BigInteger id){
        if(isAuthenticated() && isOwner()){
            Optional<Property> property = propertyRepository.findById(id);
            if(property.isPresent()){
                if(Objects.equals(property.get().getOwner().getId(), getCurrentUser().getId())){
                    Property existingProperty = property.get();
                    existingProperty.setName(requestProperty.getName());
                    existingProperty.setAddress(requestProperty.getAddress());
                    existingProperty.setState(requestProperty.getState());
                    existingProperty.setCity(requestProperty.getCity());
                    existingProperty.setPincode(requestProperty.getPincode());
                    existingProperty.setNumUnits(requestProperty.getNumUnits());
                    existingProperty.setType(requestProperty.getType());
                    propertyRepository.save(existingProperty);
                    return ResponseEntity.status(HttpStatus.OK).body("Property updated successfully");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

//    public ResponseEntity<Object> updateProperty(Property requestProperty, BigInteger id){
//        if(isAuthenticated() && isOwner()){
//            Optional<Property> property = propertyRepository.findById(id);
//            if(property.isPresent()){
//                if(Objects.equals(property.get().getOwner().getId(), getCurrentUser().getId())){
//                    requestProperty.setOwner(getCurrentUser());
//                    propertyRepository.save(requestProperty);
//                    return ResponseEntity.status(HttpStatus.OK).body("Property updated successfully");
//                }
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
//            }
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
//    }
}