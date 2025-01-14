package com.xiang.multipurposeTracker.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "TemplateTables")
public class TemplateTables {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Tableid")
    private int tableID;
    @Column(name = "Templateid")
    private int templateID;
    @Column(name = "Tablename")
    private String tableName;


    public TemplateTables(){}

    public TemplateTables(int tableID, int templateID, String tableName) {
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
