package com.Eonline.Education.exceptions;

public class MandatoryFieldNotFoundException extends RuntimeException{
    public MandatoryFieldNotFoundException(String message) {
        super(message);
    }

    public MandatoryFieldNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
