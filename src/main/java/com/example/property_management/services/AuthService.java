package com.example.property_management.services;

import com.example.property_management.config.JwtService;
import com.example.property_management.models.User;
import com.example.property_management.repositories.AuthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    AuthRepository authRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    public ResponseEntity<Object> register(User user){
        if(user.getFirstName()!=null && user.getLastName()!=null && user.getEmail()!=null && user.getPassword()!=null && user.getPhoneNumber()!=null){
            if(authRepository.findByEmail(user.getEmail()).isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            authRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(jwtToken);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields can't be empty");
    }

    public ResponseEntity<Object> login(User user){
        if(user.getFirstName()!=null && user.getLastName()!=null && user.getEmail()!=null && user.getPassword()!=null){
            User userExists = authRepository.findOneByEmailAndPassword(user.getEmail(), user.getPassword());
            if(userExists!=null){
                log.info(userExists.getFirstName());
                User verifiedUser = new User();
                verifiedUser.setId(userExists.getId());
                verifiedUser.setEmail(userExists.getEmail());
                verifiedUser.setRole(user.getRole());
                return ResponseEntity.status(HttpStatus.OK).body(verifiedUser);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user credentials");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields can't be empty");
    }
}
