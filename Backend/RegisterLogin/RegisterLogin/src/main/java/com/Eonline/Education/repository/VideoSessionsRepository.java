package com.Eonline.Education.repository;

import com.Eonline.Education.modals.VideoSessions;
import com.Eonline.Education.user.VideoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoSessionsRepository extends JpaRepository<VideoSessions,Long> {
    List<VideoSessions> findAllByTrainer(String email);
    List<VideoSessions> findByStatus(VideoStatus status);
    List<VideoSessions> findByGroupNameAndStatus(String groupName, VideoStatus status);
    @Query("SELECT v FROM VideoSessions v WHERE v.status = 'PUBLISHED' AND v.video IS NOT NULL ORDER BY v.groupName")
    List<VideoSessions> findPublishedVideos();


}