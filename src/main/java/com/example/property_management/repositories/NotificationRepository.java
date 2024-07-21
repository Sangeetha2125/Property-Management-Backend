package com.example.property_management.repositories;

import com.example.property_management.models.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, BigInteger> {
    @Query(value = "SELECT n FROM Notification n " +
            "JOIN n.agreement a " +
            "JOIN a.request r " +
            "JOIN r.user u " +
            "WHERE u.id = :userId"
    )
    List<Notification> findAllNotificationByUser(BigInteger userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Notification n WHERE n.id = :notificationId")
    void deleteNotificationById(BigInteger notificationId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Notification n WHERE n.agreement.request.user.id = :userId")
    void deleteAllNotificationsByUser(Integer userId);
}
