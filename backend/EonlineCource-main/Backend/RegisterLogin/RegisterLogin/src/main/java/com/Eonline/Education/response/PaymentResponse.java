package com.Eonline.Education.response;


import com.Eonline.Education.user.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PaymentResponse {
    private Long id;
    private String userId;
    private String userName;
    private String userEmail;
    private List<CourseDetailResponse> courseDetails;
    private String courseDuration;
    private Double totalAmount;
    private String razorpayPaymentId;
    private String paymentMethod;
    private LocalDate joiningDate;
    private LocalDate expiryDate;
    private PaymentStatus paymentStatus;
    private Date createdAt;
}
