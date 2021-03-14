package com.odeal.vendingmachine.payment;

import org.springframework.stereotype.Component;

@Component
public class ContactlessCreditCardPayment implements CreditCardPayment{

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void pay(double creditCardNumber, double amount) {
        System.out.println("Contactless Credit Card Payment, card number : "+creditCardNumber+" amount : "+amount);
    }
}
