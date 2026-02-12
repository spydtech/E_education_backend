package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.Request.MeetingRequest;
import com.Eonline.Education.exceptions.MandatoryFieldMissingException;
import com.Eonline.Education.modals.ChatGroup;
import com.Eonline.Education.modals.Meeting;
import com.Eonline.Education.modals.TraineeCredentialGenerator;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.ChatGroupRepository;
import com.Eonline.Education.repository.MeetingRepository;
import com.Eonline.Education.repository.TraineeRepository;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.response.MeetingResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingServiceImpl implements MeetingService{
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    ChatGroupRepository chatGroupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TraineeRepository traineeRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Override
    public ApiResponse meetSave(MeetingRequest meetingRequest,String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Optional<ChatGroup> optional=chatGroupRepository.findByNameIgnoreCase(meetingRequest.getGroupName());
        if(optional.isPresent()) {
            Meeting meeting=new Meeting();
            meeting.setTitle(meetingRequest.getTitle());
            meeting.setFromTime(meetingRequest.getFromTime());
            meeting.setToTime(meetingRequest.getToTime());
            meeting.setFromDate(meetingRequest.getFromDate());
            meeting.setToDate(meetingRequest.getToDate());
            meeting.setPhone(user.getPhoneNumber());
            meeting.setLink(meetingRequest.getLink());
            meeting.setPin(meetingRequest.getPin());
            meeting.setGroup(optional.get());
            if(user.getLastName()!=null){
                meeting.setOrganizer(user.getFirstName()+" " +user.getLastName());
            }else{
                meeting.setOrganizer(user.getFirstName());
            }
            meetingRepository.save(meeting);
           return new ApiResponse("meeting save successfully",true,meetingResponse(meeting));
        }else{
            return new ApiResponse("group not found");
        }
    }

    private MeetingResponse meetingResponse(Meeting request){
        MeetingResponse meetingResponse=new MeetingResponse();
        meetingResponse.setTitle(request.getTitle());
        meetingResponse.setFromDate(request.getFromDate());
        meetingResponse.setToDate(request.getToDate());
        meetingResponse.setFromTime(request.getFromTime());
        meetingResponse.setToTime(request.getFromTime());
        meetingResponse.setLink(request.getLink());
        meetingResponse.setPhone(request.getPhone());
        meetingResponse.setOrganizer(request.getOrganizer());
        meetingResponse.setPin(request.getPin());
        Optional<ChatGroup> chatGroup=chatGroupRepository.findById(request.getGroup().getId());
        meetingResponse.setGroupName(chatGroup.get().getName());
        return meetingResponse;
    }
    private List<MeetingResponse> mapToMeetingResponseList(List<Meeting> meetings) {
        return meetings.stream()
                .map(this::meetingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public  ApiResponse getMeetings(Long groupId) {
        List<Meeting> meeting=meetingRepository.findAllByGroupId(groupId);
        if(!meeting.isEmpty()){
            return new ApiResponse(mapToMeetingResponseList(meeting));
        }else{
            return new ApiResponse("group not found");
        }
    }

    @Override
    public  ApiResponse getAll() {
        List<Meeting> meetings=meetingRepository.findAll();
        return new ApiResponse(mapToMeetingResponseList(meetings));
    }

    public List<String> getGroupNamesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        List<ChatGroup> allGroups = chatGroupRepository.findAll();
        return allGroups.stream()
                .filter(group -> group.getMembers().contains(user))
                .map(ChatGroup::getName)
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse getAllTraineeMeetings(String jwt) {
        String traineeEmail = jwtTokenProvider.getEmailFromJwtToken(jwt);
        TraineeCredentialGenerator trainee = traineeRepository.findByEmail(traineeEmail);
        List<ChatGroup> allGroups = chatGroupRepository.findAll();
        List<String> groups= allGroups.stream()
                .filter(group -> group.getTrainees().getEmail().equals(trainee.getEmail()))
                .map(ChatGroup::getName)
                .toList();
        System.out.println("groups"+groups);
        List<ChatGroup> chatGroups = chatGroupRepository.findByNameInIgnoreCase(groups);
        if (chatGroups.isEmpty()) {
            return new ApiResponse("No groups found for the provided names");
        }
        List<MeetingResponse> responseList = new ArrayList<>();
        for (ChatGroup chatGroup : chatGroups) {
            List<Meeting> meetings = meetingRepository.findAllByGroupId(chatGroup.getId());
            if (!meetings.isEmpty()) {
                responseList.addAll(mapToMeetingResponseList(meetings));
            }
        }
        return responseList.isEmpty() ? new ApiResponse("No meetings found for the given groups") : new ApiResponse("trainer meetings",true,responseList);
    }

    @Override
    public ApiResponse getAllUpComingMeetings(String jwt) {
        String traineeEmail = jwtTokenProvider.getEmailFromJwtToken(jwt);
        TraineeCredentialGenerator trainee = traineeRepository.findByEmail(traineeEmail);
        List<String> groups = chatGroupRepository.findAll().stream()
                .filter(group -> group.getTrainees().getEmail().equals(trainee.getEmail()))
                .map(ChatGroup::getName)
                .toList();
        List<ChatGroup> chatGroups = chatGroupRepository.findByNameInIgnoreCase(groups);
        if (chatGroups.isEmpty()) {
            return new ApiResponse("No groups found for the provided names");
        }
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        List<MeetingResponse> responseList = new ArrayList<>();
        for (ChatGroup chatGroup : chatGroups) {
            List<Meeting> meetings = meetingRepository.findAllByGroupIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndToTimeGreaterThan(
                    chatGroup.getId(), today, today,currentTime);
            if (!meetings.isEmpty()) {
                responseList.addAll(mapToMeetingResponseList(meetings));
            }
        }
        if (responseList.isEmpty()) {
            return new ApiResponse(new ArrayList<>());
        }
        return new ApiResponse(responseList);
    }


    @Override
    public ApiResponse getAllUserMeetings(String jwt) {
        String userEmail = jwtTokenProvider.getEmailFromJwtToken(jwt);
        String email = "";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        List<ChatGroup> allGroups = chatGroupRepository.findAll();
        List<String> groups= allGroups.stream()
                .filter(group -> group.getMembers().contains(user))
                .map(ChatGroup::getName)
                .toList();
        List<ChatGroup> chatGroups = chatGroupRepository.findByNameInIgnoreCase(groups);
        if (chatGroups.isEmpty()) {
            return new ApiResponse("No groups found for the provided names");
        }
        List<MeetingResponse> responseList = new ArrayList<>();
        for (ChatGroup chatGroup : chatGroups) {
            List<Meeting> meetings = meetingRepository.findAllByGroupId(chatGroup.getId());
            if (!meetings.isEmpty()) {
                responseList.addAll(mapToMeetingResponseList(meetings));
            }
        }
        return responseList.isEmpty() ? new ApiResponse("No meetings found for the given groups") : new ApiResponse("user meetings",true,responseList);
    }
}
