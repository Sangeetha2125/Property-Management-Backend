package com.example.property_management.services;

import com.example.property_management.enums.UnitRequestStatus;
import com.example.property_management.models.Unit;
import com.example.property_management.models.UnitRequest;
import com.example.property_management.models.User;
import com.example.property_management.repositories.UnitRepository;
import com.example.property_management.repositories.UnitRequestRepository;
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
public class UnitRequestService {
    @Autowired
    private UnitRepository unitRepository;

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

    public ResponseEntity<Object> raiseRequest(UnitRequest unitRequest, BigInteger unitId){
        if(isAuthenticated() && !isOwner()){
            Optional<Unit> unit = unitRepository.findById(unitId);
            if(unit.isPresent()){
                if(unitRequest.getType()!=null && unitRequest.getMessage()!=null){
                    unitRequest.setUser(getCurrentUser());
                    unitRequest.setUnit(unit.get());
                    unitRequestRepository.save(unitRequest);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Request created successfully");
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields can't be empty");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unit not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> respondRequest(BigInteger requestId, UnitRequestStatus unitRequestStatus){
        if(isAuthenticated() && isOwner()){
            Optional<UnitRequest> unitRequest = unitRequestRepository.findById(requestId);
            if(unitRequest.isPresent()){
               if(Objects.equals(getCurrentUser().getId(), unitRequest.get().getUnit().getProperty().getOwner().getId())){
                   if(unitRequest.get().getStatus()!= UnitRequestStatus.PENDING && unitRequestStatus!=UnitRequestStatus.PENDING){
                       unitRequest.get().setStatus(unitRequestStatus);
                       if(unitRequest.get().getStatus()==UnitRequestStatus.ACCEPTED){
//                           Create a new agreement
                       }
                       unitRequestRepository.save(unitRequest.get());
                       return ResponseEntity.status(HttpStatus.OK).body("Request responded successfully");
                   }
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields can't be empty");
               }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unit request not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> getRequestsByUnitId(BigInteger unitId){
        if(isAuthenticated()){
            User user = getCurrentUser();
            Optional<Unit> unit = unitRepository.findById(unitId);
            if(unit.isPresent()){
                if(isOwner()){
                    if(Objects.equals(getCurrentUser().getId(), unit.get().getProperty().getOwner().getId())){
                        List<UnitRequest> unitRequests = unitRequestRepository.findAllByUnit(unit.get());
                        return ResponseEntity.status(HttpStatus.OK).body(unitRequests);
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
                }
                List<UnitRequest> unitRequests = unitRequestRepository.findAllByUserAndUnit(user,unit.get());
                return ResponseEntity.status(HttpStatus.OK).body(unitRequests);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unit not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> deleteRequestById(BigInteger requestId){
        if(isAuthenticated() && !isOwner()){
            Optional<UnitRequest> unitRequest = unitRequestRepository.findById(requestId);
            if(unitRequest.isPresent()){
                if(Objects.equals(getCurrentUser().getId(), unitRequest.get().getUser().getId())){
                    unitRequestRepository.deleteById(requestId);
                    return ResponseEntity.status(HttpStatus.OK).body("Unit request deleted successfully");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }
}