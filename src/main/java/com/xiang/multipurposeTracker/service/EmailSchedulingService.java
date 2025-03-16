package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.EmailSchedulingDTO;
import com.xiang.multipurposeTracker.DTO.NotificationDTO;
import com.xiang.multipurposeTracker.DTO.NotificationReportDTO;
import com.xiang.multipurposeTracker.DTO.ReportStatusDTO;
import com.xiang.multipurposeTracker.entities.UserDetails;
import com.xiang.multipurposeTracker.repository.EmailSchedulingRepository;
import com.xiang.multipurposeTracker.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class EmailSchedulingService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private TaskScheduler taskScheduler;
//    @Autowired
//    private EmailSchedulingRepository emailSchedulingRepository;

    private final Map<String, ScheduledFuture<?>> scheduledTaskCheckList = new ConcurrentHashMap<>();

    @Async
    public CompletableFuture<Boolean> scheduleEmail(NotificationReportDTO notificationReportDTO) {
        String useruid = null;
        boolean scheduleEmailFlag = false;
        boolean emailScheduled = false;
        String repeatStartDate = null;
        String repeatStartTime = null;

        ReportStatusDTO reportStatusDTO = (notificationReportDTO != null ? notificationReportDTO.getReportStatusDTO() : null);
        NotificationDTO notificationDTO = (notificationReportDTO != null ? notificationReportDTO.getNotificationDTO() : null);

        if (reportStatusDTO != null && notificationDTO != null) {
            scheduleEmailFlag = reportStatusDTO.getReportFlag();
            useruid = notificationDTO.getUserUID();
            repeatStartDate = reportStatusDTO.getRepeatStartDate();
            repeatStartTime = reportStatusDTO.getRepeatStartTime();
        }

        String emailRecipient = getUserEmail(useruid);// get email
        ScheduledFuture<?> existingSchedule = scheduledTaskCheckList.get(useruid); // retrieve schedule if any
        // must clean, reschedule each time user saves.
        if (existingSchedule != null) {
            existingSchedule.cancel(false); // cancel task
            scheduledTaskCheckList.remove(useruid); // remove keypair for garbage
        }

        if (emailRecipient != null && scheduleEmailFlag && repeatStartDate != null && repeatStartTime != null) {

            long repeatIntervalMilli = Long.parseLong(reportStatusDTO.getRepeatInterval()) * 60 * 1000;
            LocalDateTime repeatStartDateTimeUTC = LocalDateTime.parse(repeatStartDate + "T" + repeatStartTime);
            Instant firstInstance = repeatStartDateTimeUTC.atZone(ZoneId.of("UTC")).toInstant();

            Runnable emailTask = () -> {
                try {
                    sendEmailNotification(emailRecipient);
                } catch (MailException e) {
                    // future handling of invalid mail.
                    e.printStackTrace();
                }
            };

            ScheduledFuture<?> scheduledTask = taskScheduler.scheduleAtFixedRate(emailTask, firstInstance, Duration.ofMillis(repeatIntervalMilli));
            scheduledTaskCheckList.put(useruid, scheduledTask);// scheduledTaskCheckList add keypair

            emailScheduled = true;
        }

        return CompletableFuture.completedFuture(emailScheduled);
    }

    private void sendEmailNotification(String emailRecipient) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRecipient);
            message.setSubject("Test Email");
            message.setText("This is a test email sent from Spring Boot.");
            // update to include excel generation...
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUserEmail(String useruid) {
        UserDetails userDetails = userDetailsRepository.findByUserUID(useruid);

        if (userDetails != null) {
            return userDetails.getEmail();
        }

        return null;
    }

    public void restoreEmailScheduling() {
        try{
            List<EmailSchedulingDTO> allEmailToBeScheduled = null;

            if(allEmailToBeScheduled != null){
                System.out.println("========================List is not empty.");
                for(EmailSchedulingDTO currentEmail : allEmailToBeScheduled){

                    String reportFlag = "False";
                    if(currentEmail.isReportFlag()){
                        reportFlag = "true";
                    }

                    System.out.println("Useruid: " + currentEmail.getUserUID() + "|| ReportFlag: " + reportFlag + "|| " +
                            "RepeatStartDate and Time: " + currentEmail.getRepeatStartDate().toString() + "T" + currentEmail.getRepeatStartTime().toString()
                    + "|| RepeatInterval in mins: " + currentEmail.getRepeatInterval());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
