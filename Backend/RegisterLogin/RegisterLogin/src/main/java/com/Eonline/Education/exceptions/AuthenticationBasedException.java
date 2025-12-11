package com.Eonline.Education.exceptions;

public class AuthenticationBasedException extends RuntimeException{
    public AuthenticationBasedException(String message) {
        super(message);
    }

    public AuthenticationBasedException(String message, Throwable cause) {
        super(message, cause);
    }
}
