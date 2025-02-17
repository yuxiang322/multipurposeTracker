package com.xiang.multipurposeTracker.controller;

import com.xiang.multipurposeTracker.DTO.ImportTemplateDTO;
import com.xiang.multipurposeTracker.service.ImportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/import")
public class ImportTemplateController {
    @Autowired
    private ImportTemplateService importTemplateService;

    ResponseEntity<String> importTemplate(@RequestBody ImportTemplateDTO importTemplateDTO){
        try{

            String result = importTemplateService.importTemplate(importTemplateDTO);

            if(result.equals("No records found for sharecode.")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }

            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
