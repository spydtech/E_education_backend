package com.Eonline.Education.modals;

import com.Eonline.Education.user.SubmissionStatus;
import com.Eonline.Education.user.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Data
public class TaskUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus submissionStatus;
    private LocalDate submittedDate;
    private String answer;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] submittedFile;


}
