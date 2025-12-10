package com.Eonline.Education.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CourseResponse {

    private String courseName;

    private Long traineeId;

    private String traineeName;

    private String traineeEmail;

    private Long userId;

    private String userName;

    private String userEmail;

    public CourseResponse(String courseName, Long traineeId, String traineeName, String traineeEmail, Long userId, String userName, String userEmail) {
        this.courseName = courseName;
        this.traineeId = traineeId;
        this.traineeName = traineeName;
        this.traineeEmail = traineeEmail;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "CourseResponse{" +
                "courseName='" + courseName + '\'' +
                ", traineeId=" + traineeId +
                ", traineeName='" + traineeName + '\'' +
                ", traineeEmail='" + traineeEmail + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
