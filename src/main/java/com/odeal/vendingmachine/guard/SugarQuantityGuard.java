package com.odeal.vendingmachine.guard;

import com.odeal.vendingmachine.Product;
import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import com.odeal.vendingmachine.VendingMachineHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class SugarQuantityGuard implements Guard<States, Events> {

    @Autowired
    private VendingMachineHelper vendingMachineHelper;

    @Override
    public boolean evaluate(StateContext<States, Events> stateContext) {
        return vendingMachineHelper.getVoucher(stateContext.getStateMachine()).getProduct().getType() == Product.Type.HOT_BEVERAGE;
    }
}
