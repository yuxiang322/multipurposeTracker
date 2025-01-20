package com.example.multitracker.dto;

import android.os.Parcel;
import android.os.Parcelable;


import androidx.annotation.NonNull;

import java.util.List;

public class RetrieveTableDetailsDTO implements Parcelable {

    private TemplateTablesDTO templateTables;
    private List<HeaderDetailsDTO> headerDetailsList;
    private TableDetailsDTO tableDetails;

    public RetrieveTableDetailsDTO() {
    }

    public RetrieveTableDetailsDTO(TemplateTablesDTO templateTables, List<HeaderDetailsDTO> headerDetailsList, TableDetailsDTO tableDetails) {
        this.templateTables = templateTables;
        this.headerDetailsList = headerDetailsList;
        this.tableDetails = tableDetails;
    }

    protected RetrieveTableDetailsDTO(Parcel in) {
        templateTables = in.readParcelable(TemplateTablesDTO.class.getClassLoader());
        headerDetailsList = in.createTypedArrayList(HeaderDetailsDTO.CREATOR);
        tableDetails = in.readParcelable(TableDetailsDTO.class.getClassLoader());
    }

    public static final Creator<RetrieveTableDetailsDTO> CREATOR = new Creator<RetrieveTableDetailsDTO>() {
        @Override
        public RetrieveTableDetailsDTO createFromParcel(Parcel in) {
            return new RetrieveTableDetailsDTO(in);
        }

        @Override
        public RetrieveTableDetailsDTO[] newArray(int size) {
            return new RetrieveTableDetailsDTO[size];
        }
    };

    public TemplateTablesDTO getTemplateTables() {
        return templateTables;
    }

    public void setTemplateTables(TemplateTablesDTO templateTables) {
        this.templateTables = templateTables;
    }

    public List<HeaderDetailsDTO> getHeaderDetailsList() {
        return headerDetailsList;
    }

    public void setHeaderDetailsList(List<HeaderDetailsDTO> headerDetailsList) {
        this.headerDetailsList = headerDetailsList;
    }

    public TableDetailsDTO getTableDetails() {
        return tableDetails;
    }

    public void setTableDetails(TableDetailsDTO tableDetails) {
        this.tableDetails = tableDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(templateTables, flags);
        dest.writeTypedList(headerDetailsList);
        dest.writeParcelable(tableDetails, flags);
    }
}
