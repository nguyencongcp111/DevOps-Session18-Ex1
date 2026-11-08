package com.storex.payment.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;

/**
 * Gia lap cong thanh toan ben ngoai. Nam trong package con com.storex.payment.gateway
 * de thay viec dat level cho com.storex.payment ap dung ca cho package con.
 */
@Component
public class GatewayClient {

    private static final Logger log = LoggerFactory.getLogger(GatewayClient.class);

    public void charge(String paymentId, String method, BigDecimal amount) {
        log.debug("[{}] Connecting to payment gateway method={}", paymentId, method);
        simulateLatency();

        if ("BROKEN_GATEWAY".equals(method)) {
            log.debug("[{}] Gateway did not respond in time", paymentId);
            throw new PaymentGatewayException(
                    "Gateway timeout while charging " + amount,
                    new SocketTimeoutException("Read timed out"));
        }

        log.debug("[{}] Gateway accepted charge amount={}", paymentId, amount);
    }

    private void simulateLatency() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
