package com.odeal.vendingmachine;

import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
public class VendingMachineHelper {

    private String voucherKey;

    @Value("${vendingMachine.voucher.name}")
    public void setVoucherKey(String voucherKey) {
        this.voucherKey = voucherKey;
    }

    public Voucher getVoucher(StateMachine<States, Events> machine) {
        return machine.getExtendedState().get(voucherKey,Voucher.class);
    }

    public void setVoucher(StateMachine<States, Events> machine, Voucher voucher) {
        machine.getExtendedState().getVariables().put(voucherKey,voucher);
    }
}
