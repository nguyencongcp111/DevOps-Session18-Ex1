package com.storex.order.dto;

/** Phan hoi tu payment-service. */
public record PaymentResponse(String paymentId, Long orderId, String status, String message) {
}
