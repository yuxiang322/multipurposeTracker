package com.xiang.multipurposeTracker.controller;

import com.xiang.multipurposeTracker.DTO.NotificationReportDTO;
import com.xiang.multipurposeTracker.DTO.TemplateDTO;
import com.xiang.multipurposeTracker.service.NotificationService;
import com.xiang.multipurposeTracker.service.ReportStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationReportController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ReportStatusService reportStatusService;

    @PostMapping("/getNotificationReport")
    public ResponseEntity<NotificationReportDTO> getNotification(@RequestBody TemplateDTO templateID){

        NotificationReportDTO notification = null;
        if (notification.getNotificationDTO().getNotificationID() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/updateNotificationReport")
    public ResponseEntity<String> updateNotification(@RequestBody NotificationReportDTO changeNotification){

        Boolean response = null;
        if(response){
            return ResponseEntity.ok("Updated");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
