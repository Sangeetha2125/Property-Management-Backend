package com.example.property_management.controllers;

import com.example.property_management.services.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgreementController {
    @Autowired
    private AgreementService agreementService;

    public ResponseEntity<Object> createAgreement(){
        return agreementService.createAgreement();
    }

    public ResponseEntity<Object> cancelAgreement(){
        return agreementService.cancelAgreement();
    }

    public ResponseEntity<Object> endAgreement(){
        return agreementService.endAgreement();
    }
}
