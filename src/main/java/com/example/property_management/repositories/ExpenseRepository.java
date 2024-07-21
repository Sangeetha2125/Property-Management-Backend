package com.example.property_management.repositories;

import com.example.property_management.models.Expense;
import com.example.property_management.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, BigInteger> {
    List<Expense> findAllByUnit(Unit unit);
}
