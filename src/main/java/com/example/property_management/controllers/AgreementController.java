package com.example.property_management.controllers;

import com.example.property_management.models.Agreement;
import com.example.property_management.services.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/agreements")
public class AgreementController {
    @Autowired
    private AgreementService agreementService;

    @PostMapping("/create/{requestId}")
    public ResponseEntity<Object> createAgreement(@RequestBody Agreement agreement, @PathVariable BigInteger requestId){
        return agreementService.createAgreement(agreement,requestId);
    }

    @PostMapping("/cancel/{requestId}")
    public ResponseEntity<Object> cancelAgreement(@PathVariable BigInteger requestId){
        return agreementService.cancelAgreement(requestId);
    }
}
