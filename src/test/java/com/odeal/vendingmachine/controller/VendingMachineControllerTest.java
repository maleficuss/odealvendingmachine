package com.odeal.vendingmachine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeal.vendingmachine.EventRequest;
import com.odeal.vendingmachine.Product;
import com.odeal.vendingmachine.VendingMachineHelper;
import com.odeal.vendingmachine.Voucher;
import com.odeal.vendingmachine.config.Events;
import com.odeal.vendingmachine.config.States;
import com.odeal.vendingmachine.payment.CashPayment;
import com.odeal.vendingmachine.payment.CashPaymentFactory;
import com.odeal.vendingmachine.payment.CreditCardPayment;
import com.odeal.vendingmachine.payment.CreditCardPaymentFactory;
import com.odeal.vendingmachine.service.VendingMachineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VendingMachineController.class)
class VendingMachineControllerTest {

    @MockBean
    private StateMachine<States, Events> machine;
    @MockBean
    private VendingMachineService vendingMachineService;
    @MockBean
    private VendingMachineHelper vendingMachineHelper;
    @MockBean
    private List<CashPayment> cashPaymentList;
    @MockBean
    private List<CreditCardPayment> creditCardPaymentList;
    @MockBean
    private StateMachineFactory<States,Events> factory;
    @MockBean
    private CashPaymentFactory cashPaymentFactory;
    @MockBean
    private CreditCardPaymentFactory creditCardPaymentFactory;

    @InjectMocks
    private VendingMachineController vendingMachineController;

    @Autowired
    MockMvc mockMvc;


    @BeforeEach
    void setup(){

    }

    @Test
    public void test_product_list() throws Exception {
        mockMvc.perform(get("/vendingmachine/products")).andExpect(status().isOk());
    }

    @Test
    public void test_payment_method_list() throws Exception {
        mockMvc.perform(get("/vendingmachine/paymentMethods")).andExpect(status().isOk());
    }

    private String toJson(EventRequest eventRequest) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(eventRequest);
    }

    @Test
    public void when_put_event_request_return_no_content() throws Exception {

        Map<String,String> params = new HashMap<>();
        params.put("productId","1");
        EventRequest eventRequest = new EventRequest();
        eventRequest.setEvent(Events.SELECT_PRODUCT);
        eventRequest.setParams(params);

        when(machine.sendEvent(any(Message.class))).thenReturn(true);


        mockMvc.perform(
                put("/vendingmachine")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(eventRequest))
        ).andExpect(status().isNoContent());
    }

    @Test
    public void when_send_print_voucher_event_request_return_created() throws Exception {

        EventRequest eventRequest = new EventRequest();
        eventRequest.setEvent(Events.PRINT_VOUCHER);

        Voucher voucher = new Voucher();
        voucher.setProduct(new Product(1,"fake product",12.00,Product.Type.HOT_BEVERAGE));

        when(machine.sendEvent(Events.PRINT_VOUCHER)).thenReturn(true);
        when(vendingMachineHelper.getVoucher( any(StateMachine.class))).thenReturn(voucher);
        MvcResult mvcResult = mockMvc.perform(
                get("/vendingmachine/voucher")
        )
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        assertEquals(mapper.writeValueAsString(voucher),mvcResult.getResponse().getContentAsString());

    }

}