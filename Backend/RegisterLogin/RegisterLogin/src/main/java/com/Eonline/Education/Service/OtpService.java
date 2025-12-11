package com.Eonline.Education.Service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final int USER_ID_LENGTH=3;
    private static final int TICKET_NO_LENGTH=5;
    public String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public String generateUserId() {
        Random random = new Random();
        StringBuilder userId = new StringBuilder();
        for (int i = 0; i < USER_ID_LENGTH; i++) {
            userId.append(random.nextInt(10));
        }
        return userId.toString();
    }

    public String generateTicketNo() {
        Random random = new Random();
        StringBuilder userId = new StringBuilder();
        for (int i = 0; i < TICKET_NO_LENGTH; i++) {
            userId.append(random.nextInt(10));
        }
        return userId.toString();
    }


}