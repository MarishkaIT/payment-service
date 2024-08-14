package com.example.paymentservice.entity;

import lombok.Getter;

@Getter
public enum PaymentMethod {

    CREDIT_CARD("Credit card"),
    PAYPAL("PayPal"),
    BANK_TRANSFER("Bank Transfer"),
    APPLE_PAY("Apple Pay"),
    GOOGLE_PAY("Google Pay"),
    OTHER("Other");
    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

}
