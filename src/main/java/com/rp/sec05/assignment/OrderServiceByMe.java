package com.rp.sec05.assignment;

import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class OrderServiceByMe {

    private Flux<PurchaseOrder1> orders;

    //If this method is not dode then it will create a new flux for each subscriber, so the refCount(2) will never be achieved, because never will be 2 subscribers
    public Flux<PurchaseOrder1> generateOrders1() {
        if (Objects.isNull(orders)) {
            orders = generateOrders();
        }
        return orders;
    }

    private Flux<PurchaseOrder1> generateOrders() {
        return Flux.generate(
                        InventoryServiceByMe::getInventories,
                        this::processOrderAndGetNewInventory)
                .map(p -> (Pair<PurchaseOrder1, Map<String, Integer>>) p)
                .delayElements(Duration.ofMillis(100))
                .doOnNext(this::validateOrderAgainstInventory)
                .map(Pair::getLeft)
                .onErrorContinue(e -> e instanceof RuntimeException, (e, o) -> {
                    ((Pair<PurchaseOrder1, Integer>) o).getLeft().setQuantity(0);
                    System.out.println(e.getMessage());
                })
                .publish()
                .refCount(2);
    }

    private Map<String, Integer> processOrderAndGetNewInventory(final Map<String, Integer> inventories, final SynchronousSink<Object> sink) {
        final Map<String, Integer> inventoriesSnapshot = new HashMap<>(inventories);
        Map<String, Integer> inventoriesUpdated = new HashMap<>();
        if (countAllInventories(inventoriesSnapshot) == 0) {
            System.out.println("INVENTORY DEPLETED: wait until refilled to process more orders");
            sink.complete();
        } else {
            inventoriesUpdated = createOrderSendToPipelineAndUpdateInventory(sink, inventoriesSnapshot);
        }
        return inventoriesUpdated;
    }

    private Map<String, Integer> createOrderSendToPipelineAndUpdateInventory(SynchronousSink<Object> sink, Map<String, Integer> inventoriesSnapshot) {
        final PurchaseOrder1 order = new PurchaseOrder1();
        System.out.println("ORDER TO PROCESS:" + order);
        sink.next(Pair.of(order, inventoriesSnapshot));
        return reduceProductInventory(order.getItem(), order.getQuantity(), inventoriesSnapshot);
    }


    private void validateOrderAgainstInventory(Pair<PurchaseOrder1, Map<String, Integer>> p) {
        final PurchaseOrder1 order = p.getLeft();
        final int currentInventoryForProduct = getFeasibleQuantityBasedOnInventory(order.getItem(), order.getQuantity(), p.getRight());
        if(currentInventoryForProduct == 0){
            throw new RuntimeException("NO INVENTORY: product -> " + order.getItem() + ". Cancelling order.");
        }
        if (currentInventoryForProduct < order.getQuantity()) {
            order.setQuantity(currentInventoryForProduct);
            System.out.printf("NOT ENOUGH INVENTORY: Sending only %d the available units of %s%n", order.getQuantity(), order.getItem());
        }
    }

    public int getFeasibleQuantityBasedOnInventory(String item, Integer amountNeeded, Map<String, Integer> inventories) {
        int currentInventory = inventories.get(item);
        if (currentInventory <= 0) {
            return 0;
        } else if (currentInventory - amountNeeded < 0) {
            return currentInventory;
        } else {
            return amountNeeded;
        }
    }

    private int countAllInventories(Map<String, Integer> inventories) {
        AtomicInteger count = new AtomicInteger();
        inventories.values().forEach(count::addAndGet);
        return count.get();
    }

    public Map<String, Integer> reduceProductInventory(String item, int quantity, Map<String, Integer> inventories) {
        Map<String, Integer> inventories2 = new HashMap<>(inventories);
        inventories2.put(item, inventories.get(item) - quantity);
        return inventories2;
    }
}
