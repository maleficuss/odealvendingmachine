package com.odeal.vendingmachine.action;

import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import com.odeal.vendingmachine.VendingMachineHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class SelectQuantityAction implements Action<States, Events> {


    @Autowired
    private VendingMachineHelper vendingMachineHelper;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        int quantity = Integer.parseInt((String) stateContext.getMessageHeader("quantity"));
        vendingMachineHelper.getVoucher(stateContext.getStateMachine()).setQuantity(quantity);
    }
}
