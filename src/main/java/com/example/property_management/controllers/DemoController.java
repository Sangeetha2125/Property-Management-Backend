package com.example.property_management.controllers;

import com.example.property_management.config.JwtService;
import com.example.property_management.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class DemoController {
    @Autowired
    JwtService jwtService;

    @GetMapping("/greet")
    public String greet(@RequestHeader("Authorization") String token){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getRole().name();
    }
}
