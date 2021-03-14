package com.odeal.vendingmachine.payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CreditCardPaymentFactoryImplTest {

    @Autowired
    List<CreditCardPayment> creditCardPaymentList;

    @Autowired
    private CreditCardPaymentFactoryImpl creditCardPaymentFactory;

    @Test
    void credit_card_payment_list_not_null(){
        assertNotNull(creditCardPaymentList);
    }

    @Test
    void when_type_exits_return_credit_card_payment() {
        /*Test List Of Implementations*/
        assertEquals(creditCardPaymentList.size(),2);
        assertNotNull(creditCardPaymentFactory.create("RegularCreditCardPayment"));
        assertNotNull(creditCardPaymentFactory.create("ContactlessCreditCardPayment"));
    }

    @Test
    void when_type_do_not_exit_return_null() {
        assertNull(creditCardPaymentFactory.create("nonexitimplentation"));
    }

}