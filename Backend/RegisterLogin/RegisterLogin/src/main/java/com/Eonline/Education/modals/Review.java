package com.Eonline.Education.modals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String review;

    @ManyToOne
    @JoinColumn(name="course_id")
    @JsonIgnore
    private Course course;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private LocalDateTime createdAt;
}
