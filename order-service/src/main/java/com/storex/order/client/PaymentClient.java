package com.storex.order.client;

import com.storex.order.dto.PaymentRequest;
import com.storex.order.dto.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

/**
 * Goi sang payment-service de thanh toan don hang.
 * Loi ket noi hoac 4xx/5xx duoc nem ra duoi dang RestClientException.
 */
@Component
public class PaymentClient {

    private static final Logger log = LoggerFactory.getLogger(PaymentClient.class);

    private final RestClient restClient;

    public PaymentClient(RestClient.Builder builder,
                         @Value("${storex.payment-service.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public PaymentResponse pay(Long orderId, BigDecimal amount, String method) {
        log.debug("Calling payment-service orderId={} amount={} method={}", orderId, amount, method);
        return restClient.post()
                .uri("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PaymentRequest(orderId, amount, method))
                .retrieve()
                .body(PaymentResponse.class);
    }
}
