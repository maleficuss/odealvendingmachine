package com.odeal.vendingmachine.action;

import com.odeal.vendingmachine.VendingMachineHelper;
import com.odeal.vendingmachine.Voucher;
import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import com.odeal.vendingmachine.payment.CreditCardPayment;
import com.odeal.vendingmachine.payment.CreditCardPaymentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class CreditCardPaymentAction implements Action<States, Events> {

    @Autowired
    private CreditCardPaymentFactory creditCardPaymentFactory;

    @Autowired
    private VendingMachineHelper vendingMachineHelper;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        Voucher voucher = vendingMachineHelper.getVoucher(stateContext.getStateMachine());
        String paymentMethod = (String) stateContext.getMessageHeader("method");
        CreditCardPayment creditCardPayment = creditCardPaymentFactory.create(paymentMethod);
        stateContext.getExtendedState().getVariables().put("method", creditCardPayment);
        double ccNumber = Double.parseDouble((String) stateContext.getMessageHeader("ccNumber"));
        double amountToCollect = voucher.getCost();
        creditCardPayment.pay(ccNumber,amountToCollect);
        voucher.addPayment(amountToCollect);
        voucher.setPaymentMethod("creditCard");
    }
}
