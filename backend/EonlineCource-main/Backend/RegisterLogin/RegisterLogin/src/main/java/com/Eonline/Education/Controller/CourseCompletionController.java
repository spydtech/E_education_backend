package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.CourseCompletionService;
import com.Eonline.Education.modals.CourseCompletion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courseCompletion")
public class CourseCompletionController {
    @Autowired
    CourseCompletionService courseCompletionService;
    @PostMapping("/save")
    public ResponseEntity<?> createCourseCompletion(@RequestBody CourseCompletion courseCompletion){
        return new ResponseEntity<>(courseCompletionService.createCourseCompletion(courseCompletion), HttpStatus.CREATED);
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCourses(){
        return new ResponseEntity<>(courseCompletionService.getAllCourse(),HttpStatus.OK);
    }
}