package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.EmployeeAttendanceService;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.user.AttendanceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emp/attendance")
public class EmployeeAttendenceController {
    @Autowired
    private EmployeeAttendanceService service;

    @PostMapping("/checkIn/checkOut")
    public ApiResponse recordAttendance(@RequestHeader("Authorization") String jwt, @RequestParam AttendanceStatus status) {
        return service.recordAttendance(jwt, status);
    }
}
