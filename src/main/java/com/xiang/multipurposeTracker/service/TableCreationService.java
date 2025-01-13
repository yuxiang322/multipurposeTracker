package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.HeaderDetailsDTO;
import com.xiang.multipurposeTracker.DTO.TableCreationDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableCreationService {
    //Repo

    // Add table
    @Transactional
    public String addTable(TableCreationDTO tableCreationDTO) {
        try {
            // insert to TemplateTables
            int tableId = tableCreationDTO.getTemplateTables().getTableID();

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

    }

    private void insertHeaderDetails(int tableID, List<HeaderDetailsDTO> headerDetailsDTOList) {

    }
}
