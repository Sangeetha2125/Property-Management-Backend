package com.example.property_management.models;

import com.example.property_management.enums.UnitType;
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
@Table(name="unit_availability")
@Entity
public class UnitAvailability {
    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Column(name = "availability_type")
    @Enumerated(EnumType.STRING)
    private UnitType availabilityType;

    @Column(name = "amount")
    private int amount;

    @Column(name = "security_deposit")
    private Integer securityDeposit;

    @Column(name = "monthly_due")
    private Integer monthlyDue;

    @Column(name = "no_of_months")
    private Integer noOfMonths;

//    Document upload url
}
