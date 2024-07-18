package com.example.property_management.controllers;

import com.example.property_management.models.UnitAvailability;
import com.example.property_management.services.UnitAvailabilityService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/properties/{propertyId}/units/{unitId}/availabilities")
public class UnitAvailabilityController {

    @Autowired
    private UnitAvailabilityService unitAvailabilityService;

    @SneakyThrows
    @PostMapping("/")
    public ResponseEntity<Object> setUnitAvailabilities(@RequestBody UnitAvailability unitAvailability, @PathVariable BigInteger unitId){
        return unitAvailabilityService.setUnitAvailabilities(unitAvailability, unitId);
    }

    @SneakyThrows
    @GetMapping("/")
    public ResponseEntity<Object> getAllUnitAvailabilities(@PathVariable BigInteger unitId) {
        return unitAvailabilityService.getAllUnitAvailabilities(unitId);
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUnitAvailability(@RequestBody UnitAvailability unitAvailability,@PathVariable BigInteger unitId){
        return unitAvailabilityService.updateUnitAvailability(unitAvailability,unitId);
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUnitAvailability(@PathVariable BigInteger id, @PathVariable BigInteger unitId){
        return unitAvailabilityService.deleteUnitAvailability(id,unitId);
    }
}
