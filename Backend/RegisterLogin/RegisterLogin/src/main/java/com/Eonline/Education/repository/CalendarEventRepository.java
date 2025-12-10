package com.Eonline.Education.repository;


import com.Eonline.Education.modals.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    boolean existsByUserId(long userAccID);

    CalendarEvent findByUserId(long userAccID);

    void deleteByUserId(long userAccID);

    boolean existsByAdminEmail(String email);

    CalendarEvent findByAdminEmail(String email);
}
