package com.odeal.vendingmachine.guard;

import com.odeal.vendingmachine.VendingMachineHelper;
import com.odeal.vendingmachine.Voucher;
import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class EnoughCashCollectedGuard  implements Guard<States, Events> {

    @Autowired
    private VendingMachineHelper vendingMachineHelper;

    @Override
    public boolean evaluate(StateContext<States, Events> stateContext) {
        Voucher voucher = vendingMachineHelper.getVoucher(stateContext.getStateMachine());
        return voucher.getPaid() >= voucher.getCost();
    }
}
