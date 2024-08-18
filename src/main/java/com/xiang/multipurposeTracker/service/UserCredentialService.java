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
    public Boolean registrationValidation(String username, String password, String name, String email, String phoneNumber) {
        // Validate the user credentials -- Username
        UserCredentialsDTO userCredential = userCredentialsRepository.findByUsernameAndPassword(username, password);

        if (userCredential == null) {
            try {
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setUid(username)
                        .setPhoneNumber(phoneNumber);

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

                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public String loginValidation(String username, String password) {
        // Validate the login
        UserCredentialsDTO userCredential = userCredentialsRepository.findByUsernameAndPassword(username, password);
        if (userCredential != null && userCredential.getPassword().equals(password)) {
            try {
                String uid = userCredential.getUserUID();
                // Generate token
                return FirebaseAuth.getInstance().createCustomToken(uid);
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

}
