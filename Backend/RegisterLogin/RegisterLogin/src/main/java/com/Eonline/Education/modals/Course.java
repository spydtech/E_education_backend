
package com.Eonline.Education.modals;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name="courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
   // @Column(name = "course_price", nullable = false, columnDefinition = "DECIMAL(10, 2) DEFAULT 0.00")
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CourseCategory category;
}