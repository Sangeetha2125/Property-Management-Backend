package com.example.property_management.services;

import com.example.property_management.models.Property;
import com.example.property_management.models.User;
import com.example.property_management.repositories.PropertyRepository;
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

@Service
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

    public boolean isOwner(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if(authContext!=null && authContext.isAuthenticated()){
            User authenticatedUser = (User) authContext.getPrincipal();
            return authenticatedUser.getRole().name().equals("OWNER");
        }
        return false;
    }

    public User getCurrentOwner(){
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
        property.setOwnerId(getCurrentOwner());
        return property;
    }

    public ResponseEntity<Object> getAllOwnerProperties(){
        if(isOwner()){
            User user = getCurrentOwner();
            List<Property> properties = propertyRepository.findAllByOwnerId(user);
            return ResponseEntity.status(HttpStatus.OK).body(properties);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> getOwnerPropertyById(BigInteger id){
        if(isOwner()){
            Optional<Property> property = propertyRepository.findById(id);
            if(property.isPresent()){
                if(Objects.equals(property.get().getOwnerId().getId(), getCurrentOwner().getId())){
                    return ResponseEntity.status(HttpStatus.OK).body(property.get());
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> addProperty(Property requestProperty){
        if(isOwner()){
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
        if(isOwner()){
            Optional<Property> property = propertyRepository.findById(id);
            if(property.isPresent()){
                if(Objects.equals(property.get().getOwnerId().getId(), getCurrentOwner().getId())){
                    propertyRepository.save(requestProperty);
                    return ResponseEntity.status(HttpStatus.OK).body("Property updated successfully");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }
}
