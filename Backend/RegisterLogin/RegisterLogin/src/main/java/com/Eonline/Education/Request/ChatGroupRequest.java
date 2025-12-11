package com.Eonline.Education.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGroupRequest {
    private String groupName;
    private LocalDate courseEndDate;
    private List<String> users;
    private String trainees;
}
