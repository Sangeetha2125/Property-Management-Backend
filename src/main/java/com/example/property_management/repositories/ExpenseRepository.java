package com.example.property_management.repositories;

import com.example.property_management.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface ExpenseRepository extends JpaRepository<Expense, BigInteger> {
}
