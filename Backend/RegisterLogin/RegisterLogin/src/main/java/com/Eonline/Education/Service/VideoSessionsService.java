package com.Eonline.Education.Service;

import com.Eonline.Education.response.AdminVideoResponse;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.response.TrainerVideoResponse;
import com.Eonline.Education.user.VideoStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface VideoSessionsService {
    ApiResponse videoSend(String jwt, MultipartFile video, String title, String groupName, String videoDescription) throws IOException;

    List<TrainerVideoResponse> getAllVideoSessions(String jwt);

    List<AdminVideoResponse> getAllVideoSessionsForAdmin(String jwt);

    ApiResponse updateStatus(String jwt, Long videoId, VideoStatus videoStatus);
    Map<String, List<AdminVideoResponse>> getPublishedVideosByGroup();
}


