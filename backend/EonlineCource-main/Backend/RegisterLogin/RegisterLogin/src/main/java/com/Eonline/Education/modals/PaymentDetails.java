package com.Eonline.Education.modals;


import com.Eonline.Education.user.PaymentMethod;
import com.Eonline.Education.user.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentDetails {

    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String paymentId;
    private String razorpayPaymentLinkId;
    private String razorpayPaymentLinkReferenceId;
    private String razorpayPaymentLinkStatus;
    private String razorpayPaymentId;

    public PaymentDetails() {
        // TODO Auto-generated constructor stub
    }


}

