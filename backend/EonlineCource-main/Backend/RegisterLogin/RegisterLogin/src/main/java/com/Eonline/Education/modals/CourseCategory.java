package com.Eonline.Education.modals;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class CourseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;
}
