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

    @Column(name = "event_date")
    private String date;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String adminEmail;
    private String meetingLink;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Note: If TraineeCredentialGenerator is not needed, you can remove this field
    // or fix the circular reference
    @OneToOne
    @JoinColumn(name = "trainee_id", referencedColumnName = "id")
    private TraineeCredentialGenerator traineeCredentialGenerator;

    // Helper method to get user email
    public String getUserEmail() {
        return user != null ? user.getEmail() : null;
    }

    public CalendarEvent orElseThrow(Object o) {
        return null;
    }
}