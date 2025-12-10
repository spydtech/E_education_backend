package com.Eonline.Education.modals;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "calendar_event")
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String Date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String adminEmail;
    private String meetingLink;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trainee_email", referencedColumnName = "email")
    private TraineeCredentialGenerator traineeCredentialGenerator;


}
