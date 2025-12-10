package com.Eonline.Education.repository;

import com.Eonline.Education.modals.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {
}
