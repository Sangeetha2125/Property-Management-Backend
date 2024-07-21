package com.example.property_management.repositories;

import com.example.property_management.models.Property;
import com.example.property_management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, BigInteger> {
    List<Property> findAllByOwner(User owner);
}
