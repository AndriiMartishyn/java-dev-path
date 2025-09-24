package com.martishyn.productapi.exceptions;

public class PaymentNotFoundException extends RuntimeException{

    public PaymentNotFoundException(String message) {
        super(message);
    }
}
