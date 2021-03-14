package com.odeal.vendingmachine.payment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BanknotePaymentTest {

    private final BanknotePayment banknotePayment = new BanknotePayment();

    @Test
    void when_getName_return_simple_name() {
        assertEquals(banknotePayment.getName(),BanknotePayment.class.getSimpleName());
    }

    @Test
    void when_collect_do_not_throw() {
        assertDoesNotThrow(() -> banknotePayment.collect(55));
    }
}