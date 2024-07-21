package com.example.property_management.models;

import com.example.property_management.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class AuthResponse {
    private String token;
    private UserRole role;
}
