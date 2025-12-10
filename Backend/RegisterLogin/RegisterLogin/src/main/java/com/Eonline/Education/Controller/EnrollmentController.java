package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.EnrollmentService;
import com.Eonline.Education.modals.Enrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public Enrollment enrollUser(@RequestBody Enrollment enrollment) {
        return enrollmentService.enrollUser(enrollment);
    }

    @GetMapping("/user/{userId}")
    public List<Enrollment> getUserEnrollments(@PathVariable Long userId) {
        return enrollmentService.getUserEnrollments(userId);
    }
}
