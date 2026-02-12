package com.Eonline.Education.modals;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user_biodata")
public class BioData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String bio;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String gender;

    private String location;

    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private String website;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
