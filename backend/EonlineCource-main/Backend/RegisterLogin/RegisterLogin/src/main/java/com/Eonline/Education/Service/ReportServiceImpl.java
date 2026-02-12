package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.modals.Report;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.ReportRepository;
import com.Eonline.Education.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<Report> reportSave(Report report, String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        Optional<User> user = userRepository.findByEmail(email);
        Report message = new Report();
        message.setReport(report.getReport());
        message.setReportDate(report.getReportDate());
        message.setEmail(String.valueOf(user.getClass()));
        reportRepository.save(message);
        return ResponseEntity.ok(message);
    }


    @Override
    public ResponseEntity<Report> reportUpdate(Long id, Report message) {
        Optional<Report> report = reportRepository.findById(id);
        if (report.isPresent()) {
            Report report1 = report.get();
            report1.setReport(message.getReport());
            report1.setReportDate(message.getReportDate());
            reportRepository.save(report1);
            return ResponseEntity.ok(report1);

        }
        return null;
    }

    @Override
    public String reportDelete(Long id) {
        Optional<Report> report = reportRepository.findById(id);
        if (report.isPresent()) {
            Report report1 = report.get();
            reportRepository.delete(report1);

        }
        return "deleted Successfully";
    }

    @Override
    public ResponseEntity<List<Report>> report(String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        Optional<User> user = userRepository.findByEmail(email);
        List<Report> report;
        report = reportRepository.findAllByEmail(String.valueOf(user.getClass()));
        if (!report.isEmpty()) {
            return ResponseEntity.ok(report);
        }
        return null;
    }

}
