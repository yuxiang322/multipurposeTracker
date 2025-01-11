package com.example.multitracker.dto;

public class TemplateTablesDTO {
    private int tableID;
    private int templateID;
    private String tableName;

    public TemplateTablesDTO() {
    }

    public TemplateTablesDTO(int tableID, int templateID, String tableName) {
        this.tableID = tableID;
        this.templateID = templateID;
        this.tableName = tableName;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public int getTemplateID() {
        return templateID;
    }

    public void setTemplateID(int templateID) {
        this.templateID = templateID;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
