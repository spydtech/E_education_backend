package com.Eonline.Education.Service;


import com.Eonline.Education.modals.Task;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface TaskService {

    public Optional<Task> saveFile(MultipartFile file, String Email, String taskName) throws IOException;

    public Task getFile(String Email,String taskName);
}