package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.modals.Employee;
import com.Eonline.Education.modals.EmployeeAttendence;
import com.Eonline.Education.repository.EmployeeAttendenceRepo;
import com.Eonline.Education.repository.EmployeeRepository;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.user.AttendanceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class EmployeeAttendanceService {
        @Autowired
         EmployeeRepository empRepo;

        @Autowired
         EmployeeAttendenceRepo empAttendenceRepo;
        @Autowired
         JwtTokenProvider jwtTokenProvider;

        public ApiResponse recordAttendance(String jwt, AttendanceStatus status) {
            String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
            System.out.println(email);
            Employee emp = empRepo.findByEmail(email);
            if (emp != null) {
                LocalDate today = LocalDate.now();
                EmployeeAttendence attendance = empAttendenceRepo.findByEmailAndDate(email, today);
                if (status.equals(AttendanceStatus.CHECKIN)) {
                    if (attendance == null) {
                        attendance = new EmployeeAttendence();
                        attendance.setName(
                                emp.getLastName() != null ? emp.getFirstName() + " " + emp.getLastName()
                                        : emp.getFirstName());
                        attendance.setEmail(emp.getEmail());
                        attendance.setDate(today);
                        attendance.setLogInTime(LocalTime.now());
                        empAttendenceRepo.save(attendance);
                        return new ApiResponse("Check-in successful", true, attendance);
                    } else {
                        return new ApiResponse("You have already checked in today.");
                    }
                } else if (status.equals(AttendanceStatus.CHECKOUT)) {
                    if (attendance != null && attendance.getLogInTime() != null && attendance.getLogOutTime() == null) {
                        attendance.setLogOutTime(LocalTime.now());
                        Duration duration = Duration.between(attendance.getLogInTime(), attendance.getLogOutTime());
                        long hours = duration.toHours();
                        long minutes = duration.toMinutesPart();
                        long seconds = duration.toSecondsPart();
                        String formattedDuration = String.format("%d::%02d::%02d", hours, minutes, seconds);
                        attendance.setDuration(formattedDuration);
                        empAttendenceRepo.save(attendance);
                        return new ApiResponse("Check-out successful", true, attendance);
                    } else if (attendance == null) {
                        return new ApiResponse("You haven't checked in today. Please check-in first.");
                    } else {
                        return new ApiResponse("You have already checked out today.");
                    }
                }
            }
            return new ApiResponse("Trainee with email " + email + " not found.");
        }

}
