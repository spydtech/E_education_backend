package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.modals.TraineeAttendence;
import com.Eonline.Education.modals.TraineeCredentialGenerator;
import com.Eonline.Education.repository.TraineeRepository;
import com.Eonline.Education.repository.TraineerAttendanceRepo;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.user.AttendanceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TraineeAttendenceService {
    @Autowired
    TraineeRepository trainerRepository;
    @Autowired
    TraineerAttendanceRepo traineerAttendanceRepo;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public ApiResponse recordAttendance(String jwt, AttendanceStatus status) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        TraineeCredentialGenerator trainee = trainerRepository.findByEmail(email);
        if (trainee != null) {
            LocalDate today = LocalDate.now();
            TraineeAttendence attendance = traineerAttendanceRepo.findByEmailAndDate(email, today);
            if (status == AttendanceStatus.CHECKIN) {
                if (attendance == null) {
                    attendance = new TraineeAttendence();
                    attendance.setName(trainee.getLastName() != null ?
                            trainee.getFirstName() + " " + trainee.getLastName() :
                            trainee.getFirstName());
                    attendance.setEmail(trainee.getEmail());
                    attendance.setDate(today);
                    attendance.setLogInTime(LocalTime.now());
                    traineerAttendanceRepo.save(attendance);
                    return new ApiResponse("Check-in successful", true, attendance);
                } else {
                    return new ApiResponse("You have already checked in today.");
                }
            } else if (status == AttendanceStatus.CHECKOUT) {
                if (attendance != null && attendance.getLogInTime() != null && attendance.getLogOutTime() == null) {
                    attendance.setLogOutTime(LocalTime.now());
                    Duration duration = Duration.between(attendance.getLogInTime(), attendance.getLogOutTime());
                    long hours = duration.toHours();
                    long minutes = duration.toMinutesPart();
                    long seconds = duration.toSecondsPart();
                    String formattedDuration = String.format("%d::%02d::%02d", hours, minutes, seconds);
                    attendance.setDuration(formattedDuration);
                    traineerAttendanceRepo.save(attendance);
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

    public ApiResponse getAttendance(String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        TraineeCredentialGenerator trainee = trainerRepository.findByEmail(email);
        if (trainee != null) {
            List<TraineeAttendence> attendance = traineerAttendanceRepo.findAllByEmail(email);
            return new ApiResponse(attendance);
        }
        return new ApiResponse("Trainee with email " + email + " not found.");
    }
}
