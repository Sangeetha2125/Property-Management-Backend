package com.example.property_management.controllers;

import com.example.property_management.models.Agreement;
import com.example.property_management.services.AgreementService;
import lombok.SneakyThrows;
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

    @SneakyThrows
    @PostMapping("/create/{requestId}")
    public ResponseEntity<Object> createAgreement(@RequestBody Agreement agreement, @PathVariable BigInteger requestId){
        return agreementService.createAgreement(agreement,requestId);
    }

    @SneakyThrows
    @GetMapping("/current")
    public ResponseEntity<Object> getCurrentAgreement(){
        return agreementService.getCurrentAgreement();
    }

    @SneakyThrows
    @PostMapping("/terminate/{agreementId}")
    public ResponseEntity<Object> terminateAgreement(@PathVariable BigInteger agreementId){
        return agreementService.terminateAgreement(agreementId);
    }

    @SneakyThrows
    @GetMapping("/history")
    public ResponseEntity<Object> getHistoryAgreements() {
        return agreementService.getHistoryAgreements();
    }
}
