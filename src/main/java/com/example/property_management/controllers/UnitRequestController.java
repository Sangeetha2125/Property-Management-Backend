package com.example.property_management.controllers;

import com.example.property_management.enums.UnitRequestStatus;
import com.example.property_management.models.UnitRequest;
import com.example.property_management.services.UnitRequestService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/properties/{propertyId}/units/{unitId}/availabilities/{availabilityId}/requests")
public class UnitRequestController {

    @Autowired
    private UnitRequestService unitRequestService;

    @SneakyThrows
    @PostMapping("/create")
    public ResponseEntity<Object> raiseRequest(@RequestBody UnitRequest unitRequest, @PathVariable BigInteger unitId){
        return unitRequestService.raiseRequest(unitRequest, unitId);
    }

    @SneakyThrows
    @PostMapping("/respond/{requestId}")
    public ResponseEntity<Object> respondRequest(@PathVariable BigInteger requestId, @RequestBody UnitRequestStatus unitRequestStatus){
        return unitRequestService.respondRequest(requestId, unitRequestStatus);
    }

    @SneakyThrows
    @GetMapping("/")
    public ResponseEntity<Object> getRequestsByUnitId(@PathVariable BigInteger unitId){
        return unitRequestService.getRequestsByUnitId(unitId);
    }

    @SneakyThrows
    @DeleteMapping("/{requestId}")
    public ResponseEntity<Object> deleteRequestById(@PathVariable BigInteger requestId){
        return unitRequestService.deleteRequestById(requestId);
    }
}