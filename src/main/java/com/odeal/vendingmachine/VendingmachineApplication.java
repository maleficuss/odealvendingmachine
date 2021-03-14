package com.odeal.vendingmachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.config.EnableStateMachine;

@SpringBootApplication
@EnableStateMachine
public class VendingmachineApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendingmachineApplication.class, args);
    }

}
