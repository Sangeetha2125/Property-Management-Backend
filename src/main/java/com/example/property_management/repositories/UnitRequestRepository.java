package com.example.property_management.repositories;

import com.example.property_management.models.UnitRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface UnitRequestRepository extends JpaRepository<UnitRequest, BigInteger> {
}
