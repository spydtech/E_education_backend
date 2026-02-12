package com.Eonline.Education.Service;

import com.Eonline.Education.modals.TaskDetails;
import com.Eonline.Education.repository.TaskDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskDetailsServiceImpl implements TaskDetailsService{
    @Autowired
    TaskDetailsRepository taskDetailsRepository;
    @Override
    public TaskDetails createTaskDetails(TaskDetails taskDetails) {
        return taskDetailsRepository.save(taskDetails);

    }

    @Override
    public TaskDetails getTaskDetailsById(Long id) {
       Optional<TaskDetails> taskDetails= taskDetailsRepository.findById(id);
       if(taskDetails.isPresent()){
           return taskDetails.get();
       }else{
           throw new RuntimeException("id not found");
       }
    }

    @Override
    public List<TaskDetails> getAllTasks() {
        return taskDetailsRepository.findAll();
    }

    @Override
    public TaskDetails updateTaskDetails(Long id, TaskDetails updatedTaskDetails) {

        Optional<TaskDetails> optionalTaskDetails = taskDetailsRepository.findById(id);
        if (optionalTaskDetails.isPresent()) {
            TaskDetails existingTaskDetails = optionalTaskDetails.get();
            existingTaskDetails.setDay(updatedTaskDetails.getDay());
            existingTaskDetails.setDue(updatedTaskDetails.getDue());
            existingTaskDetails.setDetails(updatedTaskDetails.getDetails());
            return taskDetailsRepository.save(existingTaskDetails);
        } else {
            throw new RuntimeException("Task not found with id " + id);
        }
    }

    @Override
    public String deleteTaskDetails(Long id) {
        Optional<TaskDetails> taskDetails=taskDetailsRepository.findById(id);
        if(taskDetails.isPresent()){
            taskDetailsRepository.deleteById(id);
            return "deleted successfully";
        }else{
            return "id not found";
        }
    }
}
