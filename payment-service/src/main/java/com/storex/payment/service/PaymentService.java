package com.storex.payment.service;

import com.storex.payment.dto.CreatePaymentRequest;
import com.storex.payment.dto.PaymentResponse;
import com.storex.payment.gateway.GatewayClient;
import com.storex.payment.gateway.PaymentGatewayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private static final Set<String> SUPPORTED_METHODS = Set.of("CARD", "WALLET", "BANK_TRANSFER", "BROKEN_GATEWAY");
    private static final BigDecimal FRAUD_LIMIT = new BigDecimal("50000000");

    private final GatewayClient gateway;

    public PaymentService(GatewayClient gateway) {
        this.gateway = gateway;
    }

    public PaymentResponse process(CreatePaymentRequest request) {
        String paymentId = UUID.randomUUID().toString();
        String method = request.method().toUpperCase();

        log.debug("[{}] Received payment request orderId={} amount={} method={}",
                paymentId, request.orderId(), request.amount(), method);

        if (!SUPPORTED_METHODS.contains(method)) {
            log.warn("[{}] Unsupported payment method={}", paymentId, request.method());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unsupported payment method: " + request.method());
        }

        log.debug("[{}] Running fraud check, limit={}", paymentId, FRAUD_LIMIT);
        if (request.amount().compareTo(FRAUD_LIMIT) > 0) {
            log.warn("[{}] Payment declined by fraud check orderId={} amount={}",
                    paymentId, request.orderId(), request.amount());
            return new PaymentResponse(paymentId, request.orderId(), "DECLINED", "Amount exceeds allowed limit");
        }

        try {
            gateway.charge(paymentId, method, request.amount());
        } catch (PaymentGatewayException e) {
            // Truyen exception lam tham so cuoi de stack trace vao field stack_trace trong JSON
            log.error("[{}] Gateway error orderId={}", paymentId, request.orderId(), e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Payment gateway error");
        }

        log.info("[{}] Payment approved orderId={} amount={} method={}",
                paymentId, request.orderId(), request.amount(), method);
        return new PaymentResponse(paymentId, request.orderId(), "APPROVED", "Payment successful");
    }
}
