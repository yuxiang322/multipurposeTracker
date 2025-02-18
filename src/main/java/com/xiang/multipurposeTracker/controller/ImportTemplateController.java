package com.xiang.multipurposeTracker.controller;

import com.xiang.multipurposeTracker.DTO.ImportTemplateDTO;
import com.xiang.multipurposeTracker.service.ImportTemplateService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import")
public class ImportTemplateController {
    @Autowired
    private ImportTemplateService importTemplateService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ImportTemplateController.class);
    @PostMapping("/template")
    public ResponseEntity<String> importTemplate(@RequestBody ImportTemplateDTO importTemplateDTO){
        try{
            logger.info("Controller Useruid??: " + importTemplateDTO.getUserUID());
            String result = importTemplateService.importTemplate(importTemplateDTO);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
