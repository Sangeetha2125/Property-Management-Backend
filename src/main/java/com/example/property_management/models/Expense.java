package com.example.property_management.models;

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
@Table(name="expense")
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "expense_date")
    @Temporal(TemporalType.DATE)
    private Date expenseDate;
}
