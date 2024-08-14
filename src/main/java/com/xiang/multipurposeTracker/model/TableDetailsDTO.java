package com.xiang.multipurposeTracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TableDetails")
public class TableDetailsDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TableDetailsID")
    private int tableDetailsID;
    @Column(name = "TableID")
    private Integer tableID;
    @Column(name = "JsonData")
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
