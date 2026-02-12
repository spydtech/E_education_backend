package com.Eonline.Education.Request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class CalendarEventRequest {

    private String title;
    private String Date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String userEmail;
    private String traineeEmail;
    private String meetingLink;

}
