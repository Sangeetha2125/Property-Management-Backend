package com.example.property_management.repositories;

import com.example.property_management.enums.AvailabilityStatus;
import com.example.property_management.models.Property;
import com.example.property_management.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, BigInteger> {
    List<Unit> findAllByProperty(Property property);

    List<Unit> findAllByPropertyAndAvailability(Property property, AvailabilityStatus availabilityStatus);
}
