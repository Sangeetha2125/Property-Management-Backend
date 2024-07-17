package com.example.property_management.controllers;

import com.example.property_management.models.Property;
import com.example.property_management.services.PropertyService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @SneakyThrows
    @GetMapping("/owner/")
    public ResponseEntity<Object> getAllOwnerProperties(){
        return propertyService.getAllOwnerProperties();
    }

    @SneakyThrows
    @GetMapping("/owner/:id")
    public ResponseEntity<Object> getOwnerPropertyById(@RequestParam BigInteger id){
        return propertyService.getOwnerPropertyById(id);
    }

    @SneakyThrows
    @PostMapping("/")
    public ResponseEntity<Object> addProperty(@RequestBody Property property){
        return propertyService.addProperty(property);
    }

    @SneakyThrows
    @PutMapping("/:id")
    public ResponseEntity<Object> updateProperty(@RequestBody Property property, @RequestParam BigInteger id){
        return propertyService.updateProperty(property, id);
    }
}
