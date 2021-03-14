package com.odeal.vendingmachine.payment;

import org.springframework.stereotype.Component;

@Component
public class RegularCreditCardPayment implements CreditCardPayment{

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void pay(double creditCardNumber, double amount) {
        System.out.println("Regular Credit Card Payment, card number : "+creditCardNumber+" amount : "+amount);
    }
}
