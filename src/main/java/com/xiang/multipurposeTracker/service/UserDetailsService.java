package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.model.UserDetailsDTO;
import com.xiang.multipurposeTracker.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public void insertUserDetails(List<UserDetailsDTO> userDetailsDTOList) {
        userDetailsRepository.saveAll(userDetailsDTOList);
    }
}
