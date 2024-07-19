package com.example.property_management.controllers;

import com.example.property_management.services.UnitRequestService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties/{propertyId}/units/{unitId}/availabilities/{availabilityId}/requests")
public class UnitRequestController {
    @Autowired
    private UnitRequestService unitRequestService;

    @SneakyThrows
    @PostMapping("/create")
    public ResponseEntity<Object> raiseRequest(){
        return unitRequestService.raiseRequest();
    }

    @SneakyThrows
    @PostMapping("/respond")
    public ResponseEntity<Object> respondRequest(){
        return unitRequestService.respondRequest();
    }
}
