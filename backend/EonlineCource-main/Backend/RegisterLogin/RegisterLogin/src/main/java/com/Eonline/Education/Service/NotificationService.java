package com.Eonline.Education.Service;

import com.Eonline.Education.modals.Notification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


public interface NotificationService {

    Notification createNotification(String email, String message);
    List<Notification> getUnreadNotifications(String email);
    void markAsRead(Long notificationId);
    public String clearNotifications(String jwt);
}