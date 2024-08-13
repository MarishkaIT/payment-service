package com.example.paymentservice.gateway;

import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.entity.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class PaymentGatewayClient {

    @Value("${payment.gateway.url}")
    private String paymentGatewayUrl;

    private final OkHttpClient httpClient;

    public PaymentGatewayClient() {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public void initiatePayment(Payment payment) {
        try {
            Request request = new Request.Builder()
                    .url(paymentGatewayUrl + "/initiate-payment")
                    .post(RequestBody.create(MediaType.get("application/json"), payment.toString()))
                    .build();

            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                log.info("Payment initiated successfully for payment ID {}", payment.getId());
            }else {
                log.error("Error initiated payment for payment ID {}: {}", payment.getId(), response.code());
            }
        } catch (IOException e) {
            log.error("Error initiated payment for payment ID {}: {}", payment.getId(), e.getMessage());
        }
    }

    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        try {
            Request request = new Request.Builder()
                    .url(paymentGatewayUrl + "/update-payment-status")
                    .put(RequestBody.create(MediaType.get("/application/json"), payment.toString()))
                    .build();
            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                log.info("Payment initiated successfully for payment ID {}", payment.getId());
            }else {
                log.error("Error initiated payment for payment ID {}: {}", payment.getId(), response.code());
            }
        } catch (IOException e) {
            log.error("Error initiated payment for payment ID {}: {}", payment.getId(), e.getMessage());
        }
    }
}
