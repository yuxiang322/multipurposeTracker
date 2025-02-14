package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {

    Notifications findNotificationByTemplateID(int templateID);

    Optional<Notifications> findNotificationByNotificationID(int notificationID);
}
