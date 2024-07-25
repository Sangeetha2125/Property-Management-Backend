package com.example.property_management.models;

import com.example.property_management.enums.PaymentMethod;
import com.example.property_management.enums.PaymentStatus;
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
@Table(name="transaction")
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    @Column(name = "transaction_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @PrePersist
    private void onCreate() {
        transactionTime = new Date();
    }
}
