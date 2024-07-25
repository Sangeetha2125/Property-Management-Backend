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
@Table(name="agreement")
@Entity
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @OneToOne
    @JoinColumn(name = "request_id")
    private UnitRequest request;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "number_of_years")
    private Integer numberOfYears;
}