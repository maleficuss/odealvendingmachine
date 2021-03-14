package com.odeal.vendingmachine.guard;

import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import com.odeal.vendingmachine.service.VendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class CheckProductGuard implements Guard<States, Events> {

    @Autowired
    private VendingMachineService vendingMachineService;

    @Override
    public boolean evaluate(StateContext<States, Events> stateContext) {
        int productId = Integer.parseInt((String) stateContext.getMessageHeader("productId"));
        return vendingMachineService.getProductById(productId) != null;
    }
}
