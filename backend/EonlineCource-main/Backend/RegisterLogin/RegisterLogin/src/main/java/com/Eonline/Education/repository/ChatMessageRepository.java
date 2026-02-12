package com.Eonline.Education.repository;

import com.Eonline.Education.modals.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    List<ChatMessage> findBySenderEmailOrReceiverEmailOrderByTimestampAsc(String userEmail, String userEmail1);

    List<ChatMessage> findAllByOrderByTimestampDesc();

    List<ChatMessage> findBySenderEmailAndReceiverEmailAndReadFalse(String senderEmail, String receiverEmail);
}
