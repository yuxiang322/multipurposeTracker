package com.example.multitracker.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class HeaderDetailsDTO implements Parcelable {
    private int headerID;
    private int tableID;
    private String headerName;
    private String headerFillColour;
    private String headerTextColour;
    private Boolean textBold;

    public HeaderDetailsDTO()  {
    }

    public HeaderDetailsDTO(int headerID, int tableID, String headerName, String headerFillColour, String headerTextColour) {
        this.headerID = headerID;
        this.tableID = tableID;
        this.headerName = headerName;
        this.headerFillColour = headerFillColour;
        this.headerTextColour = headerTextColour;
    }

    protected HeaderDetailsDTO(Parcel in) {
        headerID = in.readInt();
        tableID = in.readInt();
        headerName = in.readString();
        headerFillColour = in.readString();
        headerTextColour = in.readString();
        byte tmpTextBold = in.readByte();
        textBold = tmpTextBold == 0 ? null : tmpTextBold == 1;
    }

    public static final Creator<HeaderDetailsDTO> CREATOR = new Creator<HeaderDetailsDTO>() {
        @Override
        public HeaderDetailsDTO createFromParcel(Parcel in) {
            return new HeaderDetailsDTO(in);
        }

        @Override
        public HeaderDetailsDTO[] newArray(int size) {
            return new HeaderDetailsDTO[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(headerID);
        dest.writeInt(tableID);
        dest.writeString(headerName);
        dest.writeString(headerFillColour);
        dest.writeString(headerTextColour);
        dest.writeByte((byte) (textBold == null ? 0 : textBold ? 1 : 2));
    }
}
