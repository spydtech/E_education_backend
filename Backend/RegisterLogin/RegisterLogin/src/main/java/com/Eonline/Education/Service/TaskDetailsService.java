package com.Eonline.Education.Service;

import com.Eonline.Education.modals.TaskDetails;

import java.util.List;

public interface TaskDetailsService {
    public TaskDetails createTaskDetails(TaskDetails taskDetails);
    public TaskDetails getTaskDetailsById(Long id);
    public List<TaskDetails> getAllTasks();
    public TaskDetails updateTaskDetails(Long id,TaskDetails taskDetails);
    public String deleteTaskDetails(Long id);
}
