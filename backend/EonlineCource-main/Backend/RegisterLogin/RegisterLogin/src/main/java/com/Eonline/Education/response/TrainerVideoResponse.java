package com.Eonline.Education.response;

import com.Eonline.Education.user.VideoStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Base64;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TrainerVideoResponse {
    private Long id;
    private String title;
    private String groupName;
    private String videoDescription;
    private String videoBase64;
    private String videoFileName;
    private String videoFileType;
    private LocalDate sentDate;
    private VideoStatus status;
    private String author = "E-Education";
    private String trainer;
    private String videoUrl;
    @Lob
    private byte[] video;

    // Convert byte[] video to Base64 String
    public void setVideo(byte[] video) {
        if (video != null) {
            this.videoBase64 = Base64.getEncoder().encodeToString(video);
        }
    }
}