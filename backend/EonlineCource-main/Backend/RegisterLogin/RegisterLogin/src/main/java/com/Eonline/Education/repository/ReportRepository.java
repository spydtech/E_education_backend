package com.Eonline.Education.repository;


import com.Eonline.Education.modals.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {
    List<Report> findAllByEmail(String email);
}
