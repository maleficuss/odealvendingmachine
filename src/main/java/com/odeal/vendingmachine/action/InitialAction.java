package com.odeal.vendingmachine.action;

import com.odeal.vendingmachine.Voucher;
import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import com.odeal.vendingmachine.VendingMachineHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class InitialAction implements Action<States, Events> {

    @Autowired
    private VendingMachineHelper vendingMachineHelper;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        Voucher voucher = new Voucher();
        vendingMachineHelper.setVoucher(stateContext.getStateMachine(),voucher);
        stateContext.getExtendedState().getVariables().put("voucher",voucher);
    }
}
