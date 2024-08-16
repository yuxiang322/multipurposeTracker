package com.xiang.multipurposeTracker.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.xiang.multipurposeTracker.model.LoginRequestDTO;
import com.xiang.multipurposeTracker.model.RegisterRequestDTO;
import com.xiang.multipurposeTracker.service.UserCredentialService;
import com.xiang.multipurposeTracker.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserCredentialController {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private UserCredentialService userService;


    @PostMapping("/register")
    public ResponseEntity<String> registrationUser(@RequestBody RegisterRequestDTO registerRequest) throws FirebaseAuthException {
        Boolean registrationFlag = userService.registrationValidation(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getName(), registerRequest.getEmail(), registerRequest.getPhoneNumber());

        if (registrationFlag) {
            return ResponseEntity.ok("Registration successful. Please proceed to login.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO loginRequest) throws FirebaseAuthException {
        String token = userService.loginValidation(loginRequest.getUserName(), loginRequest.getPassword());

        if (token != null) {
            return ResponseEntity.ok("User authenticated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
