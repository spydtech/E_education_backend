//package com.Eonline.Education.Controller;
//
//import com.Eonline.Education.Service.CourseService;
//import com.Eonline.Education.exceptions.CourseException;
//import com.Eonline.Education.modals.Course;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//public class UserCourseController {
//
//    private CourseService courseService;
//
//    public UserCourseController(CourseService courseService) {
//        this.courseService=courseService;
//    }
//
//
//    @GetMapping("/courses")
//    public ResponseEntity<Page<Course>> findCourseByCategoryHandler(@RequestParam String category,
//                                                                        @RequestParam Integer minPrice,
//                                                                      @RequestParam Integer maxPrice, @RequestParam Integer minDiscount, @RequestParam String sort,
//                                                                      @RequestParam String stock, @RequestParam Integer pageNumber, @RequestParam Integer pageSize){
//
//
//        Page<Course> res= courseService.getAllCourse(category, minPrice, maxPrice, minDiscount, sort,stock,pageNumber,pageSize);
//
//        System.out.println("complete courses");
//        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
//
//    }
//
//
//
//    @GetMapping("/courses/id/{courseId}")
//    public ResponseEntity<Course> findProductByIdHandler(@PathVariable Long courseId) throws CourseException {
//
//        Course course=courseService.findCourseById(courseId);
//
//        return new ResponseEntity<Course>(course,HttpStatus.ACCEPTED);
//    }
//
//    @GetMapping("/courses/search")
//    public ResponseEntity<List<Course>> searchProductHandler(@RequestParam String q){
//
//        List<Course> courses=courseService.searchCourse(q);
//
//        return new ResponseEntity<List<Course>>(courses,HttpStatus.OK);
//
//    }
//}