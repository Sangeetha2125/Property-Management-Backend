package com.example.property_management.controllers;

import com.example.property_management.models.User;
import com.example.property_management.services.AuthService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    AuthService authService;

    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user){
        return authService.register(user);
    }

    @SneakyThrows
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user){
        return authService.login(user);
    }
}
