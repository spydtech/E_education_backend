package com.Eonline.Education.response;

import com.Eonline.Education.user.SubmissionStatus;
import com.Eonline.Education.user.TaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TaskResponse {
    private Long id;
    private String assignmentDescription;
    private LocalDate assignmentDate;
    private LocalDate dueDate;
    private String chatGroup;
    private LocalDate chatGroupStartDate;
    private LocalDate chatGroupEndDate;
    private List<UserResponse> users;
    private List<UserResponse> teamMembers;
    private byte[] file;
    private String trainerName;
    private TaskStatus taskStatus;
    private SubmissionStatus submissionStatus;
    private LocalDate submittedDate;
    private int participants;
    private String answer;
    private byte[] submittingFile;
}
