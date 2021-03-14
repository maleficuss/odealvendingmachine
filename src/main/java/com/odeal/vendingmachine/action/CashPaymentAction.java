package com.odeal.vendingmachine.action;

import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.payment.CashPaymentFactory;
import com.odeal.vendingmachine.VendingMachineHelper;
import com.odeal.vendingmachine.Voucher;
import com.odeal.vendingmachine.config.States;
import com.odeal.vendingmachine.payment.CashPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;


public class CashPaymentAction implements Action<States, Events> {

    @Autowired
    private CashPaymentFactory cashPaymentFactory;

    @Autowired
    private VendingMachineHelper vendingMachineHelper;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        String paymentMethod = (String) stateContext.getMessageHeader("method");
        CashPayment cashPayment = cashPaymentFactory.create(paymentMethod);
        stateContext.getExtendedState().getVariables().put("method", cashPayment);
        double amount = Double.parseDouble((String) stateContext.getMessageHeader("amount"));
        cashPayment.collect(amount);
        Voucher voucher = vendingMachineHelper.getVoucher(stateContext.getStateMachine());
        voucher.addPayment(amount);
        voucher.setPaymentMethod("cash");
    }
}
