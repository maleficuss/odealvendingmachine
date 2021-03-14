package com.odeal.vendingmachine.payment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ContactlessCreditCardPaymentTest {


    private final ContactlessCreditCardPayment contactlessCreditCardPayment = new ContactlessCreditCardPayment();

    @Test
    void when_getName_return_simple_name() {
        assertEquals(contactlessCreditCardPayment.getName(),ContactlessCreditCardPayment.class.getSimpleName());
    }

    @Test
    void when_pay_do_not_throw() {
        assertDoesNotThrow(() -> contactlessCreditCardPayment.pay(123123123,55));
    }
}