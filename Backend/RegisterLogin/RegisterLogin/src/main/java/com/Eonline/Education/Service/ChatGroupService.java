package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.Request.ChatGroupRequest;
import com.Eonline.Education.exceptions.NotFoundException;
import com.Eonline.Education.modals.ChatGroup;
import com.Eonline.Education.modals.Payment;
import com.Eonline.Education.modals.TraineeCredentialGenerator;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.ChatGroupRepository;
import com.Eonline.Education.repository.PaymentRepository;
import com.Eonline.Education.repository.TraineeRepository;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.response.ChatGroupResponse;
import com.Eonline.Education.response.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;


@Service
public class ChatGroupService {

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public ApiResponse createChatGroup(String jwt, ChatGroupRequest chatGroup) {
        if (chatGroup == null) {
            return new ApiResponse("ChatGroupRequest cannot be null");
        }
        if (chatGroup.getGroupName() == null || chatGroup.getGroupName().trim().isEmpty()) {

            return new ApiResponse("Group name is mandatory");
        }
        if (chatGroup.getCourseEndDate() == null) {
            return new ApiResponse("Course end date is mandatory");
        }
        if (chatGroup.getTrainees() == null || chatGroup.getTrainees().isBlank()) { // handles null and empty/whitespace strings
            return new ApiResponse("Trainee email is mandatory");
        }
        Optional<ChatGroup> optionalGroup = chatGroupRepository.findByNameIgnoreCase(chatGroup.getGroupName());
        ChatGroup group = optionalGroup.orElseGet(() -> {
            ChatGroup newGroup = new ChatGroup();
            newGroup.setName(chatGroup.getGroupName());
            newGroup.setCourseStartDate(LocalDate.now());
            newGroup.setCourseEndDate(chatGroup.getCourseEndDate());
            return newGroup;
        });
        Set<User> existingUsers = new HashSet<>(group.getMembers() != null ? group.getMembers() : new ArrayList<>());
        if (chatGroup.getUsers() != null && !chatGroup.getUsers().isEmpty()) {
            for (String email : chatGroup.getUsers()) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
                if (user != null) {
                    existingUsers.add(user);
                } else {
                    return new ApiResponse("User not found for email: " + email);
                }
            }
        }
        group.setMembers(new ArrayList<>(existingUsers));
        TraineeCredentialGenerator trainee = traineeRepository.findByEmail(chatGroup.getTrainees());
        if (trainee != null) {
            group.setTrainees(trainee);
        } else {
            return new ApiResponse("Trainee not found for email: " + chatGroup.getTrainees());
        }
        chatGroupRepository.save(group);
        return new ApiResponse  (generateUserResponse(group));
    }





    public List<ChatGroupResponse> getAllChatGroups() {

        List<ChatGroupResponse> chatGroupResponses = new ArrayList<>();
        List<ChatGroup> chatGroups = chatGroupRepository.findAll();
        for (ChatGroup chatGroup : chatGroups) {
            ChatGroupResponse chatGroupResponse = new ChatGroupResponse();
            chatGroupResponse.setId(chatGroup.getId());
            chatGroupResponse.setGroupName(chatGroup.getName());
            chatGroupResponse.setCourseEndDate(chatGroup.getCourseEndDate());
            chatGroupResponse.setCourseStartDate(chatGroup.getCourseStartDate());
            List<UserResponse> userResponses = new ArrayList<>();
            for (User member : chatGroup.getMembers()) {
                List<Payment> payment = paymentRepository.findAllByUserEmail(member.getEmail());
                UserResponse userResponse = new UserResponse();
                if (!payment.isEmpty()) {
                    userResponse.setUserId(payment.get(0).getUserId());
                }
                if (member.getLastName() != null) {
                    userResponse.setFullName(member.getFirstName() + " " + member.getLastName());
                } else {
                    userResponse.setFullName(member.getFirstName());
                }
                userResponse.setEmail(member.getEmail());
                userResponse.setUserstatus(member.getStatus());
                userResponse.setCreatedAt(member.getCreatedAt());
                userResponses.add(userResponse);
            }
            chatGroupResponse.setUsers(userResponses);
            UserResponse traineeResponse = new UserResponse();
            Optional<TraineeCredentialGenerator> optional = traineeRepository.findById(chatGroup.getTrainees().getId());
            if (optional.isPresent()) {
                TraineeCredentialGenerator trainee = optional.get();
                if (trainee.getLastName() != null) {
                    traineeResponse.setFullName(trainee.getFirstName() + " " + trainee.getLastName());
                } else {
                    traineeResponse.setFullName(trainee.getFirstName());
                }
                traineeResponse.setEmail(trainee.getEmail());
                chatGroupResponse.setTrainees(traineeResponse);
                chatGroupResponses.add(chatGroupResponse);
            }else{
                throw new NotFoundException("trainer not found");
            }
        }
        return chatGroupResponses;
    }


    public Optional<ChatGroup> getChatGroupById(Long id) {
        return chatGroupRepository.findById(id);
    }

    public ChatGroupResponse updateChatGroup(String jwt, Long id, ChatGroupRequest updatedChatGroup) {
        Optional<ChatGroup> optionalChatGroup = chatGroupRepository.findById(id);
        if (optionalChatGroup.isPresent()) {
            ChatGroup chatGroup = optionalChatGroup.get();
            if(updatedChatGroup.getGroupName()!=null) {
                chatGroup.setName(updatedChatGroup.getGroupName());
            }
            if(updatedChatGroup.getCourseEndDate()!=null) {
                chatGroup.setCourseEndDate(updatedChatGroup.getCourseEndDate());
            }
            if(chatGroup.getTrainees()!=null) {
                Optional<TraineeCredentialGenerator> optional = traineeRepository.findById(chatGroup.getTrainees().getId());
                optional.ifPresent(chatGroup::setTrainees);
            }
            chatGroupRepository.save(chatGroup);
            return generateUserResponse(chatGroup);
        } else {
            return null;
        }
    }

    public void deleteChatGroup(Long id) {
        chatGroupRepository.deleteById(id);
    }

    public ChatGroupResponse addUsersToChatGroup(Long id, List<String> userEmails) {
        Optional<ChatGroup> optionalChatGroup = chatGroupRepository.findById(id);
        if (optionalChatGroup.isPresent()) {
            ChatGroup chatGroup = optionalChatGroup.get();
            for (String email : userEmails) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
                if (user != null) {
                    boolean isUserAlreadyInGroup = chatGroup.getMembers().stream()
                            .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()));
                    if (!isUserAlreadyInGroup) {
                        chatGroup.addUser(user);
                    }
                }
            }
            chatGroupRepository.save(chatGroup);
            return returnChatGroupForUsers(chatGroup);
        } else {
            return null;
        }
    }

    private ChatGroupResponse returnChatGroupForUsers(ChatGroup chatGroup) {
        ChatGroupResponse response = new ChatGroupResponse();
        response.setId(chatGroup.getId());
        response.setGroupName(chatGroup.getName());
        response.setCourseStartDate(chatGroup.getCourseStartDate());
        response.setCourseEndDate(chatGroup.getCourseEndDate());
        List<UserResponse> userResponses = new ArrayList<>();
        for (User member : chatGroup.getMembers()) {
            List<Payment> payment = paymentRepository.findAllByUserEmail(member.getEmail());
            UserResponse userResponse = new UserResponse();
            if (!payment.isEmpty()) {
                userResponse.setUserId(payment.get(0).getUserId());
            }
            if (member.getLastName() != null) {
                userResponse.setFullName(member.getFirstName() + " " + member.getLastName());
            } else {
                userResponse.setFullName(member.getFirstName());
            }
            userResponse.setEmail(member.getEmail());
            userResponse.setUserstatus(member.getStatus());
            userResponse.setCreatedAt(member.getCreatedAt());
            userResponses.add(userResponse);
        }
        response.setUsers(userResponses);
        return response;
    }


    public ChatGroupResponse usersAndTraineeListByGroupName(String groupName) {
        Optional<ChatGroup> chatGroup = chatGroupRepository.findByName(groupName);
        if (chatGroup.isPresent()) {
            return generateUserResponse(chatGroup.get());
        } else {
            throw new EntityNotFoundException(groupName + " not found.");
        }
    }

    private ChatGroupResponse generateUserResponse(ChatGroup chatGroup) {
        ChatGroupResponse response = new ChatGroupResponse();
        response.setId(chatGroup.getId());
        response.setGroupName(chatGroup.getName());
        response.setCourseStartDate(chatGroup.getCourseStartDate());
        response.setCourseEndDate(chatGroup.getCourseEndDate());

        List<UserResponse> userResponses = new ArrayList<>();
        for (User member : chatGroup.getMembers()) {
            UserResponse userResponse = new UserResponse();
            List<Payment> payment = paymentRepository.findAllByUserEmail(member.getEmail());
            if (!payment.isEmpty()) {
                userResponse.setUserId(payment.get(0).getUserId());
            }
            if (member.getLastName() != null) {
                userResponse.setFullName(member.getFirstName() + " " + member.getLastName());
            } else {
                userResponse.setFullName(member.getFirstName());
            }
            userResponse.setEmail(member.getEmail());
            userResponse.setUserstatus(member.getStatus());
            userResponse.setCreatedAt(member.getCreatedAt());
            userResponses.add(userResponse);
        }
        response.setUsers(userResponses);
        UserResponse traineeResponse = new UserResponse();
        Optional<TraineeCredentialGenerator> optional = traineeRepository.findById(chatGroup.getTrainees().getId());
        if (optional.isPresent()) {
            TraineeCredentialGenerator trainee = optional.get();
            if (trainee.getLastName() != null) {
                traineeResponse.setFullName(trainee.getFirstName() + " " + trainee.getLastName());
            } else {
                traineeResponse.setFullName(trainee.getFirstName());
            }
            traineeResponse.setEmail(trainee.getEmail());
            response.setTrainees(traineeResponse);
        }
        return response;
    }


    public ChatGroupResponse addTraineesToChatGroup(Long id, String traineeEmails) {
        Optional<ChatGroup> optionalChatGroup = chatGroupRepository.findById(id);
        if (optionalChatGroup.isPresent()) {
            ChatGroup chatGroup = optionalChatGroup.get();
            TraineeCredentialGenerator trainee = traineeRepository.findByEmail(traineeEmails);
            if (trainee != null) {
                chatGroup.setTrainees(trainee);
            }
            chatGroupRepository.save(chatGroup);
            return returnChatGroupForTrainees(chatGroup);
        } else {
            return null;
        }
    }

    private ChatGroupResponse returnChatGroupForTrainees(ChatGroup chatGroup) {
        ChatGroupResponse response = new ChatGroupResponse();
        response.setId(chatGroup.getId());
        response.setGroupName(chatGroup.getName());
        response.setCourseStartDate(chatGroup.getCourseStartDate());
        response.setCourseEndDate(chatGroup.getCourseEndDate());
        UserResponse traineeResponse = new UserResponse();
        Optional<TraineeCredentialGenerator> optional = traineeRepository.findById(chatGroup.getTrainees().getId());
        if (optional.isPresent()) {
            TraineeCredentialGenerator trainee = optional.get();
            if (trainee.getLastName() != null) {
                traineeResponse.setFullName(trainee.getFirstName() + " " + trainee.getLastName());
            } else {
                traineeResponse.setFullName(trainee.getFirstName());
            }
            traineeResponse.setEmail(trainee.getEmail());
            response.setTrainees(traineeResponse);
        }
        return response;
    }

    public String removeUsersFromChatGroup(Long groupId, List<String> userEmailsToRemove) {
        Optional<ChatGroup> optionalChatGroup = chatGroupRepository.findById(groupId);
        if (optionalChatGroup.isPresent()) {
            ChatGroup chatGroup = optionalChatGroup.get();
            List<User> usersToRemove = chatGroup.getMembers().stream()
                    .filter(user -> userEmailsToRemove.contains(user.getEmail()))
                    .toList();
            chatGroup.getMembers().removeAll(usersToRemove);
            chatGroupRepository.save(chatGroup);
            return "User remove successfully from " + chatGroup.getName() + " Group";
        }
        return "Group Not Found";
    }

    public List<ChatGroupResponse> getUsersByTraineeEmail(String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        TraineeCredentialGenerator trainee = traineeRepository.findByEmail(email);
        if (trainee == null) {
            throw new RuntimeException("Trainee not found with email: " + email);
        }

        List<ChatGroup> chatGroups = chatGroupRepository.findAllByTrainees_Id(trainee.getId());
        if (chatGroups == null || chatGroups.isEmpty()) {
            return Collections.emptyList();
        }

        List<ChatGroupResponse> responses = new ArrayList<>();
        for (ChatGroup chatGroup : chatGroups) {
            ChatGroupResponse groupUserResponse = generateUserResponse(chatGroup);
            responses.add(groupUserResponse);
        }
        return responses;
    }



    private String buildFullName(String firstName, String lastName) {
        if (firstName == null && lastName == null) return null;
        if (lastName == null || lastName.isEmpty()) return firstName;
        return firstName + " " + lastName;
    }

    public ApiResponse getUserChatGroupDetails(String token, String groupName) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        if (user == null) {
            return new ApiResponse("User not found", false);
        }
        Optional<ChatGroup> chatGroup = chatGroupRepository.findByName(groupName);
        if (chatGroup.isEmpty()) {
            return new ApiResponse("Chat group not found", false);
        }
        if (!chatGroup.get().getMembers().contains(user)) {
            return new ApiResponse("Access Denied: You are not part of this group", false);
        }
        return new ApiResponse(generateUserResponse(chatGroup.get()));
    }

    public ApiResponse getCount(String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        TraineeCredentialGenerator trainee=traineeRepository.findByEmail(email);
        if(trainee!=null){
            Map<String,Long> map=new HashMap<>();
            LocalDate today=LocalDate.now();
            List<ChatGroup> completedGroupsList=chatGroupRepository.findAllByCourseEndDateBefore(today);
            long completedGroups=completedGroupsList.size();
            List<ChatGroup> groups=chatGroupRepository.findAllByTrainees_Id(trainee.getId());
            long enrolledGroups=groups.size()-completedGroups;
            List<User> users=new ArrayList<>();
           for(ChatGroup group:groups){
               users.addAll(group.getMembers());
           }
           Stream<User> distinct=users.stream().distinct();
           long usersCount=distinct.count();
            map.put("enrolledGroups",enrolledGroups);
            map.put("completedGroups",completedGroups);
            map.put("usersCount",usersCount);
            return new ApiResponse(map);
        }else{
            return new ApiResponse("Trainer Not Found", HttpStatus.NOT_FOUND);
        }
    }

    public ApiResponse getCountByAdmin(String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Map<String,Long> map=new HashMap<>();
        if(user!=null&& user.getRole().equalsIgnoreCase("ADMIN")){
            LocalDate date=LocalDate.now();
            List<ChatGroup> chatGroups=chatGroupRepository.findAllByCourseEndDateAfter(date);
            long activeCourses=chatGroups.size();
            List<ChatGroup> allGroups=chatGroupRepository.findAll();
            List<User> usersList=new ArrayList<>();
            for(ChatGroup group:allGroups){
                usersList.addAll(group.getMembers());
            }
            Stream<User> distinct=usersList.stream().distinct();
            long usersCount=distinct.count();
            map.put("activeCourses",activeCourses);
            map.put("usersCount",usersCount);
            return new ApiResponse(map);
        }else{
            return new ApiResponse("Access Denied this page only for admin", HttpStatus.UNAUTHORIZED);
        }
    }
}
