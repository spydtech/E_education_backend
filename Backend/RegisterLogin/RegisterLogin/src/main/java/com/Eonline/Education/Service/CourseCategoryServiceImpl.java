package com.Eonline.Education.Service;

import com.Eonline.Education.modals.CourseCategory;
import com.Eonline.Education.repository.CourseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    @Override
    public List<CourseCategory> getAllCategories() {
        return courseCategoryRepository.findAll();
    }

    @Override
    public CourseCategory getCategoryById(Long id) {
        Optional<CourseCategory> category = courseCategoryRepository.findById(id);
        return category.orElse(null);
    }

    @Override
    public CourseCategory saveCategory(CourseCategory category) {
        return courseCategoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        courseCategoryRepository.deleteById(id);
    }
}
