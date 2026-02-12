package com.Eonline.Education.repository;

import com.Eonline.Education.modals.Task;
import com.Eonline.Education.modals.TaskUser;
import com.Eonline.Education.modals.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskUserRepository extends JpaRepository<TaskUser,Long> {

    List<TaskUser> findByUser(User user);

    Optional<TaskUser> findByTaskAndUser(Task task, User user);

    List<TaskUser> findByTaskTraineeEmail(String email);

    Optional<TaskUser> findByTask_Id(Long taskId);
}
