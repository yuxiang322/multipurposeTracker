package com.example.multitracker.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class NotificationDTO implements Parcelable {
    private int notificationID;
    private Integer templateID;
    private String userUID;
    private Boolean notificationFlag;
    private Boolean smsFlag;
    private Boolean whatsAppFlag;
    private String repeatStartDate;
    private String repeatStartTime;
    private String repeatDays;

    public NotificationDTO(){}

    protected NotificationDTO(Parcel in) {
        notificationID = in.readInt();
        if (in.readByte() == 0) {
            templateID = null;
        } else {
            templateID = in.readInt();
        }
        userUID = in.readString();
        byte tmpNotificationFlag = in.readByte();
        notificationFlag = tmpNotificationFlag == 0 ? null : tmpNotificationFlag == 1;
        byte tmpSmsFlag = in.readByte();
        smsFlag = tmpSmsFlag == 0 ? null : tmpSmsFlag == 1;
        byte tmpWhatsAppFlag = in.readByte();
        whatsAppFlag = tmpWhatsAppFlag == 0 ? null : tmpWhatsAppFlag == 1;
        repeatStartDate = in.readString();
        repeatStartTime = in.readString();
        repeatDays = in.readString();
    }

    public static final Creator<NotificationDTO> CREATOR = new Creator<NotificationDTO>() {
        @Override
        public NotificationDTO createFromParcel(Parcel in) {
            return new NotificationDTO(in);
        }

        @Override
        public NotificationDTO[] newArray(int size) {
            return new NotificationDTO[size];
        }
    };

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public Integer getTemplateID() {
        return templateID;
    }

    public void setTemplateID(Integer templateID) {
        this.templateID = templateID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public Boolean getNotificationFlag() {
        return notificationFlag;
    }

    public void setNotificationFlag(Boolean notificationFlag) {
        this.notificationFlag = notificationFlag;
    }

    public Boolean getSmsFlag() {
        return smsFlag;
    }

    public void setSmsFlag(Boolean smsFlag) {
        this.smsFlag = smsFlag;
    }

    public Boolean getWhatsAppFlag() {
        return whatsAppFlag;
    }

    public void setWhatsAppFlag(Boolean whatsAppFlag) {
        this.whatsAppFlag = whatsAppFlag;
    }

    public String getRepeatStartDate() {
        return repeatStartDate;
    }

    public void setRepeatStartDate(String repeatStartDate) {
        this.repeatStartDate = repeatStartDate;
    }

    public String getRepeatStartTime() {
        return repeatStartTime;
    }

    public void setRepeatStartTime(String repeatStartTime) {
        this.repeatStartTime = repeatStartTime;
    }

    public String getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(notificationID);
        if (templateID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(templateID);
        }
        dest.writeString(userUID);
        dest.writeByte((byte) (notificationFlag == null ? 0 : notificationFlag ? 1 : 2));
        dest.writeByte((byte) (smsFlag == null ? 0 : smsFlag ? 1 : 2));
        dest.writeByte((byte) (whatsAppFlag == null ? 0 : whatsAppFlag ? 1 : 2));
        dest.writeString(repeatStartDate);
        dest.writeString(repeatStartTime);
        dest.writeString(repeatDays);
    }
}
