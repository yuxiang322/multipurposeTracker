package com.xiang.multipurposeTracker.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.xiang.multipurposeTracker.model.LoginRequestDTO;
import com.xiang.multipurposeTracker.model.RegisterRequestDTO;
import com.xiang.multipurposeTracker.service.UserCredentialService;
import com.xiang.multipurposeTracker.service.FirebaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserCredentialController {

    private static final Logger logger = LoggerFactory.getLogger(UserCredentialController.class);

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private UserCredentialService userCredentialService;


    @PostMapping("/register")
    public ResponseEntity<String> registrationUser(@RequestBody RegisterRequestDTO registerRequest) throws FirebaseAuthException {
        Boolean registrationFlag = userCredentialService.registrationValidation(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getName(), registerRequest.getEmail(), registerRequest.getPhoneNumber());

        if (registrationFlag) {
            return ResponseEntity.ok("Registration successful. Please proceed to login.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO loginRequest) throws FirebaseAuthException {
        String token = userCredentialService.loginValidation(loginRequest.getUserName(), loginRequest.getPassword());

        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
