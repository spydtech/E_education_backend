package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.ReportService;
import com.Eonline.Education.modals.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    ReportService reportService;
    @PostMapping("/createReport")
    public ResponseEntity<Report> reportSave(@RequestBody Report report, @RequestHeader("Authorization") String jwt) {
        return reportService.reportSave(report,jwt);
    }

    @PutMapping("/updateReport/{id}")
    public ResponseEntity<Report> reportUpdate(@PathVariable Long id, @RequestBody Report report) {
        return reportService.reportUpdate(id,report);
    }

    @DeleteMapping("/delete/{id}")
    public String reportDelete(@PathVariable Long id) {
        return reportService.reportDelete(id);
    }
    @GetMapping("/get")
    public ResponseEntity<List<Report>> report(@RequestHeader("Authorization") String jwt) {
        return reportService.report(jwt);
    }

}
