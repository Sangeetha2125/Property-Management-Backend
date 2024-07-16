package com.example.property_management.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(pattern = "dd-mm-yyyy hh:")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Transient
    private String role;

    @PrePersist
    private void onCreate() {
        createdAt = new Date();
    }
}
