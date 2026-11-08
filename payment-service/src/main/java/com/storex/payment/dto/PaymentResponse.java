package com.storex.payment.dto;

public record PaymentResponse(
        String paymentId,
        Long orderId,
        String status,
        String message
) {
}
