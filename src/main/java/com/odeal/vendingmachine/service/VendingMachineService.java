package com.odeal.vendingmachine.service;

import com.odeal.vendingmachine.Product;

import java.util.List;

public interface VendingMachineService {
    List<Product> getProducts();
    Product getProductById(int id);
}
