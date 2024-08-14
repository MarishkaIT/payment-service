package com.example.paymentservice.service;

import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.entity.PaymentStatus;
import com.example.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentUpdateService {

    private PaymentRepository paymentRepository;
    private PaymentQueryService paymentQueryService;


    public Payment updatePayment(Long paymentId, Payment payment) {
        Payment pay = paymentQueryService.getPayment(paymentId);
        pay.setStatus(payment.getStatus());
        paymentRepository.save(pay);
        return pay;
    }
    public Payment updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setStatus(status);
        paymentRepository.save(payment);
        return payment;
    }
}
