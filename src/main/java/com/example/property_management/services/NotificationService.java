package com.example.property_management.services;

import com.example.property_management.enums.UnitRequestStatus;
import com.example.property_management.models.Notification;
import com.example.property_management.models.UnitRequest;
import com.example.property_management.models.User;
import com.example.property_management.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

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

    public ResponseEntity<Object> getAllNotifications(){
        if(isAuthenticated() && isNotOwner()){
            List<Notification> notifications = notificationRepository.findAllNotificationByUserId(getCurrentUser().getId());
            return ResponseEntity.status(HttpStatus.OK).body(notifications);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public  ResponseEntity<Object> deleteAllNotifications(){
        if(isAuthenticated() && isNotOwner()){
            notificationRepository.deleteAllNotificationsByUserId(getCurrentUser().getId().intValue());
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> deleteNotificationById(BigInteger id){
        if(isAuthenticated() && isNotOwner()){
            notificationRepository.deleteNotificationById(id);
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }
}
