package com.example.paymentservice.service;

import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.entity.PaymentMethod;
import com.example.paymentservice.entity.PaymentStatus;
import com.example.paymentservice.exception.InvalidPaymentException;
import com.example.paymentservice.gateway.PaymentGatewayClient;
import com.example.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class PaymentService {

    private PaymentRepository paymentRepository;
    private PaymentGatewayClient paymentGatewayClient;

    public Payment initiataPayment(Payment payment) {
        Payment pay = new Payment();
        pay.setAmount(payment.getAmount());
        pay.setCurrency(payment.getCurrency());
        pay.setPaymentMethod(payment.getPaymentMethod());
        pay.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(pay);
        paymentGatewayClient.initiatePayment(pay);

        return pay;
    }


    public void retryPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();
        paymentGatewayClient.initiatePayment(payment);
    }

    public void cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();
        payment.setStatus(PaymentStatus.CANCELED);
        paymentRepository.save(payment);
    }

    public void refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();
        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);
    }

    private void validatePayment(Payment payment) {
        if (payment == null) {
            throw new NullPointerException("Payment request cannot be null");
        }

        validateAmount(payment.getAmount());
        validateCurrency(payment.getCurrency());
        validatePaymentMethod(payment.getPaymentMethod());
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentException("Amount must be a positive number");
        }
    }

    private void validateCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            throw new InvalidPaymentException("Currency is required");
        }
        if (!isValidCurrency(currency)) {
            throw new InvalidPaymentException("Invalid currency");
        }
    }

    private void validatePaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new InvalidPaymentException("Payment method is required");
        }
        if (!isValidPaymentMethod(paymentMethod)) {
            throw new InvalidPaymentException("Invalid payment method");
        }
    }

    private boolean isValidCurrency(String currency) {
        return Arrays.asList("USD", "EUR", "GBP", "JPY").contains(currency);
    }

    private boolean isValidPaymentMethod(PaymentMethod paymentMethod) {
        return Arrays.asList(PaymentMethod.CREDIT_CARD, PaymentMethod.PAYPAL, PaymentMethod.BANK_TRANSFER).contains(paymentMethod);
    }
}
