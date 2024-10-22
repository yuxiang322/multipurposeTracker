package com.xiang.multipurposeTracker.controller;

import com.xiang.multipurposeTracker.DTO.NotificationDTO;
import com.xiang.multipurposeTracker.DTO.TemplateDTO;
import com.xiang.multipurposeTracker.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/getNotification")
    public ResponseEntity<NotificationDTO> getNotification(@RequestBody TemplateDTO templateID){

        NotificationDTO notification = notificationService.findByTemplateID(templateID.getTemplateID());  // Example of retrieving data
        if (notification.getNotificationID() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/updateNotification")
    public ResponseEntity<String> updateNotification(@RequestBody NotificationDTO changeNotification){

        Boolean response = notificationService.updateNotification(changeNotification);
        if(response){
            return ResponseEntity.ok("Updated");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
