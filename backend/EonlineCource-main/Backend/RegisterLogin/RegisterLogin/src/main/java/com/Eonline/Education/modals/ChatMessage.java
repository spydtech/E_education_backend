package com.Eonline.Education.modals;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderEmail;   // User's email
    private String senderName;    // User's name

    private String receiverEmail; // Admin or another user
    private String receiverName;  // Admin or another user's name

    @Lob
    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime timestamp;

    // ✅ ADD THIS FIELD
    @Column(name = "is_read", columnDefinition = "boolean default false")
    private boolean read = false;
}