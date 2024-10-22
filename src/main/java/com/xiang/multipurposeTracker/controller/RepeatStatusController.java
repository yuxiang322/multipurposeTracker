package com.xiang.multipurposeTracker.controller;


import com.xiang.multipurposeTracker.DTO.NotificationDTO;
import com.xiang.multipurposeTracker.DTO.RepeatStatusDTO;
import com.xiang.multipurposeTracker.service.RepeatStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/repeatStatus")
public class RepeatStatusController {

    @Autowired
    private RepeatStatusService repeatStatusService;

    @PostMapping("/getRepeatStatus")
    public ResponseEntity<RepeatStatusDTO> getNotification(@RequestBody NotificationDTO notificationID){

       RepeatStatusDTO repeatStatus = repeatStatusService.findRepeatStatusByNotificationID(notificationID.getNotificationID());
        if (repeatStatus == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(repeatStatus);
    }

    @PostMapping("/updateRepeatStatus")
    public ResponseEntity<String> updateRepeatStatus(@RequestBody RepeatStatusDTO changeRepeatStatus){

        Boolean response = repeatStatusService.updateRepeatStatus(changeRepeatStatus);
        if(response){
            return ResponseEntity.ok("Updated");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
