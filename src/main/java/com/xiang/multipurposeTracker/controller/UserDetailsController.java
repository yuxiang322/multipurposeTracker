package com.xiang.multipurposeTracker.controller;

import com.xiang.multipurposeTracker.model.UserDetailsDTO;
import com.xiang.multipurposeTracker.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/add")
    public String addUser(@RequestBody List<UserDetailsDTO> userDetailsDTOList) {
        userDetailsService.insertUserDetails(userDetailsDTOList);
        return "User details inserted successfully!";
    }
}
