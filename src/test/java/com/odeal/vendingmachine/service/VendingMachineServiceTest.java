package com.odeal.vendingmachine.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineServiceTest {

    private final VendingMachineServiceImpl vendingMachineService = new VendingMachineServiceImpl();

    @Test
    void when_get_products_return_list() {
        assertTrue(vendingMachineService.getProducts().size() > 0);
    }

    @Test
    void when_product_id_exits_return_product() {
        assertNotNull(vendingMachineService.getProductById(1));
    }

    @Test
    void when_product_id_does_not_exit_return_product() {
        assertNull(vendingMachineService.getProductById(9999999));
    }
}