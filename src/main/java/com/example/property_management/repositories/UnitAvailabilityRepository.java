package com.example.property_management.repositories;

import com.example.property_management.models.Unit;
import com.example.property_management.models.UnitAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface UnitAvailabilityRepository extends JpaRepository<UnitAvailability, BigInteger> {
    List<UnitAvailability> findAllByUnitId(Unit unit);
}
