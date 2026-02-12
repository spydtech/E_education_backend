package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.ChatService;
import com.Eonline.Education.modals.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    ChatService chatService;
//    @Autowired
//    SimpMessagingTemplate messagingTemplate;
    @PostMapping("/sendMessage")
    public ChatMessage sendMessage(@RequestBody ChatMessage chatMessage) {
       return chatService.saveMessage(chatMessage);
//        messagingTemplate.convertAndSendToUser(chatMessage.getReceiverEmail(), "/queue/messages", savedMessage);
    }
    @GetMapping("/getAllUserMessages/email")
    public Map<String, Object> getUserChatHistoryWithUSer(@RequestParam String email){
        return chatService.getUserChatHistoryWithUSer(email);
    }

    @GetMapping("/getAll")
    public List<Map<String, Object>>  getAll(){
        return chatService.getAll();
    }


}
