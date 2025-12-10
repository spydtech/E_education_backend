package com.Eonline.Education.Controller;

import com.Eonline.Education.Request.PaymentRequest;
import com.Eonline.Education.Service.PaymentService;
import com.Eonline.Education.modals.Payment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/store-payment")
    public void storePayment(@RequestBody PaymentRequest paymentRequest) {
        paymentService.processPayment(paymentRequest);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Payment>> getAllPayments() {
        try {
            List<Payment> payments = paymentService.getAllPayments();
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/user-courses")
    public List<Map<String, Object>> getUserCourses() {
        List<Payment> payments = paymentService.getAllPayments();
        ObjectMapper objectMapper = new ObjectMapper();

        return payments.stream().map(payment -> {
            Map<String, Object> userCourseMap = new HashMap<>();
            userCourseMap.put("userName", payment.getUserName());
            userCourseMap.put("email",payment.getUserEmail());

            try {
                Map<String, Object> courseDetails = objectMapper.readValue(payment.getCourseDetails(), new TypeReference<Map<String, Object>>() {});
                userCourseMap.put("courses", courseDetails.get("courseNames"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return userCourseMap;
        }).collect(Collectors.toList());
    }
}
