package com.xiang.multipurposeTracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiang.multipurposeTracker.DTO.HeaderDetailsDTO;
import com.xiang.multipurposeTracker.DTO.TableCreationDTO;
import com.xiang.multipurposeTracker.DTO.TemplateTablesDTO;
import com.xiang.multipurposeTracker.entities.HeaderDetails;
import com.xiang.multipurposeTracker.entities.TableDetails;
import com.xiang.multipurposeTracker.entities.TemplateTables;
import com.xiang.multipurposeTracker.repository.HeaderDetailsRepository;
import com.xiang.multipurposeTracker.repository.TableDetailsRepository;
import com.xiang.multipurposeTracker.repository.TemplateTablesRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TableCreationService {
    //Repo
    @Autowired
    private TemplateTablesRepository templateTablesRepository;
    @Autowired
    private TableDetailsRepository tableDetailsRepository;
    @Autowired
    private HeaderDetailsRepository headerDetailsRepository;

    // Add table
    @Transactional
    public String addTable(TableCreationDTO tableCreationDTO) {
        try {
            TemplateTables templateTableAdd = new TemplateTables();
            templateTableAdd.setTableName(tableCreationDTO.getTemplateTables().getTableName());
            templateTableAdd.setTemplateID(tableCreationDTO.getTemplateTables().getTemplateID());
            templateTablesRepository.save(templateTableAdd);
            int tableId = templateTableAdd.getTableID();

            // Create Json data insert to TableDetails
            insertTableDetails(tableId, tableCreationDTO.getHeaderDetailsList());
            // insert to HeaderDetails
            insertHeaderDetails(tableId, tableCreationDTO.getHeaderDetailsList());

            return "Test";
        } catch (Exception e) {
            return "Failed " + e.getMessage();
        }
    }

    private void insertTableDetails(int tableID, List<HeaderDetailsDTO> headerDetailsDTOList) {
        try{
            // Set jsonData just need the HeaderName
            Map<String, List<String>> headerDataMap = new HashMap<>();

            for(HeaderDetailsDTO currentHeaderDetails : headerDetailsDTOList){
                String headerName = currentHeaderDetails.getHeaderName();
                headerDataMap.putIfAbsent(headerName, new ArrayList<>());
            }

            ObjectMapper headerNamesMapper = new ObjectMapper();
            String headerJsonData = headerNamesMapper.writeValueAsString(headerDataMap);

            // table details object
            TableDetails tableDetailsAdd = new TableDetails();
            tableDetailsAdd.setTableID(tableID);
            tableDetailsAdd.setJsonData(headerJsonData);
            // save
            tableDetailsRepository.save(tableDetailsAdd);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void insertHeaderDetails(int tableID, List<HeaderDetailsDTO> headerDetailsDTOList) {
        try{
            List<HeaderDetails> headerDetailsList = new ArrayList<>();

            for(HeaderDetailsDTO tempHeaderDetails : headerDetailsDTOList){
                HeaderDetails headerDetailsAdd = new HeaderDetails();
                headerDetailsAdd.setTableID(tableID);
                headerDetailsAdd.setHeaderName(tempHeaderDetails.getHeaderName());
                headerDetailsAdd.setHeaderFillColour(tempHeaderDetails.getHeaderFillColour());
                headerDetailsAdd.setHeaderTextColour(tempHeaderDetails.getHeaderTextColour());
                headerDetailsAdd.setTextBold(tempHeaderDetails.getTextBold());

                headerDetailsList.add(headerDetailsAdd);
            }
            headerDetailsRepository.saveAll(headerDetailsList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
