package com.Eonline.Education.Controller;

import com.Eonline.Education.Request.MeetingRequest;
import com.Eonline.Education.Service.MeetingService;
import com.Eonline.Education.modals.Enrollment;
import com.Eonline.Education.modals.Meeting;
import com.Eonline.Education.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {
    @Autowired
    MeetingService meetingService;
    @PostMapping("/save")
    public ApiResponse meetSave(@RequestBody MeetingRequest meeting, @RequestHeader("Authorization") String jwt) {
        return meetingService.meetSave(meeting,jwt);
    }
    @GetMapping("/get/meetings/{groupId}")
    public  ApiResponse getMeetings(@PathVariable Long groupId) {
        return meetingService.getMeetings(groupId);
    }

    @GetMapping("/getAll/user/meetings")
    public ApiResponse getAllUserMeetings(@RequestHeader("Authorization") String jwt) {
        return meetingService.getAllUserMeetings(jwt);
    }
    @GetMapping("/getAll/trainee/meetings")
    public ApiResponse getAllTraineeMeetings(@RequestHeader("Authorization") String jwt) {
        return meetingService.getAllTraineeMeetings(jwt);
    }
    @GetMapping("/getAll/groups/{userId}")
    public List<String> getGroupNamesByUserId(@PathVariable Long userId){
        return meetingService.getGroupNamesByUserId(userId);

    }

    // trainer side dashboard

    @GetMapping("/getAll/upcoming/meetings")
    public ApiResponse getAllUpComingMeetings(@RequestHeader("Authorization") String jwt) {
        return meetingService.getAllUpComingMeetings(jwt);
    }
    // admin side dashboard
    @GetMapping("/getAll")
    public ApiResponse getAll() {
        return meetingService.getAll();
    }

}
