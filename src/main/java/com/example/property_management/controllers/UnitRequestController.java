package com.example.property_management.controllers;

import com.example.property_management.models.UnitRequest;
import com.example.property_management.models.UnitResponseToRequest;
import com.example.property_management.services.UnitRequestService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/requests")
public class UnitRequestController {

    @Autowired
    private UnitRequestService unitRequestService;

    @SneakyThrows
    @PostMapping("/create/{availabilityId}")
    public ResponseEntity<Object> raiseRequest(@RequestBody UnitRequest unitRequest, @PathVariable BigInteger availabilityId){
        return unitRequestService.raiseRequest(unitRequest,availabilityId);
    }

    @SneakyThrows
    @PostMapping("/respond/{requestId}")
    public ResponseEntity<Object> respondRequest(@RequestBody UnitResponseToRequest unitResponseToRequest, @PathVariable BigInteger requestId){
        return unitRequestService.respondRequest(requestId, unitResponseToRequest.getUnitRequestStatus());
    }

    @SneakyThrows
    @GetMapping("/")
    public ResponseEntity<Object> getAllRequests(){
        return unitRequestService.getAllRequests();
    }

    @SneakyThrows
    @GetMapping("/{unitId}")
    public ResponseEntity<Object> getRequestsByUnitId(@PathVariable BigInteger unitId){
        return unitRequestService.getRequestsByUnitId(unitId);
    }

    @SneakyThrows
    @PostMapping("/cancel/{requestId}")
    public ResponseEntity<Object> cancelRequest(@PathVariable BigInteger requestId){
        return unitRequestService.cancelRequest(requestId);
    }
}