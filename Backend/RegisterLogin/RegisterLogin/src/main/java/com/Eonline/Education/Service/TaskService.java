package com.Eonline.Education.Service;


import com.Eonline.Education.modals.TaskUser;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.response.TaskResponse;
import com.Eonline.Education.user.TaskStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TaskService {
    //trainer
    TaskResponse taskUploadByTrainer(String jwt, MultipartFile file, String assignmentDescription, LocalDate dueDate,String chatGroup,List<String> users,LocalDate assignmentDate) throws IOException;
    List<TaskResponse> getAll(String jwt);
    public TaskResponse getById(Long taskId);
    ApiResponse taskApproval(String jwt, Long taskId, TaskStatus taskStatus);

    public List<TaskResponse> getAllSubmission(String jwt);
    //users

    List<TaskResponse> getAllTasksByUser(String jwt);

    ApiResponse taskSubmission(String jwt, Long taskId, String answer, MultipartFile file) throws IOException;

    public TaskUser getFile(Long taskId);


    Map<String,Long> taskOverview(String jwt);
}