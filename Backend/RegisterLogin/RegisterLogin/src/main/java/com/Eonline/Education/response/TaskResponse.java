package com.Eonline.Education.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TaskResponse {

    String Email;

    byte[] zipTasks;


}
