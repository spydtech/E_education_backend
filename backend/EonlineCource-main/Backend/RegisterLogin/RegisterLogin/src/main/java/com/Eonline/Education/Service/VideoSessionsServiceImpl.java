package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.exceptions.AuthenticationBasedException;
import com.Eonline.Education.modals.ChatGroup;
import com.Eonline.Education.modals.TraineeCredentialGenerator;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.modals.VideoSessions;
import com.Eonline.Education.repository.ChatGroupRepository;
import com.Eonline.Education.repository.TraineeRepository;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.repository.VideoSessionsRepository;
import com.Eonline.Education.response.AdminVideoResponse;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.response.TrainerVideoResponse;
import com.Eonline.Education.user.VideoStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoSessionsServiceImpl implements VideoSessionsService {
    private static final Logger log = LoggerFactory.getLogger(VideoSessionsServiceImpl.class);

    @Autowired
    ChatGroupRepository chatGroupRepository;

    @Autowired
    VideoSessionsRepository videoSessionsRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TraineeRepository traineeRepository;

    @Override
    public ApiResponse videoSend(String jwt, MultipartFile video, String title, String groupName, String videoDescription) {
        try {
            String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
            TraineeCredentialGenerator trainee = traineeRepository.findByEmail(email);

            if (trainee == null) {
                return new ApiResponse(" Unauthorized. Only trainees can upload videos.", false, null);
            }

            if (video == null || video.isEmpty()) {
                return new ApiResponse(" Video file cannot be empty.", false, null);
            }

            // Validate file type
            String mediaType = video.getContentType();
            if (mediaType == null) {
                return new ApiResponse(" Invalid file type: Content-Type is null.", false, null);
            }

            Set<String> allowedTypes = Set.of("video/mp4", "video/x-msvideo", "video/x-matroska", "video/webm", "video/quicktime");
            if (!allowedTypes.contains(mediaType)) {
                return new ApiResponse(" Invalid file type. Allowed formats: MP4, AVI, MKV, WebM, MOV.", false, null);
            }

            // Validate Chat Group
            Optional<ChatGroup> chatGroupOptional = chatGroupRepository.findByName(groupName);
            if (chatGroupOptional.isEmpty()) {
                return new ApiResponse(" Group not found with name: " + groupName, false, null);
            }
            ChatGroup chatGroup = chatGroupOptional.get();

            // Store Video as BLOB
            VideoSessions videoSessions = new VideoSessions();
            videoSessions.setTitle(title);
            videoSessions.setGroupName(chatGroup.getName());
            videoSessions.setVideoDescription(videoDescription);

            videoSessions.setVideo(video.getBytes());  // Store Video as BLOB
            videoSessions.setVideoFileName(video.getOriginalFilename());
            videoSessions.setVideoFileType(mediaType);

            videoSessions.setStatus(VideoStatus.VERIFYING);
            videoSessions.setSentDate(LocalDate.now());
            videoSessions.setTrainer(trainee.getEmail());
            videoSessions.setAuthor("E-Education");  // Fixed author name

            // Set a default duration
            videoSessions.setDuration(0);  // Default duration if not provided

            videoSessionsRepository.save(videoSessions);
            return new ApiResponse(" Video saved successfully in database.", true, videoSessions);

        } catch (IOException e) {
            return new ApiResponse(" Error processing video file.", false, null);
        } catch (Exception e) {
            return new ApiResponse(" An unexpected error occurred: " + e.getMessage(), false, null);
        }
    }

    @Override
    public List<TrainerVideoResponse> getAllVideoSessions(String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        TraineeCredentialGenerator trainee = traineeRepository.findByEmail(email);

        if (trainee == null) {
            throw new AuthenticationBasedException("Unauthorized");
        }

        List<VideoSessions> videoSessionsList = videoSessionsRepository.findAllByTrainer(trainee.getEmail());

        // Debugging log
        System.out.println("Total videos found: " + videoSessionsList.size());

        return videoSessionsList.stream().map(video -> {
            TrainerVideoResponse response = new TrainerVideoResponse();
            response.setId(video.getId());
            response.setTitle(video.getTitle());
            response.setGroupName(video.getGroupName());
            response.setVideoDescription(video.getVideoDescription());
            response.setVideoFileName(video.getVideoFileName());
            response.setVideoFileType(video.getVideoFileType());
            response.setSentDate(video.getSentDate());
            response.setStatus(video.getStatus());
            response.setAuthor(video.getAuthor());
            response.setTrainer(video.getTrainer());

            // Convert video bytes to Base64 for debugging
            if (video.getVideo() != null) {
                response.setVideoBase64(Base64.getEncoder().encodeToString(video.getVideo()));
            }

            return response;
        }).collect(Collectors.toList());
    }

    private TrainerVideoResponse convertToTrainerVideoResponse(VideoSessions videoSession) {
        TrainerVideoResponse response = new TrainerVideoResponse();
        response.setId(videoSession.getId());
        response.setTitle(videoSession.getTitle());
        response.setGroupName(videoSession.getGroupName());
        response.setVideoDescription(videoSession.getVideoDescription());
        response.setSentDate(videoSession.getSentDate());
        response.setStatus(videoSession.getStatus());
        response.setAuthor(videoSession.getAuthor()); // Dynamically set author
        return response;
    }

    @Override
    public List<AdminVideoResponse> getAllVideoSessionsForAdmin(String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);

        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationBasedException("User not found with email: " + email));

        if (!"ADMIN".equals(user.getRole())) {
            throw new AuthenticationBasedException("Unauthorized");
        }

        List<VideoSessions> videoSessionsList = videoSessionsRepository.findAll();

        return videoSessionsList.stream().map(video -> {
            AdminVideoResponse response = new AdminVideoResponse();
            response.setId(video.getId());
            response.setTitle(video.getTitle());
            response.setGroupName(video.getGroupName());
            response.setVideoDescription(video.getVideoDescription());
            response.setStatus(video.getStatus());
            response.setAdminRespondDate(video.getAdminRespondDate());
            response.setAuthor(video.getAuthor());

            // Convert video bytes to Base64 (Same as Trainer)
            if (video.getVideo() != null) {
                response.setVideoBase64(Base64.getEncoder().encodeToString(video.getVideo()));
                response.setVideoFileType(video.getVideoFileType()); // Ensure frontend knows the type
            }

            return response;
        }).collect(Collectors.toList());
    }

    private AdminVideoResponse convertToAdminVideoResponse(VideoSessions videoSession) {
        AdminVideoResponse response = new AdminVideoResponse();
        response.setId(videoSession.getId());
        response.setTitle(videoSession.getTitle());
        response.setGroupName(videoSession.getGroupName());
        response.setVideoDescription(videoSession.getVideoDescription());
        response.setStatus(videoSession.getStatus());
        response.setAdminRespondDate(videoSession.getAdminRespondDate());
        response.setAuthor(videoSession.getAuthor());
        return response;
    }

    @Override
    public ApiResponse updateStatus(String jwt, Long videoId, VideoStatus videoStatus) {
        try {
            log.info(" Processing video status update...");
            log.info("JWT Token: " + jwt);
            log.info("Video ID: " + videoId);
            log.info("Requested Status: " + videoStatus);

            // Validate JWT Token
            String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
            log.info(" Extracted Email: " + email);

            // ✅ FIXED: Handle Optional properly
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

            if (!user.getRole().equalsIgnoreCase("ADMIN")) {
                log.error(" Unauthorized access by user: " + email);
                return new ApiResponse("Unauthorized", false, null);
            }

            // Fetch Video
            Optional<VideoSessions> optional = videoSessionsRepository.findById(videoId);
            if (optional.isEmpty()) {
                log.error(" Video not found with ID: " + videoId);
                return new ApiResponse("Video not found", false, null);
            }

            // Update Status
            VideoSessions videoSessions = optional.get();
            if (videoStatus.equals(VideoStatus.PUBLISHED) || videoStatus.equals(VideoStatus.DELETED)) {
                videoSessions.setStatus(videoStatus);
                videoSessions.setAdminRespondDate(LocalDate.now());
                videoSessionsRepository.save(videoSessions);

                log.info(" Video status updated successfully.");
                return new ApiResponse("Video " + videoStatus + " successfully", true, videoSessions);
            }

            log.warn(" Invalid status update request.");
            return new ApiResponse("Invalid status update request", false, null);

        } catch (Exception e) {
            log.error(" Error updating video status: ", e);
            return new ApiResponse("Error updating video status: " + e.getMessage(), false, null);
        }
    }

    @Override
    public Map<String, List<AdminVideoResponse>> getPublishedVideosByGroup() {
        List<VideoSessions> publishedVideos = videoSessionsRepository.findPublishedVideos();
        Map<String, List<AdminVideoResponse>> groupedVideos = new HashMap<>();

        for (VideoSessions video : publishedVideos) {
            String groupName = video.getGroupName();

            AdminVideoResponse response = new AdminVideoResponse();
            response.setId(video.getId());
            response.setTitle(video.getTitle());
            response.setGroupName(groupName);
            response.setVideoDescription(video.getVideoDescription());
            response.setStatus(video.getStatus());
            response.setAdminRespondDate(video.getAdminRespondDate());
            response.setAuthor(video.getAuthor());

            // Convert BLOB to Base64 for frontend playback
            if (video.getVideo() != null) {
                response.setVideoBase64(Base64.getEncoder().encodeToString(video.getVideo()));
                response.setVideoFileType(video.getVideoFileType());
            }

            // Directly return grouped data, no extra wrapper
            groupedVideos.computeIfAbsent(groupName, k -> new ArrayList<>()).add(response);
        }

        return groupedVideos;
    }
}