package com.xiang.multipurposeTracker.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.xiang.multipurposeTracker.entities.UserCredentials;
import com.xiang.multipurposeTracker.entities.UserDetails;
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
        UserCredentials userCredential = userCredentialsRepository.findByUsername(username);

        if (userCredential == null) {
            try {
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setUid(username);

                UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

                if (userRecord != null) {
                    // Insert user into User_Details table
                    UserDetails newUserDetails = new UserDetails();
                    newUserDetails.setUserUID(userRecord.getUid());
                    newUserDetails.setEmail(email);
                    newUserDetails.setName(name);
                    newUserDetails.setPhone(phoneNumber);
                    userDetailsRepository.save(newUserDetails);

                    // Insert user into User_Credentials table
                    UserCredentials newUserCredentials = new UserCredentials();
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
        UserCredentials userCredential = userCredentialsRepository.findByUsernameAndPassword(username, password);
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

    public String getUserName(String userUID){
        // Retrieve UserCredentials by userUID
        UserCredentials userCredentials = userCredentialsRepository.findByUserUID(userUID);

        // Check if userCredentials is null
        if (userCredentials != null) {
            System.out.println(userCredentials.getUsername());
            return userCredentials.getUsername();
        } else {
            // You can return a default value or throw an exception as needed
            throw new RuntimeException("User credentials not found for userUID: " + userUID);
        }
    }

    public void updatePassword(String userUID, String newPassword) throws Exception {
        // Find the user by userUID
        UserCredentials user = userCredentialsRepository.findByUserUID(userUID);

        if (user == null) {
            throw new Exception("User not found");
        }

        // Update the password with the already encrypted password
        user.setPassword(newPassword);

        // Save the updated user entity
        userCredentialsRepository.save(user);
    }
}
