package com.example.property_management.repositories;

import com.example.property_management.models.Unit;
import com.example.property_management.models.UnitRequest;
import com.example.property_management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface UnitRequestRepository extends JpaRepository<UnitRequest, BigInteger> {
    List<UnitRequest> findAllByUserAndUnit(User user, Unit unit);

    List<UnitRequest> findAllByUnit(Unit unit);

    List<UnitRequest> findAllByUser(User user);

    @Query(value = "SELECT r FROM UnitRequest r " +
            "JOIN r.unit u " +
            "JOIN u.property p " +
            "JOIN p.owner user " +
            "WHERE user.id = :userId")
    List<UnitRequest> findAllByOwner(Integer userId);

    @Query(value = "select r from UnitRequest r join r.unit u where u.id = :unitId and r.status<>'EXPIRED' ")
    List<UnitRequest> getAllRequestsByUnit(BigInteger unitId);
}
