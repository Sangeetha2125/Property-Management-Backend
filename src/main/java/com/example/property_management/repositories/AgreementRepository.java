package com.example.property_management.repositories;

import com.example.property_management.models.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface AgreementRepository extends JpaRepository<Agreement, BigInteger> {
}
