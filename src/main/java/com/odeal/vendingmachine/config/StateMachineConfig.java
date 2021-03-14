package com.odeal.vendingmachine.config;

import com.odeal.vendingmachine.action.*;
import com.odeal.vendingmachine.guard.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;


@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<States, Events> {

    @Autowired
    StateMachineFactory<States,Events> factory;

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates()
                .initial(States.PRODUCT_SELECTION, initialAction())
                .states(EnumSet.allOf(States.class))
                .choice(States.QUANTITY_SELECTED)
                .choice(States.CASH_RECEIVED)
                .choice(States.PAYMENT_COMPLETE)
                .end(States.VOUCHER_PRINTED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                  .withExternal()
                        .source(States.PRODUCT_SELECTION)
                        .target(States.QUANTITY_SELECTION)
                        .event(Events.SELECT_PRODUCT)
                        .action(selectProductAction())
                        .guard(checkProductGuard())
                  .and()
                  .withExternal()
                        .source(States.QUANTITY_SELECTION)
                        .target(States.QUANTITY_SELECTED)
                        .event(Events.SELECT_QUANTITY)
                        .action(selectQuantityAction())
                  .and()
                  /* If Hot Beverage Go to Sugar Selection*/
                  .withChoice()
                        .source(States.QUANTITY_SELECTED)
                        .first(States.SUGAR_SELECTION, sugarQuantityGuard())
                        .last(States.WAITING_FOR_PAYMENT)
                  .and()
                  /**/
                  .withExternal()
                        .source(States.SUGAR_SELECTION)
                        .target(States.WAITING_FOR_PAYMENT)
                        .event(Events.SELECT_SUGAR)
                        .action(selectSugarQuantityAction())
                  .and()

                  /* Cash Payment*/
                  .withExternal()
                        .source(States.WAITING_FOR_PAYMENT)
                        .target(States.CASH_RECEIVED)
                        .event(Events.PAY_CASH)
                        .action(cashPaymentAction())
                        .guard(validCashPaymentGuard())
                  .and()

                  .withChoice()
                        .source(States.CASH_RECEIVED)
                        .first(States.PAYMENT_COMPLETE,enoughCashCollectedGuard())
                        .last(States.WAITING_FOR_MORE_CASH)
                  .and()
                  .withExternal()
                        .source(States.WAITING_FOR_MORE_CASH)
                        .target(States.CASH_RECEIVED)
                        .event(Events.PAY_CASH)
                        .action(cashPaymentAction())
                        .guard(validCashPaymentGuard())
                  .and()
                  /**/

                  /*  Credit Card Payment */
                  .withExternal()
                      .source(States.WAITING_FOR_PAYMENT)
                      .target(States.PAYMENT_COMPLETE)
                      .event(Events.PAY_CC)
                      .action(creditCardPaymentAction())
                      .guard(validCreditCardPaymentGuard())
                  .and()
                  /**/

                  /*Go to REFUNDABLE state If There is something to refund else PRINTABLE state*/
                  .withChoice()
                      .source(States.PAYMENT_COMPLETE)
                      .first(States.REFUNDABLE,refundableGuard())
                      .last(States.PRINTABLE)
                  .and()
                  /**/

                  /*Refund*/
                  .withExternal()
                      .source(States.REFUNDABLE)
                      .target(States.PRINTABLE)
                      .event(Events.REFUND)
                  .and()
                  /**/

                  /* Print Voucher*/
                  .withExternal()
                      .source(States.PRINTABLE)
                      .target(States.VOUCHER_PRINTED)
                      .event(Events.PRINT_VOUCHER);
                  /**/
    }

    @Bean
    StateMachine<States,Events> stateMachine(){
        StateMachine<States,Events> machine = factory.getStateMachine("vendingMachine");
        machine.start();
        return machine;
    }


    /* Actions */
    @Bean
    Action<States,Events> initialAction(){
        return new InitialAction();
    }

    @Bean
    Action<States,Events> selectProductAction(){
        return new SelectProductAction();
    }

    @Bean
    Action<States,Events> selectQuantityAction(){
        return new SelectQuantityAction();
    }

    @Bean
    Action<States,Events> selectSugarQuantityAction(){
        return new SelectSugarQuantityAction();
    }

    @Bean
    Action<States,Events> cashPaymentAction(){
        return new CashPaymentAction();
    }

    @Bean
    Action<States,Events> creditCardPaymentAction(){ return new CreditCardPaymentAction(); }


    /* Guards */
    @Bean
    public Guard<States, Events> checkProductGuard() {
        return new CheckProductGuard();
    }

    @Bean
    public Guard<States, Events> sugarQuantityGuard() {
        return new SugarQuantityGuard();
    }

    @Bean
    public Guard<States, Events> validCashPaymentGuard() {
        return new ValidCashPaymentGuard();
    }

    @Bean
    public Guard<States, Events> validCreditCardPaymentGuard() {
        return new ValidCreditCardPaymentGuard();
    }

    @Bean
    public Guard<States, Events> enoughCashCollectedGuard() {
        return new EnoughCashCollectedGuard();
    }

    @Bean
    public Guard<States, Events> refundableGuard() {
        return new RefundableGuard();
    }
}
