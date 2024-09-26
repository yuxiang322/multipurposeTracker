package com.xiang.multipurposeTracker.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "HeaderDetails")
public class HeaderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HeaderID")
    private int headerID;
    @Column(name = "TableID")
    private int tableID;
    @Column(name = "HeaderName")
    private String headerName;
    @Column(name = "HeaderFillColour")
    private String headerFillColour;
    @Column(name = "HeaderTextColour")
    private String headerTextColour;
    @Column(name = "TextBold")
    private Boolean textBold;

    public HeaderDetails() {}

    public HeaderDetails(int headerID, int tableID, String headerName, String headerFillColour, String headerTextColour) {
        this.headerID = headerID;
        this.tableID = tableID;
        this.headerName = headerName;
        this.headerFillColour = headerFillColour;
        this.headerTextColour = headerTextColour;
    }

    public Boolean getTextBold() {
        return textBold;
    }

    public void setTextBold(Boolean textBold) {
        this.textBold = textBold;
    }

    public int getHeaderID() {
        return headerID;
    }

    public void setHeaderID(int headerID) {
        this.headerID = headerID;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderFillColour() {
        return headerFillColour;
    }

    public void setHeaderFillColour(String headerFillColour) {
        this.headerFillColour = headerFillColour;
    }

    public String getHeaderTextColour() {
        return headerTextColour;
    }

    public void setHeaderTextColour(String headerTextColour) {
        this.headerTextColour = headerTextColour;
    }
}
