package com.Eonline.Education.repository;

import com.Eonline.Education.modals.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
//
    @Query("Select r from Rating r where r.course.id=:courseId")
    public List<Review> getAllCourseReview(@Param("courseId") Long productId);
}
