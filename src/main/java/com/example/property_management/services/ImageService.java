package com.example.property_management.services;
import com.example.property_management.models.Image;
import com.example.property_management.models.Property;
import com.example.property_management.models.User;
import com.example.property_management.repositories.ImageRepository;
import com.example.property_management.repositories.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    private boolean isAuthenticated(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return authContext!=null && authContext.isAuthenticated();
    }

    private boolean isOwner(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authContext.getPrincipal();
        return authenticatedUser.getRole().name().equals("OWNER");
    }

    public ResponseEntity<Object> addImage(BigInteger propertyId, MultipartFile file) {
        if(isAuthenticated() && isOwner()){
            try {
                String encodedString = Base64.getEncoder().encodeToString(file.getBytes());
                Image image = new Image();
                Optional<Property> property = propertyRepository.findById(propertyId);
                if(property.isPresent()){
                    image.setProperty(property.get());
                    image.setUrl(encodedString);
                    imageRepository.save(image);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Image added successfully");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }
    public ResponseEntity<Object> getImage(BigInteger propertyId) {
        if(isAuthenticated()){
            Optional<Property> property = propertyRepository.findById(propertyId);
            List<String> imageList = new ArrayList<>();
            if(property.isPresent()){
                List<Image> images = imageRepository.findAll();
                for(Image image: images)
                {
                    if(image.getProperty().getId().equals(propertyId))
                    {
                        imageList.add(image.getUrl());
                    }
                }
                return ResponseEntity.ok(imageList);
//                if(!imageList.isEmpty()) {
//                    return ResponseEntity.ok(imageList);
//                } else {
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No images found for this property");
//                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }



}
