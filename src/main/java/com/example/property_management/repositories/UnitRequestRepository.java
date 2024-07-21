package com.example.property_management.repositories;

import com.example.property_management.models.Unit;
import com.example.property_management.models.UnitRequest;
import com.example.property_management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface UnitRequestRepository extends JpaRepository<UnitRequest, BigInteger> {
    List<UnitRequest> findAllByUserAndUnit(User user, Unit unit);

    List<UnitRequest> findAllByUnit(Unit unit);
}
