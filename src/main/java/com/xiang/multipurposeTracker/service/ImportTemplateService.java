package com.xiang.multipurposeTracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiang.multipurposeTracker.DTO.*;
import com.xiang.multipurposeTracker.entities.ShareTable;
import com.xiang.multipurposeTracker.repository.HeaderDetailsRepository;
import com.xiang.multipurposeTracker.repository.ShareTableRepository;
import com.xiang.multipurposeTracker.repository.TableDetailsRepository;
import com.xiang.multipurposeTracker.repository.TemplateTablesRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImportTemplateService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ImportTemplateService.class);
    @Autowired
    private TemplateService templateService; // template, Notification, repeatstatus

    @Autowired
    private ShareTableRepository shareTableRepository;
    @Autowired
    private TemplateTablesRepository templateTablesRepository;
    @Autowired
    private HeaderDetailsRepository headerDetailsRepository;
    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    @Transactional
    public String importTemplate(ImportTemplateDTO importTemplateDTO){

        try{
            String userUID = importTemplateDTO.getUserUID();
            String shareCode = importTemplateDTO.getShareCode();
            String result = "Invalid Code";

            // Get templateDetails from share table
            ShareTable existShareTable = shareTableRepository.findBySharingCode(shareCode);

            if(existShareTable != null){
                String templateDetailsForImport = existShareTable.getTemplateDetails();

                // Mapping, TemplateTable, headerDetails, TableDetails
                String templateName = "Null";
                String TemplateDescription = "Null";
                Map<Integer, List<TemplateTablesDTO>> templateTablesDTOHashMap = new HashMap<>();
                Map<Integer, List<HeaderDetailsDTO>> headerDetailsDTOHashMap= new HashMap<>();
                Map<Integer, List<TableDetailsDTO>> tableDetailsDTOHashMap = new HashMap<>();

                // Convert to json
                ObjectMapper objectMapper = new ObjectMapper();
                CustomShareInfoDTO customShareInfoDTO = objectMapper.readValue(templateDetailsForImport, CustomShareInfoDTO.class);

                //test
                String prettyPrintedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(customShareInfoDTO);
                logger.info("CustomShareInfoDTO: " + prettyPrintedJson);


                // Template creation
                TemplateDTO templateDTO = new TemplateDTO();
                templateDTO.setUserUID(userUID);
                //templateDTO.setTemplateName(templateName);
                //templateDTO.setTemplateDescription(TemplateDescription);
                templateDTO.setDateCreated(LocalDateTime.now());
                //templateService.createTemplate(templateDTO);

                // TemplateTable, headerDetails, TableDetails creation
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
