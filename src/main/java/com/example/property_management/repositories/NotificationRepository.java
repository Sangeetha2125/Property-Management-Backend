package com.example.property_management.repositories;

import com.example.property_management.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface NotificationRepository extends JpaRepository<Notification, BigInteger> {
}
