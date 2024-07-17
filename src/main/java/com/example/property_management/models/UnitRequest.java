package com.example.property_management.models;

import com.example.property_management.enums.UnitRequestStatus;
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
@Table(name="request")
@Entity
public class UnitRequest {
    @Column(name="id")
    @Id
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unitId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UnitType type;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UnitRequestStatus status;

    @JsonFormat(pattern = "dd-mm-yyyy hh:")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @PrePersist
    private void onCreate() {
        requestDate = new Date();
        status = UnitRequestStatus.PENDING;
    }
}
