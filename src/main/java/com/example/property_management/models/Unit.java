package com.example.property_management.models;

import com.example.property_management.enums.AvailabilityStatus;
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
@Table(name="unit")
@Entity
public class Unit {
    @Id
    @Column(name = "id")
    private BigInteger id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property propertyId;

    @Column(name = "name")
    private String name;

    @Column(name = "availability")
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availability;

    @Column(name = "floor")
    private int floor;

    @Column(name = "square_footage")
    private double squareFootage;

    @Column(name = "bedrooms")
    private int bedrooms;

    @Column(name = "bathrooms")
    private int bathrooms;

    @Column(name = "description")
    private String description;
}
