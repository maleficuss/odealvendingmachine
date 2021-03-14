package com.odeal.vendingmachine.service;

import com.odeal.vendingmachine.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class VendingMachineServiceImpl implements VendingMachineService{

    private final List<Product> products = new ArrayList<>();

    public VendingMachineServiceImpl() {
        int foodPrice = 8;
        int foodIncrement = 4;

        int hotBeveragePrice = 10;
        int hotBeverageIncrement = 6;

        int coldBeveragePrice = 5;
        int coldBeverageIncrement = 5;

        int id = 1;

        for (int i = 0; i < 20; i++) {
            products.add(new Product(id,"Food "+(i + 1),foodPrice, Product.Type.FOOD));
            foodPrice+=foodIncrement;
            id++;
        }

        for (int i = 0; i < 5; i++) {
            products.add(new Product(id,"Cold Beverage "+(i + 1),coldBeveragePrice, Product.Type.COLD_BEVERAGE));
            coldBeveragePrice+=coldBeverageIncrement;
            id++;
        }

        for (int i = 0; i < 5; i++) {
            products.add(new Product(id,"Hot Beverage "+(i + 1),hotBeveragePrice, Product.Type.HOT_BEVERAGE));
            hotBeveragePrice+=hotBeverageIncrement;
            id++;
        }
    }

    @Override
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    @Override
    public Product getProductById(int id) {
        return products.stream().filter(product -> product.getId() == id).findFirst().orElse(null);
    }
}
