package com.Eonline.Education.Service;

import com.Eonline.Education.exceptions.NotFoundException;
import com.Eonline.Education.modals.Employee;
import com.Eonline.Education.modals.Notification;
import com.Eonline.Education.modals.TraineeCredentialGenerator;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.EmployeeRepository;
import com.Eonline.Education.repository.NotificationRepository;
import com.Eonline.Education.repository.TraineeRepository;
import com.Eonline.Education.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TraineeRepository traineeRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Notification createNotification(String email, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        TraineeCredentialGenerator trainee=traineeRepository.findByEmail(email);
        Employee employee=employeeRepository.findByEmail(email);
        if (user != null) {
            notification.setUserId(user.getId());
            notification.setEmail(email);
        } else if (trainee != null) {
            notification.setUserId(trainee.getId());
            notification.setEmail(email);
        } else if (employee != null) {
            notification.setUserId(employee.getId());
            notification.setEmail(email);
        }
        notification.setRead(false);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUnreadNotifications(String  email) {
        return notificationRepository.findByEmailAndReadFalseOrderByTimestampDesc(email);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = (Notification) notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public String clearNotifications(String email){
        List<Notification> notifications=notificationRepository.findAllByEmail(email);
        notificationRepository.deleteAll(notifications);
        return "clear all notifications successfully";
    }
}