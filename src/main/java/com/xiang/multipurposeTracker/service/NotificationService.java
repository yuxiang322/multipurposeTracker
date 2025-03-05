package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.NotificationDTO;
import com.xiang.multipurposeTracker.DTO.TemplateDTO;
import com.xiang.multipurposeTracker.entities.Notifications;
import com.xiang.multipurposeTracker.repository.NotificationsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class NotificationService {

    @Autowired
    private NotificationsRepository notificationRepository;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationDTO getNotification(TemplateDTO templateDTO) {
        try {
            NotificationDTO responseNotification = new NotificationDTO();

            Notifications notification = notificationRepository.findNotificationByTemplateID(templateDTO.getTemplateID());

            if (notification != null) {
                responseNotification.setNotificationID(notification.getNotificationID());
                responseNotification.setNotificationFlag(notification.getNotificationFlag());
                responseNotification.setSmsFlag(notification.getSmsFlag());
                responseNotification.setWhatsAppFlag(notification.getWhatsAppFlag());
                responseNotification.setRepeatStartDate(notification.getRepeatStartDate() != null && !notification.getRepeatStartDate().toString().isEmpty() ? notification.getRepeatStartDate().toString() : null);
                responseNotification.setRepeatStartTime(notification.getRepeatStartTime() != null && !notification.getRepeatStartTime().toString().isEmpty() ? notification.getRepeatStartTime().toString() : null);
                responseNotification.setRepeatDays(notification.getRepeatDays());
                responseNotification.setTemplateID(notification.getTemplateID());
                return responseNotification;
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // updating
    @Transactional
    public Boolean updateNotification(NotificationDTO notificationUpdate) {
        AtomicBoolean isUpdated = new AtomicBoolean(false);

        try {
            Optional<Notifications> existingNotification = notificationRepository.findNotificationByNotificationID(notificationUpdate.getNotificationID());

            existingNotification.ifPresent(notification -> {
                logger.info("Notification Exist....");
                notification.setNotificationFlag(notificationUpdate.getNotificationFlag());
                notification.setSmsFlag(notificationUpdate.getSmsFlag());
                notification.setWhatsAppFlag(notificationUpdate.getWhatsAppFlag());
                notification.setRepeatDays(notificationUpdate.getRepeatDays());
                notification.setRepeatStartDate(LocalDate.parse(notificationUpdate.getRepeatStartDate()));
                notification.setRepeatStartTime(LocalTime.parse(notificationUpdate.getRepeatStartTime()));

                notificationRepository.save(notification);
                isUpdated.set(true);
            });

            return isUpdated.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
