package com.Eonline.Education.response;

import com.Eonline.Education.user.SubmissionStatus;
import com.Eonline.Education.user.TaskStatus;
import com.Eonline.Education.user.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResponse {
    private String email;
    private String fullName;
    private String userId;
    private Date createdAt;
    private UserStatus userstatus;
    private TaskStatus taskStatus;
    private SubmissionStatus submissionStatus;
    private LocalDate submittedDate;
    private byte[] submittedFile;
    private Long taskUserId;


    public UserResponse(String email, String fullName, String userId, Date createdAt,UserStatus userstatus) {
        this.email = email;
        this.fullName = fullName;
        this.userId = userId;
        this.createdAt = createdAt;
        this.userstatus=userstatus;
    }


    public UserResponse(String email, String fullName,UserStatus userstatus, String id, Date createdAt, TaskStatus taskStatus, SubmissionStatus submissionStatus,byte[] submittedFile,LocalDate submittedDate) {
        this.email=email;
        this.fullName=fullName;
        this.userId=id;
        this.createdAt=createdAt;
        this.taskStatus=taskStatus;
        this.submissionStatus=submissionStatus;
        this.submittedFile=submittedFile;
        this.submittedDate=submittedDate;
        this.userstatus=userstatus;
    }
    public UserResponse(String email, String fullName,UserStatus userstatus, String id, Date createdAt, TaskStatus taskStatus, SubmissionStatus submissionStatus,byte[] submittedFile,LocalDate submittedDate, Long taskUserID) {
        this.email=email;
        this.fullName=fullName;
        this.userstatus=userstatus;
        this.userId=id;
        this.createdAt=createdAt;
        this.taskStatus=taskStatus;
        this.submissionStatus=submissionStatus;
        this.submittedFile=submittedFile;
        this.submittedDate=submittedDate;
        this.taskUserId=taskUserID;
    }
}
