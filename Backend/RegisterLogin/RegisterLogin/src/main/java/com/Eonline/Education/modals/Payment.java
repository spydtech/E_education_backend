package com.Eonline.Education.modals;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String userName; // Concatenated firstName and lastName
    private String userEmail;
    private String courseDetails;

    private double totalAmount;

    private String razorpayPaymentId;

    private String paymentMethod;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    // Getters and setters, constructors, and other methods
}