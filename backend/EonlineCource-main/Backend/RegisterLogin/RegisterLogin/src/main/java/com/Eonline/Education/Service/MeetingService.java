package com.Eonline.Education.Service;

import com.Eonline.Education.Request.MeetingRequest;
import com.Eonline.Education.modals.Enrollment;
import com.Eonline.Education.modals.Meeting;
import com.Eonline.Education.response.ApiResponse;

import java.util.List;

public interface MeetingService {
    ApiResponse meetSave(MeetingRequest meeting,String jwt);

    ApiResponse getMeetings(Long group);

    ApiResponse getAll();

    ApiResponse getAllUserMeetings(String jwt);

    List<String> getGroupNamesByUserId(Long userId);

    ApiResponse getAllTraineeMeetings(String jwt);

    ApiResponse getAllUpComingMeetings(String jwt);
}
