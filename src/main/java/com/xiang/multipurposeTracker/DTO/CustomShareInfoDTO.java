package com.xiang.multipurposeTracker.DTO;

import java.util.List;

public class CustomShareInfoDTO {

    private String templateName;
    private String templateDescription;
    private List<TemplateTablesDTO> templateTables;
    private List<HeaderDetailsDTO> headerDetails;
    private List<TableDetailsDTO> tableDetails;

    public CustomShareInfoDTO() {
    }

    public CustomShareInfoDTO(String templateName, String templateDescription) {
        this.templateName = templateName;
        this.templateDescription = templateDescription;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateDescription() {
        return templateDescription;
    }

    public void setTemplateDescription(String templateDescription) {
        this.templateDescription = templateDescription;
    }

    public List<TemplateTablesDTO> getTemplateTables() {
        return templateTables;
    }

    public void setTemplateTables(List<TemplateTablesDTO> templateTables) {
        this.templateTables = templateTables;
    }

    public List<HeaderDetailsDTO> getHeaderDetails() {
        return headerDetails;
    }

    public void setHeaderDetails(List<HeaderDetailsDTO> headerDetails) {
        this.headerDetails = headerDetails;
    }

    public List<TableDetailsDTO> getTableDetails() {
        return tableDetails;
    }

    public void setTableDetails(List<TableDetailsDTO> tableDetails) {
        this.tableDetails = tableDetails;
    }
}
