package com.Eonline.Education.Service;

import com.Eonline.Education.modals.CourseCompletion;

import java.util.List;

public interface CourseCompletionService {
    public CourseCompletion createCourseCompletion(CourseCompletion courseCompletion);
    public List<CourseCompletion> getAllCourse();
}