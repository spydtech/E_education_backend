//package com.Eonline.Education.Service;
//
//import com.Eonline.Education.Request.CreateCourseRequest;
//import com.Eonline.Education.exceptions.CourseException;
//import com.Eonline.Education.modals.Course;
//import org.springframework.data.domain.Page;
//
//import java.util.List;
//
//public interface CourseService {
//
//    // Create a new course
//    Course createCourse(CreateCourseRequest req);
//
//    // Delete a course by ID
//    String deleteCourse(Long courseId) throws CourseException;
//
//    // Update a course by ID
//    Course updateCourse(Long courseId, Course req) throws CourseException;
//
//    // Get all courses
//    List<Course> getAllCourses();
//
//    // Find a course by ID
//    Course findCourseById(Long id) throws CourseException;
//
//    // Find courses by category
//    List<Course> findCourseByCategory(String category);
//
//    // Search for courses
//    List<Course> searchCourse(String query);
//
//    // Get all courses with pagination and filtering options
//    Page<Course> getAllCourse(String category,
//                              Integer minPrice, Integer maxPrice, Integer minDiscount, String sort,
//                              String stock, Integer pageNumber, Integer pageSize);
//
//    // Get recently added courses
//    List<Course> recentlyAddedCourse();
//}
