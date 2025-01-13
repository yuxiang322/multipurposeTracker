package com.xiang.multipurposeTracker.DTO;

public class TableDetailsDTO {
    private int tableDetailsID;
    private Integer tableID;
    private String jsonData;

    public TableDetailsDTO(){}

    public TableDetailsDTO(int tableDetailsID, Integer tableID, String jsonData) {
        this.tableDetailsID = tableDetailsID;
        this.tableID = tableID;
        this.jsonData = jsonData;
    }

    public int getTableDetailsID() {
        return tableDetailsID;
    }

    public void setTableDetailsID(int tableDetailsID) {
        this.tableDetailsID = tableDetailsID;
    }

    public Integer getTableID() {
        return tableID;
    }

    public void setTableID(Integer tableID) {
        this.tableID = tableID;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
