package com.rp.sec05.assignment;

import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RevenueServiceByMe {
    private static Map<String, Integer> revenueByProduct = new HashMap<>();

    public RevenueServiceByMe(){
        revenueByProduct.put("Milk", 0);
        revenueByProduct.put("Bread", 0);
    }

    public Consumer<PurchaseOrder1> setNewRevenue(){
        return order -> {
            revenueByProduct.put(order.getItem(), (int) (revenueByProduct.get(order.getItem())+order.getPrice()));
            System.out.println("REVENUE: " + revenueByProduct.toString());
        };
    }
}
