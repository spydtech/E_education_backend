package com.Eonline.Education.modals;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
//import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Ensure lazy loading for improved performance
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY) // Ensure lazy loading for improved performance
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Discounted price is required")
    private BigDecimal discountedPrice;

    @NotNull(message = "User ID is required")
    private Long userId;

//    private LocalDateTime deliveryDate;
//
//    // Add fields for quantity, size, color, etc., as needed
//
//    // Add auditing fields
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
    // Constructor, getters, and setters
}
