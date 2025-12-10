package com.Eonline.Education.Service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Async
    public void sendNotification(String email, String message) {
        // Implementation for sending notifications asynchronously
        System.out.println("Sending notification to " + email + ": " + message);
    }
}
