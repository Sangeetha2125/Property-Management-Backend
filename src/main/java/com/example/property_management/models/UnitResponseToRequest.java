package com.example.property_management.models;

import com.example.property_management.enums.UnitRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UnitResponseToRequest {
    private UnitRequestStatus unitRequestStatus;
}
