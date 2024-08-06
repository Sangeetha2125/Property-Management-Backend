package com.example.property_management.controllers;

import com.example.property_management.models.Image;
import com.example.property_management.services.ImageService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/properties/{propertyId}")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @SneakyThrows
    @PostMapping("/addImage")
    public ResponseEntity<String> addImage(@PathVariable BigInteger propertyId, @RequestParam("file") MultipartFile file) {
        String response = String.valueOf(imageService.addImage(propertyId, file));
        return ResponseEntity.ok(response);
    }

    @SneakyThrows
    @GetMapping("/getImage")
    public ResponseEntity<Object> getImage(@PathVariable BigInteger propertyId) {
        return imageService.getImage(propertyId);
    }

}
