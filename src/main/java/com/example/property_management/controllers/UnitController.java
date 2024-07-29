package com.example.property_management.controllers;

import com.example.property_management.models.Unit;
import com.example.property_management.services.UnitService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UnitController {
    @Autowired
    private UnitService unitService;

    @SneakyThrows
    @GetMapping("/properties/{propertyId}/units/")
    public ResponseEntity<Object> getAllUnits(@PathVariable BigInteger propertyId){
        return unitService.getAllUnits(propertyId);
    }

    @SneakyThrows
    @PostMapping("/properties/{propertyId}/units/")
    public ResponseEntity<Object> addUnit(@RequestBody Unit unit, @PathVariable BigInteger propertyId){
        return unitService.addUnit(unit,propertyId);
    };

    @SneakyThrows
    @PutMapping("/properties/{propertyId}/units/{unitId}")
    public ResponseEntity<Object> updateUnit(@RequestBody Unit unit, @PathVariable BigInteger propertyId, @PathVariable BigInteger unitId){
        return unitService.updateUnit(unit, propertyId, unitId);
    }

    @SneakyThrows
    @GetMapping("/properties/{propertyId}/units/{unitId}")
    public ResponseEntity<Object> getUnitById(@PathVariable BigInteger unitId){
        return unitService.getUnitById(unitId);
    }

    @SneakyThrows
    @GetMapping("/units/own")
    public ResponseEntity<Object> getAllBuyerUnits(){
        return unitService.getAllBuyerUnits();
    }

}
