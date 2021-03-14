package com.odeal.vendingmachine.payment;

public interface CreditCardPayment extends PaymentMethod {
    void pay(double creditCardNumber, double amount);
}
