package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.TaskDetailsService;
import com.Eonline.Education.modals.TaskDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskdetails")
public class TaskDetailsController {
    @Autowired
    TaskDetailsService taskDetailsService;

    @PostMapping("/create")
    public ResponseEntity<TaskDetails> creatingTaskDetails(@RequestBody TaskDetails taskDetails){
        return new ResponseEntity<>(taskDetailsService.createTaskDetails(taskDetails), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TaskDetails> getTaskDetailsById(@PathVariable Long id){
        return new ResponseEntity<>(taskDetailsService.getTaskDetailsById(id),HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDetails>> getAllTaskDetails(){
        return new ResponseEntity<>(taskDetailsService.getAllTasks(),HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskDetails> update(@PathVariable Long id,@RequestBody TaskDetails taskDetails){
        return new ResponseEntity<>(taskDetailsService.updateTaskDetails(id,taskDetails),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTasksDetails(@PathVariable Long id){
        return new ResponseEntity<>(taskDetailsService.deleteTaskDetails(id),HttpStatus.OK);
    }
}
