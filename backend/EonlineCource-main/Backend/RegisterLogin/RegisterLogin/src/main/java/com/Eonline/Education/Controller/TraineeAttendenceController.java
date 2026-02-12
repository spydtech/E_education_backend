package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.TraineeAttendenceService;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.user.AttendanceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainee/attendance")
public class TraineeAttendenceController {
    @Autowired
    private TraineeAttendenceService service;

    @PostMapping("/checkIn/checkOut")
    public ApiResponse recordAttendance(@RequestHeader("Authorization") String jwt, @RequestParam AttendanceStatus status) {
        return service.recordAttendance(jwt, status);
    }
    @GetMapping("/getAll")
    public ApiResponse getAttendance(@RequestHeader("Authorization") String jwt) {
        return service.getAttendance(jwt);
    }

}
