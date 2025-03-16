package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.NotificationDTO;
import com.xiang.multipurposeTracker.DTO.NotificationReportDTO;
import com.xiang.multipurposeTracker.DTO.ReportStatusDTO;
import com.xiang.multipurposeTracker.component.TaskSchedulerConfig;
import com.xiang.multipurposeTracker.entities.UserDetails;
import com.xiang.multipurposeTracker.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;

@Service
public class EmailSchedulingService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private TaskSchedulerConfig taskScheduler;
    private List<ScheduledFuture<?>> scheduledTasks = new ArrayList<>();

    @Async
    public CompletableFuture<Boolean> scheduleEmail(NotificationReportDTO notificationReportDTO){

        String useruid = null;
        boolean scheduleEmailFlag = false;
        boolean emailScheduled = false;
        String repeatStartDate = null;
        String repeatStartTime = null;

        ReportStatusDTO reportStatusDTO = (notificationReportDTO != null ? notificationReportDTO.getReportStatusDTO() : null);
        NotificationDTO notificationDTO = (notificationReportDTO != null ? notificationReportDTO.getNotificationDTO() : null);

        if(reportStatusDTO != null && notificationDTO != null){
            scheduleEmailFlag = reportStatusDTO.getReportFlag();
            useruid = notificationDTO.getUserUID();
            repeatStartDate = reportStatusDTO.getRepeatStartDate();
            repeatStartTime = reportStatusDTO.getRepeatStartTime();
        }

       String emailRecipient = getUserEmail(useruid);

        if(emailRecipient != null && scheduleEmailFlag && repeatStartDate != null && repeatStartTime != null){

            long repeatIntervalMilli = Long.parseLong(reportStatusDTO.getRepeatInterval()) * 60 * 1000;
            LocalDateTime repeatStartDateTimeUTC = LocalDateTime.parse(repeatStartDate + "T" + repeatStartTime);
            Instant firstInstance = repeatStartDateTimeUTC.atZone(ZoneId.of("UTC")).toInstant();


            Runnable emailTask = () -> {
                try {
                    sendEmailNotification(emailRecipient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            ScheduledFuture<?> scheduledTask = taskScheduler.taskScheduler().scheduleAtFixedRate(emailTask, firstInstance, Duration.ofMillis(repeatIntervalMilli));
            scheduledTasks.add(scheduledTask);

            emailScheduled = true;
            printScheduledTasks();
        }

        return CompletableFuture.completedFuture(emailScheduled);
    }

    public void sendEmailNotification(String emailRecipient){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRecipient);
            message.setSubject("Test Email");
            message.setText("This is a test email sent from Spring Boot.");

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUserEmail(String useruid){
        UserDetails userDetails = userDetailsRepository.findByUserUID(useruid);

        if(userDetails != null){
            return userDetails.getEmail();
        }

        return null;
    }

    //test
    public void printScheduledTasks() {
        if (scheduledTasks.isEmpty()) {
            System.out.println("No scheduled tasks");
        } else {
            for (ScheduledFuture<?> task : scheduledTasks) {
                System.out.println("Scheduled Task: " + task);
            }
        }
    }


    public void restoreEmailScheduling(){

    }
}
