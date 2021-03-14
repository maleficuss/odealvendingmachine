package com.odeal.vendingmachine;

import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import com.odeal.vendingmachine.payment.CashPayment;
import com.odeal.vendingmachine.payment.CreditCardPayment;
import com.odeal.vendingmachine.service.VendingMachineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
public class VendingStateMachineTests {

    @Autowired
    private StateMachineFactory<States, Events> factory;
    private StateMachine<States,Events> stateMachine;
    @Autowired
    private VendingMachineService vendingMachineService;

    @Autowired
    private List<CashPayment> cashPaymentList;

    @Autowired
    static List<CreditCardPayment> creditCardPaymentList;

    @Value("${vendingMachine.voucher.name}")
    private String voucherKey;

    private Product existingFoodProduct;
    private Product existingHotBeverageProduct;

    private Message<Events> foodProductSelectMessage;
    private Message<Events> hotBeverageSelectMessage;
    private Message<Events> selectQuantityMessage;
    private Message<Events> selectSugarMessage;

    private final String productIdKey = "productId";
    private final String quantityKey = "quantity";
    private final String sugarKey = "sugarQuantity";
    private final String cashPaymentAmountKey = "amount";
    private final String paymentMethodKey = "method";
    private final String ccNumberKey = "ccNumber";


    @BeforeEach
    public void setup(){
        stateMachine = factory.getStateMachine();
        /* Existing Food Product*/
        int productId = 1;
        this.existingFoodProduct =  vendingMachineService.getProductById(productId);
        /**/

        /* Existing Hot Beverage*/
        existingHotBeverageProduct =vendingMachineService.
                getProducts().stream().
                filter(product -> product.getType().equals(Product.Type.HOT_BEVERAGE)).
                findFirst().orElse(null);
        /**/

        /* Successful FOOD Product SELECT_PRODUCT Message*/
        this.foodProductSelectMessage = MessageBuilder
                .withPayload(Events.SELECT_PRODUCT)
                .setHeader(productIdKey,String.valueOf(this.existingFoodProduct.getId()))
                .build();
        /**/

        /* Successful HOT_BEVERAGE Product SELECT_PRODUCT Message*/
        this.hotBeverageSelectMessage = MessageBuilder
                .withPayload(Events.SELECT_PRODUCT)
                .setHeader(productIdKey,String.valueOf(this.existingHotBeverageProduct.getId()))
                .build();
        /**/

        /* Successful SELECT_QUANTITY Message*/
        this.selectQuantityMessage = MessageBuilder
                .withPayload(Events.SELECT_QUANTITY)
                .setHeader(quantityKey,"3")
                .build();
        /**/

        /* Successful SELECT_SUGAR Message*/
        this.selectSugarMessage = MessageBuilder
                .withPayload(Events.SELECT_SUGAR)
                .setHeader(sugarKey,"2")
                .build();
        /**/



    }


    @Test
    public void test_initial_action(){
        StateMachineTestPlanBuilder<States,Events> builder = StateMachineTestPlanBuilder.<States,Events>builder()
                .stateMachine(stateMachine)
                .step()
                    .expectVariable(voucherKey)
                    .expectState(States.PRODUCT_SELECTION)
                    .and();
        assertDoesNotThrow(() -> builder.build().test());
    }

    @Test
    public void when_select_product_event_change_state_to_with_existing_product_id(){

        StateMachineTestPlanBuilder<States,Events> builder = StateMachineTestPlanBuilder.<States,Events>builder()
                .stateMachine(stateMachine)
                .step()
                .sendEvent(this.foodProductSelectMessage)
                .expectStateChanged(2)
                .expectState(States.QUANTITY_SELECTION)
                .and();
        assertDoesNotThrow(() -> builder.build().test());
    }


    @Test
    public void when_select_product_event_do_not_change_state_with_non_existing_product_id(){

        /* Non-Existing Product*/
        int productId = 955959559;
        Product product = vendingMachineService.getProductById(productId);
        assertNull(product);
        /**/

        Message<Events> message = MessageBuilder
                .withPayload(Events.SELECT_PRODUCT)
                .setHeader(productIdKey,String.valueOf(productId))
                .build();

        StateMachineTestPlanBuilder<States,Events> builder = StateMachineTestPlanBuilder.<States,Events>builder()
                .stateMachine(stateMachine)
                .step()
                    .sendEvent(message)
                    .expectStateChanged(1)
                    .expectState(States.PRODUCT_SELECTION)
                .and();

        assertDoesNotThrow(() -> builder.build().test());
    }

    @Test
    public void when_select_quantity_event_for_non_hot_beverage_product_change_to_waiting_for_payment(){

        StateMachineTestPlanBuilder<States,Events> builder = StateMachineTestPlanBuilder.<States,Events>builder()
            .stateMachine(stateMachine)
            .step()
                .sendEvent(foodProductSelectMessage)
                .sendEvent(selectQuantityMessage)
                .expectStateChanged(3)
                .expectState(States.WAITING_FOR_PAYMENT)
            .and();

        assertDoesNotThrow(() -> builder.build().test());
    }

    @Test
    public void when_select_quantity_event_for_hot_beverage_product_change_to_sugar_selection(){

        StateMachineTestPlanBuilder<States,Events> builder = StateMachineTestPlanBuilder.<States,Events>builder()
                .stateMachine(stateMachine)
                .step()
                    .sendEvent(hotBeverageSelectMessage)
                    .sendEvent(selectQuantityMessage)
                    .expectStateChanged(3)
                    .expectState(States.SUGAR_SELECTION)
                .and();

        assertDoesNotThrow(() -> builder.build().test());
    }

    @Test
    public void when_select_sugar_event_change_to_waiting_for_payment(){
        StateMachineTestPlanBuilder<States,Events> builder = StateMachineTestPlanBuilder.<States,Events>builder()
                .stateMachine(stateMachine)
                .step()
                    .sendEvent(hotBeverageSelectMessage)
                    .sendEvent(selectQuantityMessage)
                    .sendEvent(selectSugarMessage)
                    .expectStateChanged(4)
                    .expectState(States.WAITING_FOR_PAYMENT)
                .and();

        assertDoesNotThrow(() -> builder.build().test());
    }


    @Test
    public void when_pay_cash_event_change_to_waiting_for_more_cash_if_payment_not_satisfied(){

        /*Unsatisfied PAY_CASH Message*/
        int productId = Integer.parseInt(String.valueOf(foodProductSelectMessage.getHeaders().get(productIdKey)));
        Product product = vendingMachineService.getProductById(productId);

        int quantity = Integer.parseInt(String.valueOf(selectQuantityMessage.getHeaders().get(quantityKey)));

        Message<Events> payCashMessage = MessageBuilder
                .withPayload(Events.PAY_CASH)
                .setHeader(paymentMethodKey,"BanknotePayment")
                /* -1 For Quantity so payment will not be unsatisfied*/
                .setHeader(cashPaymentAmountKey,String.valueOf(product.getPrice() * (quantity - 1)))
                .build();
        /**/




        StateMachineTestPlanBuilder<States,Events> builder = StateMachineTestPlanBuilder.<States,Events>builder()
                .stateMachine(stateMachine)
                .step()
                    .sendEvent(hotBeverageSelectMessage)
                    .sendEvent(selectQuantityMessage)
                    .sendEvent(selectSugarMessage)
                    .sendEvent(payCashMessage)
                    .expectStateChanged(5)
                    .expectState(States.WAITING_FOR_MORE_CASH)
                .and();

        assertDoesNotThrow(() -> builder.build().test());
    }


    @Test
    public void when_pay_cash_event_if_total_payment_more_than_cost_go_refundable(){

        /*Unsatisfied PAY_CASH Message*/
        /*Send Same message 2 times, so payment will be satisfied*/
        int productId = Integer.parseInt(String.valueOf(foodProductSelectMessage.getHeaders().get(productIdKey)));
        Product product = vendingMachineService.getProductById(productId);

        int quantity = Integer.parseInt(String.valueOf(selectQuantityMessage.getHeaders().get(quantityKey)));


        /* Test it For All CashPayment Implementations*/

        Message<Events> payCashMessage = MessageBuilder
                .withPayload(Events.PAY_CASH)
                .setHeader(paymentMethodKey,"BanknotePayment")
                /* -1 For Price so payment will not be unsatisfied At First PAY_CASH event*/
                .setHeader(cashPaymentAmountKey,String.valueOf(product.getPrice() * (quantity - 1)))
                .build();
        /**/


        StateMachineTestPlanBuilder<States,Events> builder = StateMachineTestPlanBuilder.<States,Events>builder()
                .stateMachine(stateMachine)
                .step()
                    .sendEvent(foodProductSelectMessage)
                    .sendEvent(selectQuantityMessage)
                    .sendEvent(selectSugarMessage)
                    .sendEvent(payCashMessage)
                    .sendEvent(payCashMessage)
                    .expectStateChanged(5)
                    .expectState(States.REFUNDABLE)
                .and();

        assertDoesNotThrow(() -> builder.build().test());
        /**/
    }


    @Test
    public void when_pay_cash_event_if_total_payment_same_with_cost_go_printable(){

        /*Unsatisfied PAY_CASH Message*/
        /*Send Same message 2 times, so payment will be satisfied*/
        int productId = Integer.parseInt(String.valueOf(foodProductSelectMessage.getHeaders().get(productIdKey)));
        Product product = vendingMachineService.getProductById(productId);

        int quantity = Integer.parseInt(String.valueOf(selectQuantityMessage.getHeaders().get(quantityKey)));

        Message<Events> payCashMessage = MessageBuilder
                .withPayload(Events.PAY_CASH)
                .setHeader(paymentMethodKey,"BanknotePayment")
                /* -1 For Price so payment will not be unsatisfied At First PAY_CASH event*/
                .setHeader(cashPaymentAmountKey,String.valueOf(product.getPrice() * quantity))
                .build();
        /**/


        StateMachineTestPlanBuilder<States,Events> builder = StateMachineTestPlanBuilder.<States,Events>builder()
                .stateMachine(stateMachine)
                .step()
                .sendEvent(foodProductSelectMessage)
                .sendEvent(selectQuantityMessage)
                .sendEvent(selectSugarMessage)
                .sendEvent(payCashMessage)
                .expectStateChanged(4)
                .expectState(States.PRINTABLE)
                .and();

        assertDoesNotThrow(() -> builder.build().test());
    }


    @Test
    public void when_pay_cc_event_go_to_printable(){
        Message<Events> payCCMessage = MessageBuilder
                .withPayload(Events.PAY_CC)
                .setHeader(paymentMethodKey,"ContactlessCreditCardPayment")
                .setHeader(ccNumberKey,"1231231231")
                .build();
        /**/

        StateMachineTestPlanBuilder<States,Events> builder = StateMachineTestPlanBuilder.<States,Events>builder()
                .stateMachine(stateMachine)
                .step()
                .sendEvent(foodProductSelectMessage)
                .sendEvent(selectQuantityMessage)
                .sendEvent(selectSugarMessage)
                .sendEvent(payCCMessage)
                .expectStateChanged(4)
                .expectState(States.PRINTABLE)
                .and();

        assertDoesNotThrow(() -> builder.build().test());
    }



}
