package com.Eonline.Education.repository;

import com.Eonline.Education.modals.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Long> {
//
    @Query("Select r From Rating r where r.course.id=:courseId")
    public List<Rating> getAllcourseRating(@Param("courseId") Long productId);
}
