package com.storex.order.dto;

import jakarta.validation.constraints.NotBlank;

public record PayOrderRequest(@NotBlank String method) {
}
