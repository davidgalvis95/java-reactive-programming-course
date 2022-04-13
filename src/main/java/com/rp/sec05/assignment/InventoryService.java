package com.rp.sec05.assignment;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class InventoryService {

    private Map<String, Integer> db = new HashMap<>();

    public InventoryService(){
        db.put("Kids", 100);
        db.put("Automotive", 100);
    }

    //This is the consumer that is generated when we subscribe to the orders service
    public Consumer<PurchaseOrder> subscribeOrderStream(){
        return p -> db.computeIfPresent(p.getCategory(), (k, v) -> v - p.getQuantity());
    }

    //This is generating a flux each 2 seconds with the updated inventory
    public Flux<String> inventoryStream(){
        return Flux.interval(Duration.ofSeconds(2))
                .map(i -> db.toString());
    }

}
