package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.TaskService;
import com.Eonline.Education.modals.Task;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/upload/{Email}/{taskName}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String Email, @PathVariable String taskName) {
        try {
            Optional<Task> savedFile = taskService.saveFile(file,Email,taskName);
            return new ResponseEntity<>("File uploaded successfully: " + savedFile.get().getName(), HttpStatus.OK);
        } catch (IOException | java.io.IOException e) {
            return new ResponseEntity<>("Could not upload the file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/download/{Email}/{taskName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String Email,@PathVariable String taskname) {
        Task taskEntity = taskService.getFile(Email,taskname);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + taskEntity.getName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, taskEntity.getType())
                .body(taskEntity.getData());
    }

}