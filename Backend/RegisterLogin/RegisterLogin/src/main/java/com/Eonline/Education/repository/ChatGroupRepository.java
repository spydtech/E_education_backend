package com.Eonline.Education.repository;

import com.Eonline.Education.modals.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {

    Optional<ChatGroup> findByNameIgnoreCase(String group);

    Optional<ChatGroup> findByName(String groupName);

    List<ChatGroup> findByNameInIgnoreCase(List<String> groups);

    List<Long> findUserIdsByIdIn(List<Long> chatGroupIds);

    List<ChatGroup> findByTrainees_Id(long id);

    List<ChatGroup> findAllByTrainees_Id(long id);

    List<ChatGroup> findAllByCourseEndDateBefore(LocalDate today);

    List<ChatGroup> findAllByCourseEndDateAfter(LocalDate date);
}
