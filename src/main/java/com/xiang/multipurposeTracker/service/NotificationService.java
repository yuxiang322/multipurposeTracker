package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.repository.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationsRepository notificationRepository;

}
