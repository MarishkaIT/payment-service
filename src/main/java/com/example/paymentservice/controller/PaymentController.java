package com.example.paymentservice.controller;

import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.entity.PaymentStatus;
import com.example.paymentservice.exception.InvalidPaymentException;
import com.example.paymentservice.service.PaymentQueryService;
import com.example.paymentservice.service.PaymentService;
import com.example.paymentservice.service.PaymentUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private PaymentService paymentService;
    private PaymentUpdateService paymentUpdateService;
    private PaymentQueryService paymentQueryService;

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long paymentId) {
        Payment payment = paymentQueryService.getPayment(paymentId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Payment>> getPaymentHistory(@PathVariable Long userId) {
        List<Payment> payments = paymentQueryService.getPaymentHistory(userId);
        return ResponseEntity.ok(payments);
    }
    @PostMapping
    public ResponseEntity<Payment> initiatePayment(@RequestBody Payment payment){
        try {
            Payment initiatedPayment = paymentService.initiataPayment(payment);
            return ResponseEntity.ok(initiatedPayment);
        } catch (InvalidPaymentException e) {
            return badRequest().body(null);
        }
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long paymentId, @RequestBody Payment payment) {
        Payment pay =  paymentUpdateService.updatePayment(paymentId, payment);
        return ResponseEntity.ok(pay);
    }

    @PostMapping("/{paymentId}/retry")
    public ResponseEntity<Void> retryPayment(@PathVariable Long paymentId) {
        paymentService.retryPayment(paymentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<Void> cancelPayment(@PathVariable Long paymentId) {
        Payment payment = paymentQueryService.getPayment(paymentId);
        paymentUpdateService.updatePaymentStatus(payment, PaymentStatus.CANCELED);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Void> refundPayment(@PathVariable Long paymentId) {
        Payment payment = paymentQueryService.getPayment(paymentId);
        paymentUpdateService.updatePaymentStatus(payment, PaymentStatus.REFUNDED);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentForUser(@PathVariable Long userId) {
        List<Payment> payments = paymentQueryService.getPaymentsForUser(userId);
        return ResponseEntity.ok(payments);
    }
}
