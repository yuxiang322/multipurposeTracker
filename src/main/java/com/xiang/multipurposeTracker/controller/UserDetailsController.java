package com.xiang.multipurposeTracker.controller;

import com.xiang.multipurposeTracker.DTO.UserDetailsDTO;
import com.xiang.multipurposeTracker.service.UserCredentialService;
import com.xiang.multipurposeTracker.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/userDetails")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserCredentialService userCredentialService;

    // Get user details
    @PostMapping("/getUserDetails")
    public ResponseEntity<UserDetailsDTO> getUserDetails(@RequestBody UserDetailsDTO userDetailsDTO) {

            UserDetailsDTO userDetails = userDetailsService.getUserDetails(userDetailsDTO.getUserUID());
            userDetails.setUserUID(userCredentialService.getUserName(userDetailsDTO.getUserUID()));

            return ResponseEntity.ok(userDetails);
    }

    // Edit email
    @PostMapping("/changeEmail")
    public ResponseEntity<String> editEmail(@RequestBody UserDetailsDTO newEmail){

        try {
            // Email service
            userDetailsService.updateEmail(newEmail.getUserUID(), newEmail.getEmail());
            return ResponseEntity.ok("Email updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update email: " + e.getMessage());
        }
    }

    // Edit phone
    @PostMapping("/changePhone")
    public ResponseEntity<String> editPhone(@RequestBody UserDetailsDTO newPhone){

        try {
            // Phone service
            userDetailsService.updatePhone(newPhone.getUserUID(), newPhone.getPhone());
            return ResponseEntity.ok("Phone number updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update phone number: " + e.getMessage());
        }
    }
}
