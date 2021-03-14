package com.odeal.vendingmachine.guard;

import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import com.odeal.vendingmachine.payment.CreditCardPaymentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class ValidCreditCardPaymentGuard implements Guard<States, Events> {

    @Autowired
    private CreditCardPaymentFactory creditCardPaymentFactory;

    @Override
    public boolean evaluate(StateContext<States, Events> stateContext) {

        String paymentMethod = (String) stateContext.getMessageHeader("method");
        return creditCardPaymentFactory.create(paymentMethod) != null;
    }
}
