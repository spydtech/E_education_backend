package com.Eonline.Education.Controller;

import com.Eonline.Education.modals.OrderRequest;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RazorypayController {
    @Value("${razorpay.api.key}")
    private String razorpayApiKey;
    @Value("${razorpay.api.secret}")
    private String razorpayApiSecret;

    @PostMapping(path = "/create-order")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest){
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey,razorpayApiSecret);
            JSONObject orderRequestJson = new JSONObject();
            orderRequestJson.put("amount", orderRequest.getAmount()); // Amount should be in paise
            orderRequestJson.put("currency", orderRequest.getCurrency());
            orderRequestJson.put("receipt", orderRequest.getReceipt());
            orderRequestJson.put("payment_capture", 1); // Auto-capture payment

            orderRequestJson.put("notes", new JSONObject().put("payment_method", orderRequest.getPaymentMethod()));

            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequestJson);
            return ResponseEntity.ok(razorpayOrder.toString());


        }catch  (RazorpayException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating order: " + e.getMessage());
        }

    }

}
