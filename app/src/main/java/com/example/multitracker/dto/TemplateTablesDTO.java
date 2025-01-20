package com.example.multitracker.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TemplateTablesDTO implements Parcelable {
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

    protected TemplateTablesDTO(Parcel in) {
        tableID = in.readInt();
        templateID = in.readInt();
        tableName = in.readString();
    }

    public static final Creator<TemplateTablesDTO> CREATOR = new Creator<TemplateTablesDTO>() {
        @Override
        public TemplateTablesDTO createFromParcel(Parcel in) {
            return new TemplateTablesDTO(in);
        }

        @Override
        public TemplateTablesDTO[] newArray(int size) {
            return new TemplateTablesDTO[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(tableID);
        dest.writeInt(templateID);
        dest.writeString(tableName);
    }
}
