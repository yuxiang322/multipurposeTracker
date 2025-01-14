package com.xiang.multipurposeTracker.DTO;


public class HeaderDetailsDTO {
    private int headerID;
    private int tableID;
    private String headerName;
    private String headerFillColour;
    private String headerTextColour;
    private Boolean textBold;

    public HeaderDetailsDTO() {}

    public HeaderDetailsDTO(int headerID, int tableID, String headerName, String headerFillColour, String headerTextColour, Boolean textBold) {
        this.headerID = headerID;
        this.tableID = tableID;
        this.headerName = headerName;
        this.headerFillColour = headerFillColour;
        this.headerTextColour = headerTextColour;
        this.textBold = textBold;
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
