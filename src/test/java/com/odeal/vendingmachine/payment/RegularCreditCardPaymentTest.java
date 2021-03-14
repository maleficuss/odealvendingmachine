package com.odeal.vendingmachine.payment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RegularCreditCardPaymentTest {


    private final RegularCreditCardPayment regularCreditCardPayment = new RegularCreditCardPayment();

    @Test
    void when_getName_return_simple_name() {
        assertEquals(regularCreditCardPayment.getName(),RegularCreditCardPayment.class.getSimpleName());
    }

    @Test
    void when_pay_do_not_throw() {
        assertDoesNotThrow(() -> regularCreditCardPayment.pay(22222222,66));
    }
}