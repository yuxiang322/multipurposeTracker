package com.example.multitracker.dto;

public class NotificationReportDTO {
    private NotificationDTO notificationDTO;
    private ReportStatusDTO reportStatusDTO;

    public NotificationReportDTO() {
    }

    public NotificationDTO getNotificationDTO() {
        return notificationDTO;
    }

    public void setNotificationDTO(NotificationDTO notificationDTO) {
        this.notificationDTO = notificationDTO;
    }

    public ReportStatusDTO getReportStatusDTO() {
        return reportStatusDTO;
    }

    public void setReportStatusDTO(ReportStatusDTO reportStatusDTO) {
        this.reportStatusDTO = reportStatusDTO;
    }
}
