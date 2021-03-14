package com.odeal.vendingmachine.action;

import com.odeal.vendingmachine.VendingMachineHelper;
import com.odeal.vendingmachine.Voucher;
import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class SelectSugarQuantityAction implements Action<States, Events> {

    @Autowired
    private VendingMachineHelper vendingMachineHelper;

    @Override
    public void execute(StateContext<States, Events> stateContext) {

        Voucher voucher = vendingMachineHelper.getVoucher(stateContext.getStateMachine());

        int sugarQuantity = Integer.parseInt((String) stateContext.getMessageHeader("sugarQuantity"));
        voucher.setSugarQuantity(sugarQuantity);
    }
}
