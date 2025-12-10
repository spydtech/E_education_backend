package com.Eonline.Education.Service;


import com.Eonline.Education.modals.CourseCompletion;
import com.Eonline.Education.repository.CourseCompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CourseCompletionServiceImpl implements CourseCompletionService{
    @Autowired
    CourseCompletionRepository courseCompletionRepository;
    public CourseCompletion createCourseCompletion(CourseCompletion courseCompletion){
       // courseCompletion.setIssueDate(LocalDate.now());
        return courseCompletionRepository.save(courseCompletion);
    }

    @Override
    public List<CourseCompletion> getAllCourse() {
        return courseCompletionRepository.findAll();
    }

}
