package com.Eonline.Education.repository;

import com.Eonline.Education.modals.TraineeAttendence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TraineerAttendanceRepo extends JpaRepository<TraineeAttendence,Long> {
    TraineeAttendence findByEmailAndDate(String email, LocalDate today);

    List<TraineeAttendence> findAllByEmail(String email);
}
