package com.example.property_management.repositories;

import com.example.property_management.enums.AvailabilityStatus;
import com.example.property_management.models.Property;
import com.example.property_management.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, BigInteger> {
    List<Unit> findAllByProperty(Property property);

    List<Unit> findAllByPropertyAndAvailability(Property property, AvailabilityStatus availabilityStatus);

    @Query(
            value = "select u.id, u.property_id, u.name, u.availability, u.floor, u.square_footage, u.bedrooms, u.bathrooms, u.description, u.sold_to " +
                    "from unit u " +
            "join user " +
            "on user.id = u.sold_to " +
            "where user.id = :buyerId", nativeQuery = true
    )
    List<Unit> findAllByBuyerId(BigInteger buyerId);
}
