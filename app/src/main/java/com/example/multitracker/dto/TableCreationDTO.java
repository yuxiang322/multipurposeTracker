package com.example.multitracker.dto;

import java.util.ArrayList;
import java.util.List;

public class TableCreationDTO {
    private TemplateTablesDTO templateTables;
    private List<HeaderDetailsDTO> headerDetailsList;

    public TableCreationDTO() {

    }
    public TableCreationDTO(TemplateTablesDTO templateTables, List<HeaderDetailsDTO> headerDetailsList){
        this.templateTables = templateTables;
        this.headerDetailsList = headerDetailsList;
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
}
