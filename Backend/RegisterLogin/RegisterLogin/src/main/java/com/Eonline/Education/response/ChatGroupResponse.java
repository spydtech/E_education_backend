package com.Eonline.Education.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGroupResponse {
    private long id;
    private String name;
    private List<String> users;
    private List<String> trainees;
}
