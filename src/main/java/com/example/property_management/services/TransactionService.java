package com.example.property_management.services;

import com.example.property_management.enums.PaymentMethod;
import com.example.property_management.enums.PaymentStatus;
import com.example.property_management.models.Agreement;
import com.example.property_management.models.Transaction;
import com.example.property_management.models.User;
import com.example.property_management.repositories.AgreementRepository;
import com.example.property_management.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private boolean isAuthenticated(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return authContext!=null && authContext.isAuthenticated();
    }

    private boolean isOwner(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authContext.getPrincipal();
        return authenticatedUser.getRole().name().equals("OWNER");
    }

    private User getCurrentUser(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return (User) authContext.getPrincipal();
    }

    public ResponseEntity<Object> createTransaction(BigInteger agreementId){
        if(isAuthenticated() && !isOwner()){
            Optional<Agreement> agreement = agreementRepository.findById(agreementId);
            if(agreement.isPresent()){
                if(Objects.equals(agreement.get().getRequestId().getUserId().getId(), getCurrentUser().getId())){
                    Transaction transaction = new Transaction();
                    transaction.setAgreementId(agreement.get());
                    transaction.setStatus(PaymentStatus.PAID);
                    transaction.setPaymentMethod(PaymentMethod.NET_BANKING);
                    transactionRepository.save(transaction);
                    return ResponseEntity.status(HttpStatus.OK).body("Amount paid successfully");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agreement not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }
}
