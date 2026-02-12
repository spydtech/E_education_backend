package com.Eonline.Education.Request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long userId;
    private String courseId;
    private String courseName;
    private double coursePrice;

    // Constructors, getters, and setters
}