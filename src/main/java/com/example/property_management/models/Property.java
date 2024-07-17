package com.example.property_management.models;

import com.example.property_management.enums.PropertyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="property")
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User ownerId;

    @Column(name = "name")
    private String name;

    @Column(name="address")
    private String address;

    @Column(name="state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name="pincode")
    private String pincode;

    @Column(name="num_units")
    private int numUnits;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PropertyType type;
}
