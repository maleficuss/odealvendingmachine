package com.odeal.vendingmachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VoucherTest {

    private Voucher voucher;

    @BeforeEach
    void setup(){
        voucher = new Voucher();
    }

    @Test
    public void when_paid_more_than_cost_reflect_to_refund(){
        Product product = new Product(1,"Fake Product",12.00,Product.Type.FOOD);
        int quantity = 3;
        double payment = 100.00;
        voucher.setProduct(product);
        voucher.setQuantity(quantity);
        voucher.addPayment(payment);
        double expect = payment - (product.getPrice() * quantity);
        assertEquals(expect,voucher.getRefund());
    }

    @Test
    public void when_quantity_set_calculate_cost(){
        Product product = new Product(1,"Fake Product",12.00,Product.Type.FOOD);
        int quantity = 3;
        voucher.setProduct(product);
        voucher.setQuantity(quantity);

        double except = product.getPrice() * quantity;

        assertEquals(except,voucher.getCost());


    }

}