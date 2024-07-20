package com.example.property_management.controllers;

import com.example.property_management.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public ResponseEntity<Object> getAllNotifications(){
        return notificationService.getAllNotifications();
    }

    @DeleteMapping("/")
    public  ResponseEntity<Object> deleteAllNotifications(){
        return notificationService.deleteAllNotifications();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNotificationById(@PathVariable BigInteger id){
        return notificationService.deleteNotificationById(id);
    }
}
