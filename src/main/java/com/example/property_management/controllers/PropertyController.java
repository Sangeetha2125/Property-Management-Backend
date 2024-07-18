package com.example.property_management.controllers;

import com.example.property_management.models.Property;
import com.example.property_management.services.PropertyService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @SneakyThrows
    @GetMapping("/")
    public ResponseEntity<Object> getAllProperties(){
        return propertyService.getAllProperties();
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPropertyById(@PathVariable BigInteger id){
        return propertyService.getPropertyById(id);
    }

    @SneakyThrows
    @PostMapping("/")
    public ResponseEntity<Object> addProperty(@RequestBody Property property){
        return propertyService.addProperty(property);
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProperty(@RequestBody Property property, @PathVariable BigInteger id){
        return propertyService.updateProperty(property, id);
    }
}
