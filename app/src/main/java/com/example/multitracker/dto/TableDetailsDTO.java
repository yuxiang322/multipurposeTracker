package com.example.multitracker.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TableDetailsDTO implements Parcelable {
    private int tableDetailsID;
    private Integer tableID;
    private String jsonData;

    public TableDetailsDTO() {
    }

    public TableDetailsDTO(int tableDetailsID, Integer tableID, String jsonData) {
        this.tableDetailsID = tableDetailsID;
        this.tableID = tableID;
        this.jsonData = jsonData;
    }

    protected TableDetailsDTO(Parcel in) {
        tableDetailsID = in.readInt();
        if (in.readByte() == 0) {
            tableID = null;
        } else {
            tableID = in.readInt();
        }
        jsonData = in.readString();
    }

    public static final Creator<TableDetailsDTO> CREATOR = new Creator<TableDetailsDTO>() {
        @Override
        public TableDetailsDTO createFromParcel(Parcel in) {
            return new TableDetailsDTO(in);
        }

        @Override
        public TableDetailsDTO[] newArray(int size) {
            return new TableDetailsDTO[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(tableDetailsID);
        if (tableID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(tableID);
        }
        dest.writeString(jsonData);
    }
}
