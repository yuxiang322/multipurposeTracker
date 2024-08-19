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
        String registrationResponse = userCredentialService.registrationValidation(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getName(), registerRequest.getEmail(), registerRequest.getPhoneNumber());

        if (registrationResponse.equals("Registration successful. Please proceed to login.")) {
            return ResponseEntity.ok(registrationResponse);
        } else if (registrationResponse.equals("Username taken.")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(registrationResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(registrationResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO loginRequest) throws FirebaseAuthException {
        String loginResponse = userCredentialService.loginValidation(loginRequest.getUserName(), loginRequest.getPassword());

        if (loginResponse.equals("Invalid username or password")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }else if(loginResponse.equals("Firebase authentication error.")){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponse);
        }else {
            return ResponseEntity.ok(loginResponse);
        }
    }
}
