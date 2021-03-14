package com.odeal.vendingmachine;

import com.odeal.vendingmachine.config.Events;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EventRequest {
    private Events event;
    private Map<String, String> params = new HashMap<>();
}
