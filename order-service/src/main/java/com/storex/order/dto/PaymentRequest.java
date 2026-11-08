package com.storex.order.dto;

import java.math.BigDecimal;

/** Body gui sang payment-service. */
public record PaymentRequest(Long orderId, BigDecimal amount, String method) {
}
