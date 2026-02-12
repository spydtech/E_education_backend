package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.TaskService;
import com.Eonline.Education.modals.TaskUser;
import com.Eonline.Education.repository.TaskRepository;
import com.Eonline.Education.repository.TraineeRepository;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.response.TaskResponse;
import com.Eonline.Education.user.TaskStatus;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    TraineeRepository traineeRepository;
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    //trainer


    @PostMapping(value = "/tasks/upload/by-trainee", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse taskUploadByTrainer(
            @RequestHeader("Authorization") String jwt,
            @RequestParam("file") MultipartFile file,
            @RequestParam("users") List<String> users,
            @RequestParam("assignmentDescription") String assignmentDescription,
            @RequestParam("dueDate") LocalDate dueDate,
            @RequestParam("chatGroup") String chatGroup,
            @RequestParam("assignmentDate") LocalDate assignmentDate) {
        try {
            TaskResponse savedFile = taskService.taskUploadByTrainer(jwt, file, assignmentDescription, dueDate, chatGroup, users, assignmentDate);
            return new ApiResponse("Task submitted successfully.", true, savedFile);
        } catch (IOException e) {
            return new ApiResponse("Could not upload the file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAll/by-trainee")
    public List<TaskResponse> getAll(@RequestHeader("Authorization") String jwt) {
        return taskService.getAll(jwt);
    }
    @GetMapping("/getAll/submission/by-trainee")
    public List<TaskResponse> getAllSubmission(@RequestHeader("Authorization") String jwt) {
        return taskService.getAllSubmission(jwt);
    }

    @GetMapping("/getById/{taskId}")
    public TaskResponse getById(@PathVariable Long taskId) {
        return taskService.getById(taskId);
    }

    @PutMapping("/task/approval/by-trainee")
    public ResponseEntity<ApiResponse> taskApproval(@RequestHeader("Authorization") String jwt, @RequestParam("taskId") Long taskId,
                                                    @RequestParam("taskStatus") TaskStatus taskStatus){
        ApiResponse response = taskService.taskApproval(jwt, taskId, taskStatus);
        return ResponseEntity.ok(response);
    }





    //users
    @GetMapping("/getAllTasks/by-user")

    public List<TaskResponse> getAllTasksByUser(@RequestHeader("Authorization") String jwt) {
        return taskService.getAllTasksByUser(jwt);
    }

    @PostMapping("/task/submitted/by-user")
    public ResponseEntity<ApiResponse> taskSubmission(
            @RequestHeader("Authorization") String jwt,
            @RequestParam("taskId") Long taskId,
            @RequestParam("answer") String answer,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException, java.io.IOException {
        ApiResponse response = taskService.taskSubmission(jwt, taskId, answer, file);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/download/{taskId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long taskId) {
        TaskUser taskEntity = taskService.getFile(taskId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + taskEntity.getTask().getName()+ "\"")
                .header(HttpHeaders.CONTENT_TYPE, taskEntity.getTask().getType())
                .body(taskEntity.getTask().getFile());
    }


    //trainer side dashboard
    @GetMapping("/taskOverview")
    public Map<String,Long> taskOverview(@RequestHeader("Authorization") String jwt) {
        return taskService.taskOverview(jwt);
    }



}