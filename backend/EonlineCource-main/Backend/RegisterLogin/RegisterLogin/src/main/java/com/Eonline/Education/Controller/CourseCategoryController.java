    package com.Eonline.Education.Controller;
    
    import com.Eonline.Education.Service.CourseCategoryService;
    import com.Eonline.Education.modals.CourseCategory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    
    import java.util.List;
    
    @RestController
    @RequestMapping("/categories")
    public class CourseCategoryController {
    
        @Autowired
        private CourseCategoryService courseCategoryService;
    
        // Get all categories
        @GetMapping
        public ResponseEntity<List<CourseCategory>> getAllCategories() {
            List<CourseCategory> categories = courseCategoryService.getAllCategories();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
    
        // Get category by ID
        @GetMapping("/{id}")
        public ResponseEntity<CourseCategory> getCategoryById(@PathVariable Long id) {
            CourseCategory category = courseCategoryService.getCategoryById(id);
            if (category != null) {
                return new ResponseEntity<>(category, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    
        // Create a new category
        @PostMapping
        public ResponseEntity<CourseCategory> createCategory(@RequestBody CourseCategory category) {
            CourseCategory savedCategory = courseCategoryService.saveCategory(category);
            return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
        }
    
        // Update an existing category
        @PutMapping("/{id}")
        public ResponseEntity<CourseCategory> updateCategory(@PathVariable Long id, @RequestBody CourseCategory category) {
            category.setId(id);
            CourseCategory updatedCategory = courseCategoryService.saveCategory(category);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        }
    
        // Delete a category
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
            courseCategoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
