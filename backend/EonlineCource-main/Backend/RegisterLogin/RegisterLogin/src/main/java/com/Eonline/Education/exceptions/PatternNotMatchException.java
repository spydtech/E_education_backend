package com.Eonline.Education.exceptions;

public class PatternNotMatchException extends RuntimeException{
    public PatternNotMatchException(String message) {
        super(message);
    }

    public PatternNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
