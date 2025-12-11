package com.Eonline.Education.Controller;

import com.Eonline.Education.Request.ChatGroupRequest;
import com.Eonline.Education.Service.ChatGroupService;
import com.Eonline.Education.modals.ChatGroup;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.response.ChatGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
public class ChatGroupController {

    @Autowired
    private ChatGroupService chatGroupService;

    @GetMapping
    public ResponseEntity<List<ChatGroupResponse>> getAllChatGroups() {
        List<ChatGroupResponse> chatGroups = chatGroupService.getAllChatGroups();
        return ResponseEntity.ok(chatGroups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatGroup> getChatGroupById(@PathVariable Long id) {
        Optional<ChatGroup> chatGroup = chatGroupService.getChatGroupById(id);
        return chatGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ApiResponse createChatGroup(@RequestHeader("Authorization") String jwt,@RequestBody ChatGroupRequest chatGroupRequest) {
        return chatGroupService.createChatGroup(jwt,chatGroupRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatGroupResponse> updateChatGroup(@RequestHeader("Authorization") String jwt,@PathVariable Long id, @RequestBody ChatGroupRequest updatedChatGroup) {
        ChatGroupResponse chatGroup = chatGroupService.updateChatGroup(jwt,id, updatedChatGroup);
        if (chatGroup != null) {
            return ResponseEntity.ok(chatGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatGroup(@PathVariable Long id) {
        chatGroupService.deleteChatGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/add-users")
    public ResponseEntity<ChatGroupResponse> addUsersToChatGroup(@PathVariable Long id, @RequestBody List<String> userEmails) {
      return ResponseEntity.ok(chatGroupService.addUsersToChatGroup(id, userEmails));
    }

    @PostMapping("/{id}/add-trainees")
    public ResponseEntity<ChatGroupResponse> addTraineesToChatGroup(@PathVariable Long id, @RequestBody String traineeEmails) {
       return ResponseEntity.ok( chatGroupService.addTraineesToChatGroup(id, traineeEmails));
    }
    @GetMapping("/get/user/trainee/{groupName}")
    public ChatGroupResponse usersAndTraineeListByGroupName(@PathVariable String groupName){
        return chatGroupService.usersAndTraineeListByGroupName(groupName);
    }

    @DeleteMapping("/{groupId}/remove-users")
    public String removeUsersFromChatGroup(
            @PathVariable("groupId") Long groupId,
            @RequestBody List<String> userEmailsToRemove) {

       return chatGroupService.removeUsersFromChatGroup(groupId, userEmailsToRemove);
    }

    @GetMapping("/get/users/email")
    public List<ChatGroupResponse> getUsersByTraineeEmail(@RequestHeader("Authorization") String jwt){
        return chatGroupService.getUsersByTraineeEmail(jwt);
    }
    @GetMapping("/get/chatGroup/by-user")
    public ApiResponse getUserChatGroupDetails(@RequestHeader("Authorization") String token,@RequestParam String groupName){
        return chatGroupService.getUserChatGroupDetails(token,groupName);
    }



    //trainer dashboard


//users,groups,completed groups count
    @GetMapping("/get/count")
    public ApiResponse getCount(@RequestHeader("Authorization") String token){
        return chatGroupService.getCount(token);
    }


    //admin side dashboard
    @GetMapping("/get/count/by-admin")
    public ApiResponse getCountByAdmin(@RequestHeader("Authorization") String token){
        return chatGroupService.getCountByAdmin(token);
    }
}
