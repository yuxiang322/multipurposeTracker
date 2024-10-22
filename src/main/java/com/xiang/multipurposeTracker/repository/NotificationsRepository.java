package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationsRepository extends JpaRepository<Notifications, String> {

    Notifications findNotificationByTemplateID(int templateID);

    Optional<Notifications> findNotificationByNotificationID(int notificationID);
}
