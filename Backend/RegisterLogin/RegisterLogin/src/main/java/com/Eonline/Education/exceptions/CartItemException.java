package com.Eonline.Education.exceptions;

public class CartItemException extends Exception {
    String message;

    public CartItemException(String message) {
        this.message = message;
    }
}
