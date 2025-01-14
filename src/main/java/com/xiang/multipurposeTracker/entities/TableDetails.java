package com.xiang.multipurposeTracker.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "TableDetails")
public class TableDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Tabledetailsid")
    private int tableDetailsID;
    @Column(name = "Tableid")
    private Integer tableID;
    @Column(name = "Jsondata")
    private String jsonData;

    public TableDetails(){}

    public TableDetails(int tableDetailsID, Integer tableID, String jsonData) {
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
