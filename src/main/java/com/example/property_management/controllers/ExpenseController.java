package com.example.property_management.controllers;

import com.example.property_management.services.ExpenseService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @SneakyThrows
    @GetMapping("/")
    public ResponseEntity<Object> getAllExpenses(){
        return expenseService.getAllExpenses();
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<Object> getExpenseById(@PathVariable BigInteger id){
        return expenseService.getExpenseById(id);
    }

    @SneakyThrows
    @GetMapping("/unit/{unitId}")
    public ResponseEntity<Object> getAllExpensesByUnitId(@PathVariable BigInteger unitId){
        return expenseService.getAllExpensesByUnitId(unitId);
    }

    @SneakyThrows
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<Object> getAllExpensesByPropertyId(@PathVariable BigInteger propertyId){
        return expenseService.getAllExpensesByPropertyId(propertyId);
    }

    @SneakyThrows
    @PostMapping("/")
    public ResponseEntity<Object> addExpense(){
        return expenseService.addExpense();
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateExpenseById(){
        return expenseService.updateExpenseById();
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteExpenseById(){
        return expenseService.deleteExpenseById();
    }
}
