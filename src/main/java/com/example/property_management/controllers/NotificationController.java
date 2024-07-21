package com.example.property_management.controllers;

import com.example.property_management.services.NotificationService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @SneakyThrows
    @GetMapping("/")
    public ResponseEntity<Object> getAllNotifications(){
        return notificationService.getAllNotifications();
    }

    @SneakyThrows
    @DeleteMapping("/")
    public  ResponseEntity<Object> deleteAllNotifications(){
        return notificationService.deleteAllNotifications();
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNotificationById(@PathVariable BigInteger id){
        return notificationService.deleteNotificationById(id);
    }
}
