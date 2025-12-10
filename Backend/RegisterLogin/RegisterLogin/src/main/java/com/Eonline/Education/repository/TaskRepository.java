package com.Eonline.Education.repository;


import com.Eonline.Education.modals.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository  extends JpaRepository<Task,Long> {


    Optional<Task> findByUserIdAndTaskName(long userId, String taskName);

    boolean existsByUserIdAndTaskName(Long id, String taskName);
}