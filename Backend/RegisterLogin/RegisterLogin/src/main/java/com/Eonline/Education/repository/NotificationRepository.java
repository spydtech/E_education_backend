package com.Eonline.Education.repository;

import com.Eonline.Education.modals.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByUserIdAndReadFalse(String email);

    List<Notification> findByEmailAndReadFalse(String email);

    List<Notification> findByEmailAndReadFalseOrderByTimestampDesc(String email);

    List<Notification> findAllByEmail(String email);
}