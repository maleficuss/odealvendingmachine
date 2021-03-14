package com.odeal.vendingmachine.payment;

import org.springframework.stereotype.Component;

@Component
public class BanknotePayment implements CashPayment {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void collect(double amount) {
        System.out.println("Banknote collected : "+amount);
    }
}
