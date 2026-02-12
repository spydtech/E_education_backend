package com.Eonline.Education.response;

import com.Eonline.Education.modals.VideoSessions;
import com.Eonline.Education.user.VideoStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AdminVideoResponse {
    private Long id;
    private String title;
    private String groupName;
    private String videoDescription;
    private byte[] video;
    private VideoStatus status;
    private LocalDate adminRespondDate;
    private String author = "E-Education"; // Fixed author name
    private String videoUrl;
    private String videoBase64;
    private String videoFileType;

}