package com.example.property_management.repositories;

import com.example.property_management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Integer> {
    User findOneByEmail(String email);

    User findOneByEmailAndPassword(String email, String password);
}
