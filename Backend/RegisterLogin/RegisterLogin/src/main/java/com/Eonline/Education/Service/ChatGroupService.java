package com.Eonline.Education.Service;

import com.Eonline.Education.modals.ChatGroup;
import com.Eonline.Education.modals.TraineeCredentialGenerator;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.ChatGroupRepository;
import com.Eonline.Education.repository.TraineeRepository;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.response.ChatGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatGroupService {

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    public ChatGroup createChatGroup(ChatGroup chatGroup) {
        return chatGroupRepository.save(chatGroup);
    }

    public List<ChatGroupResponse> getAllChatGroups() {

            List<ChatGroupResponse> chatGroupResponses = new ArrayList<>();
            List<ChatGroup> chatGroups = chatGroupRepository.findAll();
            List<User> users = userRepository.findAll();

            for (ChatGroup chatGroup : chatGroups) {
                ChatGroupResponse chatGroupResponse = new ChatGroupResponse();
                chatGroupResponse.setId(chatGroup.getId());
                chatGroupResponse.setName(chatGroup.getName());

                List<String> userDetails = new ArrayList<>();

                for (User member : chatGroup.getMembers()) {
                    String userName = member.getFirstName() + " " + member.getLastName();
                    String email = member.getEmail();
                    String resUserDetail = "username is: " + userName + ", email is: " + email;
                    userDetails.add(resUserDetail);
                }

                List<String> traineeDetails = new ArrayList<>();

                for (TraineeCredentialGenerator member : chatGroup.getTrainees()) {
                    String userName = member.getFirstName() + " " + member.getLastName();
                    String email = member.getEmail();
                    String resUserDetail = "username is: " + userName + ", email is: " + email;
                    traineeDetails.add(resUserDetail);
                }

                chatGroupResponse.setUsers(userDetails);
               // chatGroupResponses.add(chatGroupResponse);
                chatGroupResponse.setTrainees(traineeDetails);
                chatGroupResponses.add(chatGroupResponse);
            }

            return chatGroupResponses;
        }



    public Optional<ChatGroup> getChatGroupById(Long id) {
        return chatGroupRepository.findById(id);
    }

    public ChatGroup updateChatGroup(Long id, ChatGroup updatedChatGroup) {
        Optional<ChatGroup> optionalChatGroup = chatGroupRepository.findById(id);
        if (optionalChatGroup.isPresent()) {
            ChatGroup chatGroup = optionalChatGroup.get();
            chatGroup.setName(updatedChatGroup.getName());
            return chatGroupRepository.save(chatGroup);
        } else {
            return null;
        }
    }

    public void deleteChatGroup(Long id) {
        chatGroupRepository.deleteById(id);
    }

    public ChatGroup addUsersToChatGroup(Long id, List<String> userEmails) {
        Optional<ChatGroup> optionalChatGroup = chatGroupRepository.findById(id);
        if (optionalChatGroup.isPresent()) {
            ChatGroup chatGroup = optionalChatGroup.get();
            for (String email : userEmails) {
                User user = userRepository.findByEmail(email);
                if (user != null) {
                    chatGroup.addUser(user);
                }
            }
            return chatGroupRepository.save(chatGroup);
        } else {
            return null;
        }
    }

    public ChatGroup addTraineesToChatGroup(Long id, List<String> traineeEmails) {
        Optional<ChatGroup> optionalChatGroup = chatGroupRepository.findById(id);
        if (optionalChatGroup.isPresent()) {
            ChatGroup chatGroup = optionalChatGroup.get();
            for (String email : traineeEmails) {
                TraineeCredentialGenerator trainee = traineeRepository.findByEmail(email);
                if (trainee != null) {
                    chatGroup.addTrainee(trainee);
                }
            }
            return chatGroupRepository.save(chatGroup);
        } else {
            return null;
        }
    }

    public ChatGroup removeUsersFromChatGroup(Long groupId, List<String> userEmailsToRemove) {
        Optional<ChatGroup> optionalChatGroup = chatGroupRepository.findById(groupId);
        if (optionalChatGroup.isPresent()) {
            ChatGroup chatGroup = optionalChatGroup.get();
            List<User> usersToRemove = chatGroup.getMembers().stream()
                    .filter(user -> userEmailsToRemove.contains(user.getEmail()))
                    .collect(Collectors.toList());
            chatGroup.getMembers().removeAll(usersToRemove);
            return chatGroupRepository.save(chatGroup);
        }
        return null;
    }

    public ChatGroup removeTraineesFromChatGroup(Long groupId, List<String> traineeEmailsToRemove) {
        Optional<ChatGroup> optionalChatGroup = chatGroupRepository.findById(groupId);
        if (optionalChatGroup.isPresent()) {
            ChatGroup chatGroup = optionalChatGroup.get();
            List<TraineeCredentialGenerator> traineesToRemove = chatGroup.getTrainees().stream()
                    .filter(trainee -> traineeEmailsToRemove.contains(trainee.getEmail()))
                    .collect(Collectors.toList());
            chatGroup.getTrainees().removeAll(traineesToRemove);
            return chatGroupRepository.save(chatGroup);
        }
        return null;
    }
}
