package com.Eonline.Education.repository;

import com.Eonline.Education.modals.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Custom query to filter courses based on price, discount, and sorting criteria
//    @Query("SELECT c FROM Course c WHERE " +
//            "((:minPrice IS NULL AND :maxPrice IS NULL) OR (c.discountedPrice BETWEEN :minPrice AND :maxPrice)) AND " +
//            "(:minDiscount IS NULL OR c.discountPercent >= :minDiscount) " +
//            "ORDER BY CASE WHEN :sort = 'price_low' THEN c.discountedPrice END ASC, " +
//            "CASE WHEN :sort = 'price_high' THEN c.discountedPrice END DESC, " +
//            "c.createdAt DESC")
//    List<Course> filterCourses(@Param("minPrice") Integer minPrice,
//                               @Param("maxPrice") Integer maxPrice,
//                               @Param("minDiscount") Integer minDiscount,
//                               @Param("sort") String sort, Pageable pageable);
//
//    // Custom query to fetch top 10 courses by creation date
//    List<Course> findTop10ByOrderByCreatedAtDesc();
//
//    // Custom query to search for courses based on a query string in title or description
//    @Query("SELECT c FROM Course c WHERE c.title LIKE %:query% OR c.description LIKE %:query%")
//    List<Course> searchProduct(@Param("query") String query);
}
