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
@Table(name="property")
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @Column(name="owner_id")
    private String ownerId;

    @Column(name="address")
    private String address;

    @Column(name="state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name="pincode")
    private String pincode;

    @Column(name="num_units")
    private String numUnits;

    @Column(name = "type")
    private String type;
}
