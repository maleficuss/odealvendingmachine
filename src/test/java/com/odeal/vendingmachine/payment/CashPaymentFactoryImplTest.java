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
class CashPaymentFactoryImplTest {

    @Autowired
    List<CashPayment> cashPaymentList;

    @Autowired
    private CashPaymentFactoryImpl cashPaymentFactory;

    @Test
    void credit_card_payment_list_not_null(){
        assertNotNull(cashPaymentList);
    }

    @Test
    void when_type_exits_return_credit_card_payment() {
        /*Test List Of Implementations*/
        assertEquals(cashPaymentList.size(),2);
        assertNotNull(cashPaymentFactory.create("BanknotePayment"));
        assertNotNull(cashPaymentFactory.create("CoinPayment"));
    }

    @Test
    void when_type_do_not_exit_return_null() {
        assertNull(cashPaymentFactory.create("nonexitimplentation"));
    }

}