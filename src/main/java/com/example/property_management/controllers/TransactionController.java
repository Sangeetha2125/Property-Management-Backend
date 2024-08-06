package com.example.property_management.controllers;

import com.example.property_management.services.TransactionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @SneakyThrows
    @PostMapping("/{agreementId}")
    public ResponseEntity<Object> createRentalTransaction(@PathVariable BigInteger agreementId){
        return transactionService.createRentalTransaction(agreementId);
    }
    @GetMapping("/lastTransactionDate/{requestId}")
    public ResponseEntity<Object> getLastTransactionDate(@PathVariable BigInteger requestId){
        return transactionService.getLastTransactionDate(requestId);
    }
}
