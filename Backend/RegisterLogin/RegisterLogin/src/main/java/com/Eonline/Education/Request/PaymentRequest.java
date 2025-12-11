package com.Eonline.Education.Request;

import lombok.Data;

import java.util.List;

@Data
public class PaymentRequest {
    private Long userId;
    private String firstName;
    private String lastName;
    private String userEmail;
    private List<String> courseNames;
    private List<Double> coursePrices;
    private Double totalAmount;
    private String razorpayPaymentId;
    private String paymentMethod;
    private Integer courseDuration;

    // Getters and Setters
}
