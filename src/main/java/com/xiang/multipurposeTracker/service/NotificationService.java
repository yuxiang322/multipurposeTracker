package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.NotificationDTO;
import com.xiang.multipurposeTracker.entities.Notifications;
import com.xiang.multipurposeTracker.repository.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class NotificationService {

    @Autowired
    private NotificationsRepository notificationRepository;

    public NotificationDTO findByTemplateID(int templateID) {

        Notifications notification = notificationRepository.findNotificationByTemplateID(templateID);

        NotificationDTO notificationDTO = new NotificationDTO();
        if(notification != null){
            notificationDTO.setNotificationID(notification.getNotificationID());
            notificationDTO.setTemplateID(notification.getTemplateID());
            notificationDTO.setUserUID(notification.getUserUID());
            notificationDTO.setNotificationFlag(notification.getNotificationFlag());
            notificationDTO.setEmailFlag(notification.getEmailFlag());
            notificationDTO.setSmsFlag(notification.getSmsFlag());
            notificationDTO.setWhatsAppFlag(notification.getWhatsAppFlag());
        }

        return notificationDTO;
    }

    public Boolean updateNotification(NotificationDTO notificationUpdate){
        Optional<Notifications> existingNotificationOpt = notificationRepository.findNotificationByNotificationID(notificationUpdate.getNotificationID());

        // Check if the notification exists
        if (existingNotificationOpt.isPresent()) {
            Notifications existingNotification = existingNotificationOpt.get();

            // Update fields if the incoming data is not null
            if (notificationUpdate.getNotificationFlag() != null) {
                existingNotification.setNotificationFlag(notificationUpdate.getNotificationFlag());
            }
            if (notificationUpdate.getSmsFlag() != null) {
                existingNotification.setSmsFlag(notificationUpdate.getSmsFlag());
            }
            if (notificationUpdate.getWhatsAppFlag() != null) {
                existingNotification.setWhatsAppFlag(notificationUpdate.getWhatsAppFlag());
            }
            if (notificationUpdate.getEmailFlag() != null) {
                existingNotification.setEmailFlag(notificationUpdate.getEmailFlag());
            }

            notificationRepository.save(existingNotification);
            return true;
        }
        return false;

    }
}
