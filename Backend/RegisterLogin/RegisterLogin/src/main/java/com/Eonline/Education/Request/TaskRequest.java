package com.Eonline.Education.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    private String assignmentDescription;
    private LocalDate dueDate;
    private String chatGroup;
    private List<String> users;

}
