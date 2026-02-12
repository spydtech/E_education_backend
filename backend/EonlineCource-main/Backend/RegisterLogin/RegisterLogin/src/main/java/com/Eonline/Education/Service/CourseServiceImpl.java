//package com.Eonline.Education.Service;
//
//import com.Eonline.Education.Request.CreateCourseRequest;
//import com.Eonline.Education.exceptions.CourseException;
//import com.Eonline.Education.modals.Course;
//import com.Eonline.Education.repository.CourseRepository;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class CourseServiceImplementation implements CourseService {
//    private final CourseRepository courseRepository;
//
//    public CourseServiceImplementation(CourseRepository courseRepository) {
//        this.courseRepository = courseRepository;
//    }
//
//    @Override
//    public Course createCourse(CreateCourseRequest req) {
//        // Validate request data
//        // Example: if (req.getTitle() == null || req.getTitle().isEmpty()) { throw new IllegalArgumentException("Title is required"); }
//
//        Course course = new Course();
//        course.setTitle(req.getTitle());
//        course.setDescription(req.getDescription());
//        course.setDiscountedPrice(req.getDiscountedPrice());
//        course.setDiscountPercent(req.getDiscountPercent());
//        course.setCoursePrice(req.getPrice());
//        course.setImageUrl(req.getImageUrl());
//        course.setCreatedAt(LocalDateTime.now());
//
//        return courseRepository.save(course);
//    }
//
//    @Override
//    public String deleteCourse(Long courseId) throws CourseException {
//        Course course = findCourseById(courseId);
//        courseRepository.delete(course);
//        return "Course deleted successfully";
//    }
//
//    @Override
//    public Course updateCourse(Long courseId, Course req) throws CourseException {
//        Course course = findCourseById(courseId);
//        if (req.getDescription() != null) {
//            course.setDescription(req.getDescription());
//        }
//        return courseRepository.save(course);
//    }
//
//    @Override
//    public List<Course> getAllCourses() {
//        return courseRepository.findAll();
//    }
//
//    @Override
//    public Course findCourseById(Long id) throws CourseException {
//        Optional<Course> opt = courseRepository.findById(id);
//        return opt.orElseThrow(() -> new CourseException("Course not found with ID: " + id));
//    }
//
//    @Override
//    public List<Course> findCourseByCategory(String category) {
//        // Implement category-based filtering if needed
//        return List.of();
//    }
//
//    @Override
//    public List<Course> searchCourse(String query) {
//        return courseRepository.searchProduct(query);
//    }
//
//    @Override
//    public Page<Course> getAllCourse(String category, Integer minPrice, Integer maxPrice, Integer minDiscount,
//                                     String sort, String stock, Integer pageNumber, Integer pageSize) {
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        return (Page<Course>) courseRepository.filterCourses(minPrice, maxPrice, minDiscount, sort, pageable);
//    }
//
//    @Override
//    public List<Course> recentlyAddedCourse() {
//        return courseRepository.findTop10ByOrderByCreatedAtDesc();
//    }
//}
