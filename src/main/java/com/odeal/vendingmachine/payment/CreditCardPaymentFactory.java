package com.odeal.vendingmachine.payment;

public interface CreditCardPaymentFactory {
    CreditCardPayment create(String type);
}
