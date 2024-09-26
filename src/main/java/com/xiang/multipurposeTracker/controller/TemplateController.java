package com.xiang.multipurposeTracker.controller;

import com.xiang.multipurposeTracker.DTO.TemplateDTO;
import com.xiang.multipurposeTracker.DTO.UserUIDRequestDTO;
import com.xiang.multipurposeTracker.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    // Get Template
    @PostMapping("/getTemplates")
    public ResponseEntity<List<TemplateDTO>> getTemplates(@RequestBody UserUIDRequestDTO userUIDRequestDTO) {

        List<TemplateDTO> templates = templateService.getTemplatesByUserUID(userUIDRequestDTO.getUserUID());

        return ResponseEntity.ok(templates);
    }

    // Create Template
    @PostMapping("/createTemplate")
    public ResponseEntity<String> createTemplate(@RequestBody TemplateDTO templateRequest) {
        System.out.println("here? ");
        try {
            String responseMessage = templateService.createTemplate(templateRequest);
            // Return a 200 OK status with a success message
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Log the error for debugging
            System.err.println("Error in controller: " + e.getMessage());

            // Return a 400 or 500 status code based on the error
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete Template
    @PostMapping("/deleteTemplate")
    public ResponseEntity<String> deleteTemplate(@RequestBody TemplateDTO templateRequest) {

        try {
            String responseMessage = templateService.deleteTemplate(templateRequest);
            // Return a 200 OK status with a success message
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Log the error for debugging
            System.err.println("Error in controller: " + e.getMessage());

            // Return a 400 or 500 status code based on the error
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
