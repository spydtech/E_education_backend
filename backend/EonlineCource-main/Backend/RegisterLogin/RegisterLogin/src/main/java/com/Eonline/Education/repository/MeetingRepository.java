package com.Eonline.Education.repository;

import com.Eonline.Education.modals.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {




    List<Meeting> findAllByGroupId(Long groupId);

    List<Meeting> findAllByGroupIdAndFromDateLessThanEqualAndToDateGreaterThanEqual(Long id, LocalDate today, LocalDate today1);

    List<Meeting> findAllByGroupIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndToTimeGreaterThan(Long id, LocalDate today, LocalDate today1, LocalTime currentTime);
}
