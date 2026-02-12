package com.Eonline.Education.Controller;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.Service.VideoSessionsService;

import com.Eonline.Education.repository.TraineeRepository;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.repository.VideoSessionsRepository;
import com.Eonline.Education.response.AdminVideoResponse;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.response.TrainerVideoResponse;
import com.Eonline.Education.user.VideoStatus;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/video")
@Validated
public class VideoSessionsController {
    private static final Logger log = LoggerFactory.getLogger(VideoSessionsController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    VideoSessionsService videoSessionsService;
    @Autowired
    private VideoSessionsRepository videoSessionsRepository;

    @Autowired
    TraineeRepository traineeRepository;


    @PostMapping("/send/video")
    public ApiResponse videoSend(@RequestHeader("Authorization") String jwt, @RequestParam("video") MultipartFile video,
                                 @RequestParam("title") String title,@RequestParam("groupName") String groupName,
                                 @RequestParam("videoDescription") String videoDescription) throws IOException {
        return videoSessionsService.videoSend(jwt, video,title,groupName,videoDescription);
    }

    // getAll video sessions trainer side
    @GetMapping("/getAll/VideoSessions/trainer")
    public List<TrainerVideoResponse> getAllVideoSessions(@RequestHeader("Authorization") String jwt){
        return videoSessionsService.getAllVideoSessions(jwt);
    }

    //getAll video sessions admin side
    @GetMapping("/getAll/VideoSessions/admin")
    public List<AdminVideoResponse> getAllVideoSessionsForAdmin(@RequestHeader("Authorization") String jwt){
        return videoSessionsService.getAllVideoSessionsForAdmin(jwt);
    }

    //update status (published/deleted)
    @PutMapping("/update/status")
    public ApiResponse updateStatus(@RequestHeader("Authorization") String jwt, @RequestParam Long videoId, @RequestParam VideoStatus videoStatus){
        return videoSessionsService.updateStatus(jwt,videoId,videoStatus);
    }

    @GetMapping("/published/group")
    public ResponseEntity<Map<String, List<AdminVideoResponse>>> getPublishedVideosByGroup() {
        Map<String, List<AdminVideoResponse>> groupedVideos = videoSessionsService.getPublishedVideosByGroup();
        return ResponseEntity.ok(groupedVideos);
    }







}