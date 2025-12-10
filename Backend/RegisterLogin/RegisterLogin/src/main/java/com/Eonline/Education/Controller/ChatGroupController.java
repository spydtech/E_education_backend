package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.ChatGroupService;
import com.Eonline.Education.modals.ChatGroup;
import com.Eonline.Education.response.ChatGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chat-groups")
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
    public ResponseEntity<ChatGroup> createChatGroup(@RequestBody ChatGroup chatGroup) {
        ChatGroup createdChatGroup = chatGroupService.createChatGroup(chatGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChatGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatGroup> updateChatGroup(@PathVariable Long id, @RequestBody ChatGroup updatedChatGroup) {
        ChatGroup chatGroup = chatGroupService.updateChatGroup(id, updatedChatGroup);
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
    public ResponseEntity<ChatGroup> addUsersToChatGroup(@PathVariable Long id, @RequestBody List<String> userEmails) {
        ChatGroup chatGroup = chatGroupService.addUsersToChatGroup(id, userEmails);
        if (chatGroup != null) {
            return ResponseEntity.ok(chatGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/add-trainees")
    public ResponseEntity<ChatGroup> addTraineesToChatGroup(@PathVariable Long id, @RequestBody List<String> traineeEmails) {
        ChatGroup chatGroup = chatGroupService.addTraineesToChatGroup(id, traineeEmails);
        if (chatGroup != null) {
            return ResponseEntity.ok(chatGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{groupId}/remove-users")
    public ResponseEntity<ChatGroup> removeUsersFromChatGroup(
            @PathVariable("groupId") Long groupId,
            @RequestBody List<String> userEmailsToRemove) {

        ChatGroup chatGroup = chatGroupService.removeUsersFromChatGroup(groupId, userEmailsToRemove);

        if (chatGroup != null) {
            return ResponseEntity.ok(chatGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{groupId}/remove-trainees")
    public ResponseEntity<ChatGroup> removeTraineesFromChatGroup(
            @PathVariable("groupId") Long groupId,
            @RequestBody List<String> traineeEmailsToRemove) {

        ChatGroup chatGroup = chatGroupService.removeTraineesFromChatGroup(groupId, traineeEmailsToRemove);

        if (chatGroup != null) {
            return ResponseEntity.ok(chatGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
