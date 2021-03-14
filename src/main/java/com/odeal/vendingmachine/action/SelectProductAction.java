package com.odeal.vendingmachine.action;

import com.odeal.vendingmachine.Product;
import com.odeal.vendingmachine.VendingMachineHelper;
import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import com.odeal.vendingmachine.service.VendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class SelectProductAction implements Action<States, Events> {

    @Autowired
    private VendingMachineService vendingMachineService;

    @Autowired
    private VendingMachineHelper vendingMachineHelper;

    @Override
    public void execute(StateContext<States, Events> stateContext) {

        int productId = Integer.parseInt((String) stateContext.getMessageHeader("productId"));
        Product product = vendingMachineService.getProductById(productId);
        vendingMachineHelper.getVoucher(stateContext.getStateMachine()).setProduct(product);
    }
}
