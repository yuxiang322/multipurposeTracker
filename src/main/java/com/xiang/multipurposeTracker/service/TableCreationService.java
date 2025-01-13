package com.xiang.multipurposeTracker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableCreationService {
    //Repo

    // Add table
    @Transactional
    public String addTable(){

        // insert to TemplateTables
        // Create Json data insert to TableDetails
        // insert to HeaderDetails

        return "Test";
    }
}
