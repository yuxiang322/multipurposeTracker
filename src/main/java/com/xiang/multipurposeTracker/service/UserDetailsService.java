package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.UserDetailsDTO;
import com.xiang.multipurposeTracker.entities.UserDetails;
import com.xiang.multipurposeTracker.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    // Get user details
    public UserDetailsDTO getUserDetails(String userUID){
        UserDetails userDetails = userDetailsRepository.findByUserUID(userUID);

        if (userDetails == null) {
            throw new RuntimeException("User not found with UID: " + userUID);
        }

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUserUID(userDetails.getUserUID());
        userDetailsDTO.setUserName(userDetails.getName());
        userDetailsDTO.setEmail(userDetails.getEmail());
        userDetailsDTO.setPhone(userDetails.getPhone());

        return userDetailsDTO;
    }

    // Update email
    public void updateEmail(String userUid, String newEmail){
        UserDetails userDetails = userDetailsRepository.findByUserUID(userUid);
        if (userDetails == null) {
            throw new RuntimeException("User not found with UID: " + userUid);
        }

        // Update email and save
        userDetails.setEmail(newEmail);
        userDetailsRepository.save(userDetails);
    }
    // Update phone
    public void updatePhone(String userUid, String newPhone){
        UserDetails userDetails = userDetailsRepository.findByUserUID(userUid);
        if (userDetails == null) {
            throw new RuntimeException("User not found with UID: " + userUid);
        }

        // Update phone and save
        userDetails.setPhone(newPhone);
        userDetailsRepository.save(userDetails);
    }
}
