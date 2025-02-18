package com.xiang.multipurposeTracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiang.multipurposeTracker.DTO.*;
import com.xiang.multipurposeTracker.entities.HeaderDetails;
import com.xiang.multipurposeTracker.entities.ShareTable;
import com.xiang.multipurposeTracker.entities.TableDetails;
import com.xiang.multipurposeTracker.entities.TemplateTables;
import com.xiang.multipurposeTracker.repository.HeaderDetailsRepository;
import com.xiang.multipurposeTracker.repository.ShareTableRepository;
import com.xiang.multipurposeTracker.repository.TableDetailsRepository;
import com.xiang.multipurposeTracker.repository.TemplateTablesRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImportTemplateService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ImportTemplateService.class);
    @Autowired
    private TemplateService templateService;
    @Autowired
    private ShareTableRepository shareTableRepository;
    @Autowired
    private TemplateTablesRepository templateTablesRepository;
    @Autowired
    private HeaderDetailsRepository headerDetailsRepository;
    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    @Transactional
    public String importTemplate(ImportTemplateDTO importTemplateDTO) {

        try {
            String userUID = importTemplateDTO.getUserUID();
            String shareCode = importTemplateDTO.getShareCode();
            String result = "Invalid Code";

            // Get templateDetails from share table
            ShareTable existShareTable = shareTableRepository.findBySharingCode(shareCode);

            if (existShareTable != null) {
                String templateDetailsForImport = existShareTable.getTemplateDetails();

                // Convert to json
                ObjectMapper objectMapper = new ObjectMapper();
                CustomShareInfoDTO customShareInfoDTO = objectMapper.readValue(templateDetailsForImport, CustomShareInfoDTO.class);
                //test
//                String prettyPrintedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(customShareInfoDTO);
//                logger.info("CustomShareInfoDTO: " + prettyPrintedJson);

                String templateName = customShareInfoDTO.getTemplateName();
                String TemplateDescription = customShareInfoDTO.getTemplateDescription();
                Map<Integer, List<TemplateTablesDTO>> templateTablesDTOHashMap = new HashMap<>();
                Map<Integer, List<HeaderDetailsDTO>> headerDetailsDTOHashMap = new HashMap<>();
                Map<Integer, List<TableDetailsDTO>> tableDetailsDTOHashMap = new HashMap<>();

                for (TemplateTablesDTO currentTable : customShareInfoDTO.getTemplateTables()) {
                    templateTablesDTOHashMap.computeIfAbsent(currentTable.getTableID(), k -> new ArrayList<>()).add(currentTable);
                }

                for (HeaderDetailsDTO currentHeader : customShareInfoDTO.getHeaderDetails()) {
                    headerDetailsDTOHashMap.computeIfAbsent(currentHeader.getTableID(), k -> new ArrayList<>()).add(currentHeader);
                }

                for (TableDetailsDTO currentTableDetails : customShareInfoDTO.getTableDetails()) {
                    tableDetailsDTOHashMap.computeIfAbsent(currentTableDetails.getTableID(), k -> new ArrayList<>()).add(currentTableDetails);
                }


                // Template creation
                TemplateDTO templateDTO = new TemplateDTO();
                templateDTO.setUserUID(userUID);
                templateDTO.setTemplateName(templateName);
                templateDTO.setTemplateDescription(TemplateDescription);
                templateDTO.setDateCreated(LocalDateTime.now());
                String templateID = templateService.createTemplate(templateDTO);


                // TemplateTable, headerDetails, TableDetails creation
                for (Map.Entry<Integer, List<TemplateTablesDTO>> entry : templateTablesDTOHashMap.entrySet()) {
                    int refTableID = entry.getKey();
                    List<TemplateTablesDTO> refTablesLists = entry.getValue();

                    // Create table
                    TemplateTables tableInsert = new TemplateTables();
                    tableInsert.setTemplateID(Integer.parseInt(templateID));
                    tableInsert.setTableName(refTablesLists.get(0).getTableName());
                    TemplateTables saveTemplateTable = templateTablesRepository.save(tableInsert);
                    int newTableID = saveTemplateTable.getTableID();

                    // get header list from headerDetailshm using reftableid
                    // use newtableid to create records
                    List<HeaderDetailsDTO> refHeaderLists = headerDetailsDTOHashMap.get(refTableID);
                    if (refHeaderLists != null && !refHeaderLists.isEmpty()) {
                        for (HeaderDetailsDTO headerDetailsDTO : refHeaderLists) {
                            HeaderDetails headerInsert = new HeaderDetails();
                            headerInsert.setTableID(newTableID);
                            headerInsert.setHeaderName(headerDetailsDTO.getHeaderName());
                            headerInsert.setHeaderTextColour(headerDetailsDTO.getHeaderTextColour());
                            headerInsert.setHeaderFillColour(headerDetailsDTO.getHeaderFillColour());
                            headerInsert.setTextBold(headerDetailsDTO.getTextBold());

                            headerDetailsRepository.save(headerInsert);
                        }
                    }

                    // get 1 tabledetails useing ref tableid
                    // create using new tableid
                    List<TableDetailsDTO> tableDetailsDTO = tableDetailsDTOHashMap.get(refTableID);
                    if (tableDetailsDTO != null && !tableDetailsDTO.isEmpty()) {
                        TableDetails tableDetailsInsert = new TableDetails();
                        tableDetailsInsert.setTableID(newTableID);
                        tableDetailsInsert.setJsonData(tableDetailsDTO.get(0).getJsonData());

                        tableDetailsRepository.save(tableDetailsInsert);
                    }
                }
                result = "Success";
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
