package com.Eonline.Education.modals;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCompletion {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String courseName;
    private LocalDate issueDate;
    private LocalDate ExpiryDate;
    private String userName;
    private String firstName;
    private String LastName;
}
