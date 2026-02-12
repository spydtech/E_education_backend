package com.Eonline.Education.Service;

import com.Eonline.Education.modals.CourseCategory;

import java.util.List;

public interface CourseCategoryService {
    List<CourseCategory> getAllCategories();
    CourseCategory getCategoryById(Long id);
    CourseCategory saveCategory(CourseCategory category);
    void deleteCategory(Long id);
}
