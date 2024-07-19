package com.example.property_management.models;

import com.example.property_management.enums.UnitType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Date;

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
    private Unit unitId;

    @Column(name = "availability_type")
    @Enumerated(EnumType.STRING)
    private UnitType availabilityType;

    @Column(name = "amount")
    private int amount;

    @Column(name = "security_deposit")
    private Integer securityDeposit;

    @Column(name = "monthly_due_date")
    private Integer monthlyDueDate;

//    Document upload url
}
