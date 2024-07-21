package com.example.property_management.models;

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
@Table(name="notification")
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    @Column(name = "notification_date")
    @JsonFormat(pattern = "dd-MM-yyyy hh:")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationDate;

    @PrePersist
    private void onCreate() {
        notificationDate = new Date();
    }
}
