package com.odeal.vendingmachine.payment;

public interface CashPaymentFactory {
    CashPayment create(String type);
}
