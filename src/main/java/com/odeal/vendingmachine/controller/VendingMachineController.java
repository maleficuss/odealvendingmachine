package com.odeal.vendingmachine.controller;

import com.odeal.vendingmachine.EventRequest;
import com.odeal.vendingmachine.Product;
import com.odeal.vendingmachine.VendingMachineHelper;
import com.odeal.vendingmachine.Voucher;
import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import com.odeal.vendingmachine.payment.CashPayment;
import com.odeal.vendingmachine.payment.CreditCardPayment;
import com.odeal.vendingmachine.payment.PaymentMethod;
import com.odeal.vendingmachine.service.VendingMachineService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping("/vendingmachine")
public class VendingMachineController {

    private final StateMachine<States, Events> machine;
    private final VendingMachineService vendingMachineService;
    private final VendingMachineHelper vendingMachineHelper;
    private final List<CashPayment> cashPaymentList;
    private final List<CreditCardPayment> creditCardPaymentList;

    public VendingMachineController(
            StateMachine<States, Events> machine,
            VendingMachineService vendingMachineService,
            VendingMachineHelper vendingMachineHelper,
            List<CashPayment> cashPaymentList,
            List<CreditCardPayment> creditCardPaymentList
    ) {
        this.machine = machine;
        this.vendingMachineService = vendingMachineService;
        this.vendingMachineHelper = vendingMachineHelper;
        this.cashPaymentList = cashPaymentList;
        this.creditCardPaymentList = creditCardPaymentList;
    }

    private Voucher getVoucher(){
        return vendingMachineHelper.getVoucher(machine);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> products(){
        return ResponseEntity.ok(vendingMachineService.getProducts());
    }

    @GetMapping("/paymentMethods")
    public ResponseEntity<Map<String,List<String>>> paymentMethods(){
        Map<String,List<String>> paymentMethods = new HashMap<>();
        paymentMethods.put("cash",cashPaymentList.stream().map(PaymentMethod::getName).collect(Collectors.toList()));
        paymentMethods.put("creditCard",creditCardPaymentList.stream().map(PaymentMethod::getName).collect(Collectors.toList()));
        return ResponseEntity.ok(paymentMethods);
    }

    @PutMapping
    public ResponseEntity<Void> vendingMachine(@RequestBody EventRequest eventRequest){
        MessageBuilder<Events> messageBuilder = MessageBuilder.withPayload(eventRequest.getEvent());

        for (Map.Entry<String, String> entry : eventRequest.getParams().entrySet()) {
            messageBuilder.setHeader(entry.getKey(),entry.getValue());
        }

        if (machine.sendEvent(messageBuilder.build())) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.unprocessableEntity().build();

    }

    @GetMapping("/voucher")
    public ResponseEntity<Voucher> printVoucher(){

        if (machine.sendEvent(Events.PRINT_VOUCHER)) {
            return ResponseEntity.ok(getVoucher());
        }
        return ResponseEntity.unprocessableEntity().build();

    }

}
