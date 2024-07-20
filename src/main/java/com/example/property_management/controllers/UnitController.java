package com.example.property_management.controllers;

import com.example.property_management.models.Unit;
import com.example.property_management.services.UnitService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/properties/{propertyId}/units")
public class UnitController {
    @Autowired
    private UnitService unitService;

    @SneakyThrows
    @GetMapping("/")
    public ResponseEntity<Object> getAllUnits(@PathVariable BigInteger propertyId){
        return unitService.getAllUnits(propertyId);
    }

    @SneakyThrows
    @PostMapping("/")
    public ResponseEntity<Object> addUnit(@RequestBody Unit unit, @PathVariable BigInteger propertyId){
        return unitService.addUnit(unit,propertyId);
    };

    @SneakyThrows
    @PutMapping("/{unitId}")
    public ResponseEntity<Object> updateUnit(@RequestBody Unit unit, @PathVariable BigInteger propertyId, @PathVariable BigInteger unitId){
        return unitService.updateUnit(unit, propertyId, unitId);
    }
}
