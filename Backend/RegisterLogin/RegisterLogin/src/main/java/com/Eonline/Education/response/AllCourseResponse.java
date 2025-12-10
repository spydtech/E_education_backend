package com.Eonline.Education.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class AllCourseResponse {

    private List<CourseDetails> courses;

    @Data
    @RequiredArgsConstructor
    public static class CourseDetails {
        private String courseName;
        private List<UserDetails> users;
        private List<TraineeDetails> trainees;

        public CourseDetails(String courseName, List<UserDetails> users, List<TraineeDetails> trainees) {
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class UserDetails {
        private Long userId;
        private String userName;
        private String userEmail;

        public UserDetails(Long id, String s, String email) {
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class TraineeDetails {
        private Long traineeId;
        private String traineeName;
        private String traineeEmail;

        public TraineeDetails(long id, String s, String email) {
        }
    }

    public AllCourseResponse(List<CourseDetails> courses) {
        this.courses = courses;
    }

}