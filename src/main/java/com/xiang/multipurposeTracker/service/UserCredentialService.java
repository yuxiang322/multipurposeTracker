package com.xiang.multipurposeTracker.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.xiang.multipurposeTracker.model.UserCredentialsDTO;
import com.xiang.multipurposeTracker.model.UserDetailsDTO;
import com.xiang.multipurposeTracker.repository.UserCredentialsRepository;
import com.xiang.multipurposeTracker.repository.UserDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Transactional
    public String registrationValidation(String username, String password, String name, String email, String phoneNumber) {
        String response = null;
        // Validate the user credentials -- Username
        UserCredentialsDTO userCredential = userCredentialsRepository.findByUsername(username);

        if (userCredential == null) {
            try {
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setUid(username);

                UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

                if (userRecord != null) {
                    // Insert user into User_Details table
                    UserDetailsDTO newUserDetails = new UserDetailsDTO();
                    newUserDetails.setUserUID(userRecord.getUid());
                    newUserDetails.setEmail(email);
                    newUserDetails.setName(name);
                    newUserDetails.setPhone(phoneNumber);
                    userDetailsRepository.save(newUserDetails);

                    // Insert user into User_Credentials table
                    UserCredentialsDTO newUserCredentials = new UserCredentialsDTO();
                    newUserCredentials.setUsername(username);
                    newUserCredentials.setPassword(password);
                    newUserCredentials.setUserUID(userRecord.getUid());
                    userCredentialsRepository.save(newUserCredentials);

                    response = "Registration successful. Please proceed to login.";
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "Registration failed: " + e.getMessage();
            }
        } else {
            response = "Username taken.";
        }
        return response;
    }

    public String loginValidation(String username, String password) {
        String response = null;
        // Validate the login
        UserCredentialsDTO userCredential = userCredentialsRepository.findByUsernameAndPassword(username, password);
        if (userCredential != null && userCredential.getPassword().equals(password)) {
            try {
                String uid = userCredential.getUserUID();
                // Generate token
                response = FirebaseAuth.getInstance().createCustomToken(uid);
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
                response = "Firebase authentication error.";
            }
        } else {
            response = "Invalid username or password";
        }
        return response;
    }
}
