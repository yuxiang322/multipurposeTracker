package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.ReportStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class EmailSchedulingService {

    @Autowired
    private JavaMailSender mailSender;

    // implement email scheduling

    public boolean scheduleEmail(ReportStatusDTO reportStatusDTO){
        boolean scheduleEmailFlag = false;
        boolean emailScheduled = false;

        if(reportStatusDTO != null){
            scheduleEmailFlag = reportStatusDTO.getReportFlag();
        }

        if(scheduleEmailFlag){
            // set up scheduling
            // time string, date string = utc zone
            // interval in minutes
            long repeatIntervalMilli = Long.parseLong(reportStatusDTO.getRepeatInterval()) * 60 * 1000;
            LocalDateTime utcTime = LocalDateTime.of(LocalDate.parse(reportStatusDTO.getRepeatStartDate()), LocalTime.parse(reportStatusDTO.getRepeatStartTime()));

        }

        return emailScheduled;
    }

    public boolean testSchedulingEmail(){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("lgzai96@live.com");
            message.setSubject("Test Email");
            message.setText("This is a test email sent from Spring Boot.");

            mailSender.send(message);

            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void restoreEmailScheduling(){

    }
}
