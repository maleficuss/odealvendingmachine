package com.odeal.vendingmachine.payment;

public interface CashPayment extends PaymentMethod{
    void collect(double amount);
}
