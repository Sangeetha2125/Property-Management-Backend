package com.example.property_management.controllers;

import com.example.property_management.models.Property;
import com.example.property_management.models.Unit;
import com.example.property_management.models.UnitRequest;
import com.example.property_management.services.PropertyService;
import com.example.property_management.services.UnitService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/properties/:id/units")
public class UnitController {
    @Autowired
    private UnitService unitService;

    @SneakyThrows
    @GetMapping("/")
    public ResponseEntity<Object> getAllAvailableUnits(){
        return unitService.getAllAvailableUnits();
    }

    @SneakyThrows
    @GetMapping("/:id")
    public ResponseEntity<Object> raiseUnitRequest(@RequestBody UnitRequest unitRequest){
        return unitService.raiseUnitRequest(unitRequest);
    }

    @SneakyThrows
    @PostMapping("/")
    public ResponseEntity<Object> addUnit(@RequestBody Unit unit){
        return unitService.addUnit(unit);
    }

    @SneakyThrows
    @PutMapping("/:id")
    public ResponseEntity<Object> updateUnit(@RequestBody Unit unit, @RequestParam BigInteger id){
        return unitService.updateUnit(unit, id);
    }
}
