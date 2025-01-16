package com.xiang.multipurposeTracker.service;


import com.xiang.multipurposeTracker.DTO.HeaderDetailsDTO;
import com.xiang.multipurposeTracker.DTO.RetrieveTableDetailsDTO;
import com.xiang.multipurposeTracker.DTO.TableDetailsDTO;
import com.xiang.multipurposeTracker.DTO.TemplateTablesDTO;
import com.xiang.multipurposeTracker.entities.HeaderDetails;
import com.xiang.multipurposeTracker.entities.TableDetails;
import com.xiang.multipurposeTracker.entities.TemplateTables;
import com.xiang.multipurposeTracker.repository.HeaderDetailsRepository;
import com.xiang.multipurposeTracker.repository.TableDetailsRepository;
import com.xiang.multipurposeTracker.repository.TemplateTablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RetrieveTableService {

    @Autowired
    private TemplateTablesRepository templateTablesRepository;
    @Autowired
    private HeaderDetailsRepository headerDetailsRepository;
    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    // Retrieve table details
    public List<RetrieveTableDetailsDTO> retrieveListOfTable(int templateID) {
        try {
            List<RetrieveTableDetailsDTO> listOfTables = new ArrayList<>();

            // Template Table
            List<TemplateTables> templateTablesList = templateTablesRepository.findTemplateTablesByTemplateID(templateID);

            for (TemplateTables templateTable : templateTablesList) {

                RetrieveTableDetailsDTO tempTableData = new RetrieveTableDetailsDTO();

                // TemplateTable DTO
                TemplateTablesDTO templateTablesDTO = new TemplateTablesDTO();
                templateTablesDTO.setTableID(templateTable.getTableID());
                templateTablesDTO.setTemplateID(templateTable.getTemplateID());
                templateTablesDTO.setTableName(templateTable.getTableName());

                tempTableData.setTemplateTables(templateTablesDTO);

                // Header Details
                tempTableData.setHeaderDetailsList(getListHeaderDetails(templateTable.getTableID()));
                // Table Details
                tempTableData.setTableDetails(getTableDetailsDTO(templateTable.getTableID()));

                listOfTables.add(tempTableData);
            }
            return listOfTables;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<HeaderDetailsDTO> getListHeaderDetails(int tableID) {
        try {
            List<HeaderDetailsDTO> headerDetailsDTOList = new ArrayList<>();
            List<HeaderDetails> headerDetailsList = headerDetailsRepository.findHeaderDetailsByTableID(tableID);

            for (HeaderDetails headerDetail : headerDetailsList) {

                HeaderDetailsDTO headerDetailsDTO = new HeaderDetailsDTO();
                headerDetailsDTO.setHeaderID(headerDetail.getHeaderID());
                headerDetailsDTO.setTableID(headerDetail.getTableID());
                headerDetailsDTO.setHeaderName(headerDetail.getHeaderName());
                headerDetailsDTO.setHeaderTextColour(headerDetail.getHeaderTextColour());
                headerDetailsDTO.setHeaderFillColour(headerDetail.getHeaderFillColour());
                headerDetailsDTO.setTextBold(headerDetail.getTextBold());

                headerDetailsDTOList.add(headerDetailsDTO);
            }
            return headerDetailsDTOList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TableDetailsDTO getTableDetailsDTO(int tableID){
        try{
            TableDetailsDTO tableDetailsDTO = new TableDetailsDTO();
            TableDetails tableDetails = tableDetailsRepository.findTableDetailsByTableID(tableID);

            tableDetailsDTO.setTableDetailsID(tableDetails.getTableDetailsID());
            tableDetailsDTO.setTableID(tableDetails.getTableID());
            tableDetailsDTO.setJsonData(tableDetails.getJsonData());

            return tableDetailsDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
