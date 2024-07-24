package com.example.property_management.repositories;

import com.example.property_management.enums.UnitType;
import com.example.property_management.models.Unit;
import com.example.property_management.models.UnitAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface UnitAvailabilityRepository extends JpaRepository<UnitAvailability, BigInteger> {
    List<UnitAvailability> findAllByUnit(Unit unit);

    List<UnitAvailability> findAllByUnitAndAvailabilityType(Unit unit, UnitType unitType);

    List<UnitAvailability> findAllByUnitAndAvailabilityTypeNot(Unit unit, UnitType unitType);
}
