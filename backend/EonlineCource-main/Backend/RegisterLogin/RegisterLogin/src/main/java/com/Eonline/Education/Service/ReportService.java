package com.Eonline.Education.Service;

import com.Eonline.Education.modals.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface ReportService {
    public ResponseEntity<Report> reportSave(Report report,String jwt);

    ResponseEntity<Report> reportUpdate(Long id, Report message);

    String reportDelete(Long id);

    ResponseEntity<List<Report>> report(String jwt);
}
