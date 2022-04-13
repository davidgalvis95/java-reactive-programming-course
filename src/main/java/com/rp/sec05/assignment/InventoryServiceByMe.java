package com.rp.sec05.assignment;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class InventoryServiceByMe {

    private static Map<String, Integer> productInventory = new HashMap<>();

    public InventoryServiceByMe(){
        productInventory.put("Milk", 30);
        productInventory.put("Bread", 30);
    }

    public Consumer<PurchaseOrder1> setNewInventory(){
        return order -> {
            productInventory.put(order.getItem(), productInventory.get(order.getItem())-order.getQuantity());
            System.out.println("INVENTORY: " + productInventory.toString());
        };
    }

    public static int getInventoryForProduct(String key){
        return productInventory.get(key);
    }

    public static Map<String, Integer> getInventories(){
        return productInventory;
    }

    public static int getAllProductsInventories(){
        AtomicInteger totalInventory = new AtomicInteger();
        productInventory.values().forEach(totalInventory::addAndGet);
        return totalInventory.get();
    }

}
