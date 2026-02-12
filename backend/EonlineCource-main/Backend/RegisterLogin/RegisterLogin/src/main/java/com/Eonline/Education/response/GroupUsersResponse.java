package com.Eonline.Education.response;

import com.Eonline.Education.user.UserStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@JsonPropertyOrder({

        "chatGroupName",
        "courseStartDate",
        "courseEndDate",
        "userID",
        "userName",
        "userEmail",
        "status",
        "courses",
        "traineeName",
        "traineeEmail"

})
public class GroupUsersResponse {
    private String userID;
    private String chatGroupName;
    private LocalDate CourseStartDate;
    private LocalDate CourseEndDate;
    private String userName;
    private String userEmail;
    private UserStatus status;
    private List<Map<String, Object>> courses;
    private String traineeName;
    private String traineeEmail;

    public GroupUsersResponse(String userID, String chatGroupName, LocalDate courseStartDate, LocalDate courseEndDate, String userName, String userEmail, UserStatus status, List<Map<String, Object>> courses, String traineeName, String traineeEmail) {
        this.userID = userID;
        this.chatGroupName = chatGroupName;
        this.CourseStartDate = courseStartDate;
        this.CourseEndDate = courseEndDate;
        this.userName = userName;
        this.userEmail = userEmail;
        this.status = status;
        this.courses = courses;
        this.traineeName = traineeName;
        this.traineeEmail = traineeEmail;
    }

    public GroupUsersResponse(String chatGroupName, String userName, String userEmail, UserStatus status, List<Map<String, Object>> courses) {
        this.chatGroupName = chatGroupName;
        this.userName = userName;
        this.userEmail = userEmail;
        this.status = status;
        this.courses = courses;

    }

    public GroupUsersResponse(String userID,String chatGroupName, String userName, String userEmail, UserStatus status,
                             List<Map<String, Object>> courses,
                              String traineeName, String traineeEmail) {
        this.userID = userID;
        this.chatGroupName = chatGroupName;
        this.userName = userName;
        this.userEmail = userEmail;
        this.status = status;
        this.courses = courses;
        this.traineeName=traineeName;
        this.traineeEmail=traineeEmail;
    }
}
