package com.example.property_management.repositories;

import com.example.property_management.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface TransactionRepository extends JpaRepository<Transaction, BigInteger> {
}
