package com.odeal.vendingmachine.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CashPaymentFactoryImpl implements CashPaymentFactory {

    private final List<CashPayment> payments;

    @Autowired
    public CashPaymentFactoryImpl(List<CashPayment> payments) {
        this.payments = payments;
    }

    @Override
    public CashPayment create(String type) {
        return payments.stream().filter(payment -> payment.getClass().getSimpleName().equals(type)).findFirst().orElse(null);
    }
}
