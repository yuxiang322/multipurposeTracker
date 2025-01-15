package com.example.multitracker.dto;

import com.example.multitracker.TemplateDetails;

import java.util.List;

public class RetrieveTableDetailsDTO {

    private TemplateTablesDTO templateTables;
    private List<HeaderDetailsDTO> headerDetailsList;

    private TableDetailsDTO tableDetails;

    public RetrieveTableDetailsDTO() {
    }

    public RetrieveTableDetailsDTO(TemplateTablesDTO templateTables, List<HeaderDetailsDTO> headerDetailsList, TableDetailsDTO tableDetails) {
        this.templateTables = templateTables;
        this.headerDetailsList = headerDetailsList;
        this.tableDetails = tableDetails;
    }

    public TemplateTablesDTO getTemplateTables() {
        return templateTables;
    }

    public void setTemplateTables(TemplateTablesDTO templateTables) {
        this.templateTables = templateTables;
    }

    public List<HeaderDetailsDTO> getHeaderDetailsList() {
        return headerDetailsList;
    }

    public void setHeaderDetailsList(List<HeaderDetailsDTO> headerDetailsList) {
        this.headerDetailsList = headerDetailsList;
    }

    public TableDetailsDTO getTableDetails() {
        return tableDetails;
    }

    public void setTableDetails(TableDetailsDTO tableDetails) {
        this.tableDetails = tableDetails;
    }
}
