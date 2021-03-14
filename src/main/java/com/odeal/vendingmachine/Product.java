package com.odeal.vendingmachine;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Product {

    private final int id;
    private final String name;
    private final double price;
    private final Type type;

    public enum Type{
        FOOD (1),
        COLD_BEVERAGE(2),
        HOT_BEVERAGE(3);

        private final int value;

        Type(int value){
            this.value=value;
        }
    }
}
