package com.example.multitracker.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TemplateDTO implements Parcelable {

    private int templateID;
    private String userUID;
    private String templateName;
    private String dateCreated;
    private String templateDescription;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TemplateDTO() {
    }

    public TemplateDTO(int templateID, String userUID, String templateName, String dateCreated, String templateDescription) {
        this.templateID = templateID;
        this.userUID = userUID;
        this.templateName = templateName;
        this.dateCreated = dateCreated;
        this.templateDescription = templateDescription;
    }

    public String getTemplateDescription() {
        return templateDescription;
    }

    public void setTemplateDescription(String templateDescription) {
        this.templateDescription = templateDescription;
    }

    public int getTemplateID() {
        return templateID;
    }

    public void setTemplateID(int templateID) {
        this.templateID = templateID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated != null ? dateCreated.format(FORMATTER) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(templateID);
        dest.writeString(userUID);
        dest.writeString(templateName);
        dest.writeString(dateCreated);  // Write dateCreated as String
        dest.writeString(templateDescription);
    }

    public static final Creator<TemplateDTO> CREATOR = new Creator<TemplateDTO>() {
        @Override
        public TemplateDTO createFromParcel(Parcel in) {
            int templateID = in.readInt();
            String userUID = in.readString();
            String templateName = in.readString();
            String dateCreatedStr = in.readString();  // Read dateCreated as String
            String templateDescription = in.readString();

            return new TemplateDTO(templateID, userUID, templateName, dateCreatedStr, templateDescription);
        }

        @Override
        public TemplateDTO[] newArray(int size) {
            return new TemplateDTO[size];
        }
    };
}
