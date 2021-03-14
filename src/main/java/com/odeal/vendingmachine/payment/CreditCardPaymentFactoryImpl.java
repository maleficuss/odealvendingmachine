package com.odeal.vendingmachine.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreditCardPaymentFactoryImpl implements CreditCardPaymentFactory{

    private final List<CreditCardPayment> payments;

    @Autowired
    public CreditCardPaymentFactoryImpl(List<CreditCardPayment> payments) {
        this.payments = payments;
    }

    @Override
    public CreditCardPayment create(String type) {
        return payments.stream().filter(creditCardPayment -> creditCardPayment.getName().equals(type)).findFirst().orElse(null);
    }
}
