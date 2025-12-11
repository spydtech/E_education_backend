package com.Eonline.Education.modals;

import com.Eonline.Education.user.VideoStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class VideoSessions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String groupName;
    private String videoDescription;
    @Lob
    @JsonIgnore
    @Column(columnDefinition = "LONGBLOB")
    private byte[] video;
    private String videoFileName;
    private String videoFileType;
    private LocalDate sentDate;
    @Enumerated(EnumType.STRING)
    private VideoStatus status;
    @Column(name = "adminRespondDate", nullable = true)
    private LocalDate adminRespondDate;
    private String author = "E-Education"; // Fixed author name
    private String trainer;
    @Column(name = "duration", nullable = true)
    private Integer duration;

}