package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.Request.PaymentRequest;
import com.Eonline.Education.modals.Payment;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.PaymentRepository;
import com.Eonline.Education.user.PaymentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
     PaymentRepository paymentRepository;
    @Autowired
    OtpService otpService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public Payment processPayment(PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        List<Payment> payments=paymentRepository.findAllByUserEmail(paymentRequest.getUserEmail());
        if(!payments.isEmpty()){
            Payment payment1=payments.get(0);
            payment.setUserId(payment1.getUserId());
        }else{
            String userId= otpService.generateUserId();
            payment.setUserId(userId);
        }
        payment.setUserName(paymentRequest.getFirstName() + " " + paymentRequest.getLastName());
        payment.setUserEmail(paymentRequest.getUserEmail());
        payment.setRazorpayPaymentId(paymentRequest.getRazorpayPaymentId());
        payment.setTotalAmount(paymentRequest.getTotalAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        LocalDate joiningDate=LocalDate.now();
        payment.setJoiningDate(LocalDate.now());
        LocalDate expiryDate = joiningDate.plusMonths(paymentRequest.getCourseDuration());
        payment.setExpiryDate(expiryDate);
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        // Convert course names and prices to JSON string
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> courseDetails = new HashMap<>();
            courseDetails.put("courseNames", paymentRequest.getCourseNames());
            courseDetails.put("coursePrices", paymentRequest.getCoursePrices());
            payment.setCourseDetails(objectMapper.writeValueAsString(courseDetails));
        } catch (Exception e) {
            // Handle JSON serialization exception
            e.printStackTrace();
        }
        payment.setTotalAmount(paymentRequest.getTotalAmount());
        payment.setRazorpayPaymentId(paymentRequest.getRazorpayPaymentId());
        paymentRepository.save(payment);
        return payment;
    }

    public List<Payment> getAllPayments() {
        try {
            List<Payment> payments = paymentRepository.findAll();
            System.out.println("Payments fetched: " + payments); // See if this works
            return payments;
        } catch (Exception e) {
            e.printStackTrace(); // Log if there’s a database issue
            return Collections.emptyList(); // Return empty if an error occurs
        }
    }

    public List<Payment> getUserPaymentHistory(String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        return paymentRepository.findAllByUserEmail(email);

    }
}
