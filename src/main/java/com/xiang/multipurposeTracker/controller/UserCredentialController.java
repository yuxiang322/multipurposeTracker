package com.xiang.multipurposeTracker.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.xiang.multipurposeTracker.DTO.LoginRequestDTO;
import com.xiang.multipurposeTracker.DTO.PasswordDTO;
import com.xiang.multipurposeTracker.DTO.RegisterRequestDTO;
import com.xiang.multipurposeTracker.DTO.UserDetailsDTO;
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
    public ResponseEntity<String> registrationUser(@RequestBody RegisterRequestDTO registerRequestDTO) throws FirebaseAuthException {
        String registrationResponse = userCredentialService.registrationValidation(registerRequestDTO.getUsername(), registerRequestDTO.getPassword(), registerRequestDTO.getName(), registerRequestDTO.getEmail(), registerRequestDTO.getPhoneNumber());

        if (registrationResponse.equals("Registration successful. Please proceed to login.")) {
            return ResponseEntity.ok(registrationResponse);
        } else if (registrationResponse.equals("Username taken.")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(registrationResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(registrationResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) throws FirebaseAuthException {
        String loginResponse = userCredentialService.loginValidation(loginRequestDTO.getUserName(), loginRequestDTO.getPassword());

        // Generate a JWT
        if (loginResponse.equals("Invalid username or password")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }else if(loginResponse.equals("Firebase authentication error.")){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponse);
        }else {
            return ResponseEntity.ok(loginResponse);
        }
    }

    // Change password
    @PostMapping("/changePassword")
    public ResponseEntity<String> editPassword(@RequestBody PasswordDTO newPassword){

        try {
            userCredentialService.updatePassword(newPassword.getUserUID(), newPassword.getPassword());
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update password: " + e.getMessage());
        }
    }
}
