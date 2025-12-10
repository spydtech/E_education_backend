package com.Eonline.Education.Request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserTraineeCourseRequest {

    private String userEmail;

    private String traineeEmail;

    private String mainCourseName;

    private String subCourseName;
}
