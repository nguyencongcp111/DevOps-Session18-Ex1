package com.storex.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotNull Long orderId,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotBlank String method
) {
}
