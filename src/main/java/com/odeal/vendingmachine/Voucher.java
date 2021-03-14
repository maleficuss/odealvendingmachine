package com.odeal.vendingmachine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Voucher {
    @Getter
    @Setter
    @JsonUnwrapped
    @JsonIgnoreProperties(value = {"id","price","type"})
    private Product product;
    @Getter
    @Setter
    private int quantity;
    @Getter
    @Setter
    private int sugarQuantity;

    @Setter
    @Getter
    private String paymentMethod;

    private double paid;


    public synchronized void addPayment(double payment){
        paid+=payment;
    }

    public double getPaid() {
        return paid;
    }

    public double getCost() {
        return product == null ? 0 : product.getPrice() * getQuantity();
    }


    public double getRefund() {
        double calculated = getPaid() - getCost();
        return calculated < 0 ? 0 : calculated;
    }


}