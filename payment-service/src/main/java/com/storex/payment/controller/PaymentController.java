package com.storex.payment.controller;

import com.storex.payment.dto.CreatePaymentRequest;
import com.storex.payment.dto.PaymentResponse;
import com.storex.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse create(@Valid @RequestBody CreatePaymentRequest request) {
        log.debug("POST /api/payments orderId={}", request.orderId());
        return paymentService.process(request);
    }
}
