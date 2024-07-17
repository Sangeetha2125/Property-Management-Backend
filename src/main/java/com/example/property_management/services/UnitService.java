package com.example.property_management.services;

import com.example.property_management.models.Property;
import com.example.property_management.models.Unit;
import com.example.property_management.models.UnitRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class UnitService {
    public ResponseEntity<Object> getAllAvailableUnits() {
        return null;
    }

    public ResponseEntity<Object> raiseUnitRequest(UnitRequest unitRequest) {
        return null;
    }

    public ResponseEntity<Object> addUnit(Unit unit) {
        return null;
    }

    public ResponseEntity<Object> updateUnit(Unit unit, BigInteger id) {
        return null;
    }
}
