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
        try {
            String responseMessage = templateService.createTemplate(templateRequest);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Log the error for debugging
            System.err.println("Error in controller: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete Template
    @PostMapping("/deleteTemplate")
    public ResponseEntity<String> deleteTemplate(@RequestBody TemplateDTO templateRequest) {
        try {
            String responseMessage = templateService.deleteTemplate(templateRequest);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Log the error for debugging
            System.err.println("Error in controller: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Save Template details
    @PostMapping("/saveTemplateDetails")
    public ResponseEntity<String> saveTemplateDetails(@RequestBody TemplateDTO saveTemplateDetails){
        try{
            String saveTemplateResponse = templateService.saveTemplateDetails(saveTemplateDetails);

            return ResponseEntity.ok(saveTemplateResponse);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update Details");
        }
    }
}
