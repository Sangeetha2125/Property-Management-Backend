package com.example.property_management.repositories;

import com.example.property_management.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Date;

public interface TransactionRepository extends JpaRepository<Transaction, BigInteger> {
//    @Query(value = "select ")
//    Date getLastPaidDate();
}
