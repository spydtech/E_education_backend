//package com.Eonline.Education.Controller;
//
//import com.Eonline.Education.Request.CreateCourseRequest;
//import com.Eonline.Education.Service.CourseService;
//import com.Eonline.Education.exceptions.CourseException;
//import com.Eonline.Education.modals.Course;
//import com.Eonline.Education.response.ApiResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/courses")
//public class AdminCourseController {
//
//    private final CourseService courseService;
//
//    public AdminCourseController(CourseService courseService) {
//        this.courseService = courseService;
//    }
//
//    @PostMapping("/addCourse")
//    public ResponseEntity<Course> createCourseHandler(@RequestBody CreateCourseRequest req) throws CourseException {
//        Course createdCourse = courseService.createCourse(req);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
//    }
//
//    @DeleteMapping("/{courseId}/delete")
//    public ResponseEntity<ApiResponse> deleteCourseHandler(@PathVariable Long courseId) throws CourseException {
//        String message = courseService.deleteCourse(courseId);
//        ApiResponse response = new ApiResponse(message, true);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<Course>> getAllCourses() {
//        List<Course> courses = courseService.getAllCourses();
//        return ResponseEntity.status(HttpStatus.OK).body(courses);
//    }
//
//    @GetMapping("/recent")
//    public ResponseEntity<List<Course>> getRecentlyAddedCourses() {
//        List<Course> courses = courseService.recentlyAddedCourse();
//        return ResponseEntity.status(HttpStatus.OK).body(courses);
//    }
//
//    @PutMapping("/{courseId}/update")
//    public ResponseEntity<Course> updateCourseHandler(@RequestBody Course req, @PathVariable Long courseId) throws CourseException {
//        Course updatedCourse = courseService.updateCourse(courseId, req);
//        return ResponseEntity.status(HttpStatus.OK).body(updatedCourse);
//    }
//
//    @PostMapping("/creates")
//    public ResponseEntity<ApiResponse> createMultipleCourses(@RequestBody CreateCourseRequest[] reqs) throws CourseException {
//        for (CreateCourseRequest course : reqs) {
//            courseService.createCourse(course);
//        }
//        ApiResponse response = new ApiResponse("Courses created successfully", true);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//}
