package com.odeal.vendingmachine.payment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CoinPaymentTest {

    private final CoinPayment coinPayment = new CoinPayment();

    @Test
    void when_getName_return_simple_name() {
        assertEquals(coinPayment.getName(),CoinPayment.class.getSimpleName());
    }

    @Test
    void when_collect_do_not_throw() {
        assertDoesNotThrow(() -> coinPayment.collect(66));
    }
}