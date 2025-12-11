package com.Eonline.Education.repository;

import com.Eonline.Education.modals.EmployeeAttendence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EmployeeAttendenceRepo extends JpaRepository<EmployeeAttendence,Long> {
    EmployeeAttendence findByEmailAndDate(String email, LocalDate today);
}
