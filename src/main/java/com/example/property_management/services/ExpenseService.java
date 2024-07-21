package com.example.property_management.services;

import com.example.property_management.models.Expense;
import com.example.property_management.models.Property;
import com.example.property_management.models.Unit;
import com.example.property_management.models.User;
import com.example.property_management.repositories.ExpenseRepository;
import com.example.property_management.repositories.PropertyRepository;
import com.example.property_management.repositories.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UnitRepository unitRepository;

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

    private User getCurrentUser(){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return (User) authContext.getPrincipal();
    }

    public ResponseEntity<Object> getAllExpenses(){
        if(isAuthenticated() && isOwner()){
//
            return null;
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> getExpenseById(BigInteger id){
        if(isAuthenticated() && isOwner()){
            Optional<Expense> expense = expenseRepository.findById(id);
            if(expense.isPresent()){
                if(Objects.equals(expense.get().getUnit().getProperty().getOwner().getId(), getCurrentUser().getId())){
                    return ResponseEntity.status(HttpStatus.OK).body(expense.get());
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> getAllExpensesByUnitId(BigInteger unitId){
        if(isAuthenticated() && isOwner()){
            Optional<Unit> unit = unitRepository.findById(unitId);
            if(unit.isPresent()){
                if(Objects.equals(unit.get().getProperty().getOwner().getId(), getCurrentUser().getId())){
                    List<Expense> expenses = expenseRepository.findAllByUnit(unit.get());
                    return ResponseEntity.status(HttpStatus.OK).body(expenses);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unit not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> getAllExpensesByPropertyId(BigInteger propertyId){
        if(isAuthenticated() && isOwner()){
            Optional<Property> property = propertyRepository.findById(propertyId);
            if(property.isPresent()){
                if(Objects.equals(property.get().getOwner().getId(), getCurrentUser().getId())){
//
                    return null;
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> addExpense(){
        if(isAuthenticated() && isOwner()){
//
            return null;
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> updateExpenseById(){
        if(isAuthenticated() && isOwner()){
//
            return null;
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }

    public ResponseEntity<Object> deleteExpenseById(){
        if(isAuthenticated() && isOwner()){
//
            return null;
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to access this route");
    }
}
