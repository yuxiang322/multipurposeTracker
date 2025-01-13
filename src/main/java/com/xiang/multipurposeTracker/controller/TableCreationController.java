package com.xiang.multipurposeTracker.controller;

import com.xiang.multipurposeTracker.DTO.TableCreationDTO;
import com.xiang.multipurposeTracker.service.TableCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/table")
public class TableCreationController {

    @Autowired
    private TableCreationService tableCreationService;

    @PostMapping("/create")
    public ResponseEntity<String> tableCreate(@RequestBody TableCreationDTO tableCreationDTO) {

        // Service

        return ResponseEntity.ok("Test");
    }

}
