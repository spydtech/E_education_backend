package com.Eonline.Education.Service;

import com.Eonline.Education.modals.Task;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.TaskRepository;
import com.Eonline.Education.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImplementation implements TaskService{

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public Optional<Task> saveFile(MultipartFile file, String Email, String taskName) throws IOException {
        User currentUser = userRepository.findByEmail(Email);
        Task taskEntity = new Task();
        List<String> allTasks = Arrays.asList("task1", "task2", "task3", "task4", "task5", "task6",
                "task7", "task8", "task9", "task10",
                "task11", "task12", "task13", "task14", "task15");


        if (!taskRepository.existsByUserIdAndTaskName(currentUser.getId(), taskName)) {
            if (allTasks.contains(taskName.toLowerCase())) {

                taskEntity.setName(file.getOriginalFilename());
                taskEntity.setType(file.getContentType());
                taskEntity.setData(file.getBytes());
                taskEntity.setTaskName(taskName);
                taskEntity.setUser(currentUser);
            }
            taskRepository.save(taskEntity);
        } else {
            Optional<Task> existingTask = taskRepository.findByUserIdAndTaskName(currentUser.getId(), taskName);
            existingTask.get().setName(file.getOriginalFilename());
            existingTask.get().setType(file.getContentType());
            existingTask.get().setData(file.getBytes());
            existingTask.get().setUser(currentUser);
            taskRepository.save(existingTask.get());
        }
        return  taskRepository.findByUserIdAndTaskName(currentUser.getId(),taskName);
    }

    @Override
    public Task getFile(String Email,String taskName) {
        User currentUser = userRepository.findByEmail(Email);
        long userId = currentUser.getId();

        return (Task) taskRepository.findByUserIdAndTaskName(userId,taskName).orElseThrow(() -> new RuntimeException("File not found"));
    }
}