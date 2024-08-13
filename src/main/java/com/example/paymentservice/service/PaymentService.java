package com.example.paymentservice.service;

import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.entity.PaymentStatus;
import com.example.paymentservice.gateway.PaymentGatewayClient;
import com.example.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private PaymentRepository paymentRepository;
    private PaymentGatewayClient paymentGatewayClient;

    public Payment createPayment(Payment payment) {
        Payment pay = new Payment();
        pay.setAmount(payment.getAmount());
        pay.setCurrency(payment.getCurrency());
        pay.setPaymentMethod(payment.getPaymentMethod());
        pay.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(pay);
        paymentGatewayClient.initiatePayment(pay);

        return pay;
    }

    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow();
    }

    public Payment updatePayment(Long paymentId, Payment payment) {
        Payment pay = getPayment(paymentId);
        pay.setStatus(payment.getStatus());
        paymentRepository.save(pay);
        return pay;
    }

    public List<Payment> getPaymentsForUser(Long userId) {
        return paymentRepository.findByUserId(userId);
    }
}
