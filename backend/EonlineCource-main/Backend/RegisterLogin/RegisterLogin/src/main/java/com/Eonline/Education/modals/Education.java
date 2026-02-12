package com.Eonline.Education.modals;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.time.Year;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Education_Details")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_of_institution")
    private String nameOfInstitution;

    @Column(name = "degree")
    private String degree;

    @Column(name = "start_month")
    @Enumerated(EnumType.STRING)
    private Month startMonth;

    @Column(name = "start_year")
    private Year startYear;

    @Column(name = "graduation_month")
    @Enumerated(EnumType.STRING)
    private Month graduationMonth;

    @Column(name = "graduation_year")
    private Year graduationYear;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
