package com.Eonline.Education.Service;

import com.Eonline.Education.modals.ChatMessage;
import com.Eonline.Education.modals.Payment;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.ChatMessageRepository;
import com.Eonline.Education.repository.PaymentRepository;
import com.Eonline.Education.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    PaymentRepository paymentRepository;

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        // ✅ FIXED: Handle Optional properly
        User sender = userRepository.findByEmail(chatMessage.getSenderEmail())
                .orElse(null); // Return null if sender not found

        if (sender != null) {
            chatMessage.setSenderName(sender.getFirstName());
        }

        // ✅ FIXED: Handle Optional properly
        User receiver = userRepository.findByEmail(chatMessage.getReceiverEmail())
                .orElse(null); // Return null if receiver not found

        if (receiver != null) {
            chatMessage.setReceiverName(receiver.getFirstName());
        }

        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }

    public Map<String, Object> getUserChatHistoryWithUSer(String userEmail) {
        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(userEmail)
                .orElse(null); // Return null if user not found

        List<ChatMessage> chatMessages = chatMessageRepository.findBySenderEmailOrReceiverEmailOrderByTimestampAsc(userEmail, userEmail);

        if (chatMessages.isEmpty()) {
            return Collections.emptyMap();
        }

        ChatMessage lastMessage = chatMessages.get(chatMessages.size() - 1);
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> messagesList = new ArrayList<>();

        for (ChatMessage chat : chatMessages) {
            Map<String, Object> msg = new LinkedHashMap<>();
            msg.put("message", chat.getMessage());
            msg.put("timestamp", chat.getTimestamp());
            msg.put("email", chat.getSenderEmail());
            messagesList.add(msg);
        }

        response.put("messages", messagesList);

        if (user != null) {
            Map<String, Object> userDetails = new LinkedHashMap<>();
            userDetails.put("username", user.getFirstName());
            userDetails.put("email", user.getEmail());
            response.put("user", userDetails);
        }

        return response;
    }

    public List<Map<String, Object>> getAll() {
        List<ChatMessage> allMessages = chatMessageRepository.findAll();
        Map<String, Map<String, Object>> userMap = new LinkedHashMap<>();

        for (ChatMessage msg : allMessages) {
            String email = msg.getSenderEmail();
            String receiverEmail = msg.getReceiverEmail();

            processUser(email, msg, userMap);
            processUser(receiverEmail, msg, userMap);
        }

        return new ArrayList<>(userMap.values());
    }

    private void processUser(String email, ChatMessage msg, Map<String, Map<String, Object>> userMap) {
        // ✅ FIXED: Handle Optional properly
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Skip admin users
            if (user.getRole() != null && !user.getRole().equals("ADMIN")) {
                Map<String, Object> userDetails = userMap.getOrDefault(email, new LinkedHashMap<>());
                List<Payment> payments = paymentRepository.findAllByUserEmail(user.getEmail());
                String userID = (!payments.isEmpty()) ? payments.get(0).getUserId() : null;

                userDetails.put("userId", userID);
                userDetails.put("username", user.getFirstName());
                userDetails.put("email", user.getEmail());
                userDetails.put("profilePhoto", user.getProfilePhoto());
                userDetails.put("lastMessage", msg.getMessage());
                userDetails.put("lastMessageTimestamp", msg.getTimestamp());

                userMap.put(email, userDetails);
            }
        }
    }
}