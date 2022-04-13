package com.rp.sec05;

import com.rp.courseutil.Util;
import com.rp.sec05.assignment.InventoryServiceByMe;
import com.rp.sec05.assignment.OrderServiceByMe;
import com.rp.sec05.assignment.RevenueServiceByMe;

public class Lec06AssignmentDoneByMe {

    public static void main(String[] args) {
        OrderServiceByMe orderServiceByMe = new OrderServiceByMe();
        InventoryServiceByMe inventoryServiceByMe = new InventoryServiceByMe();
        RevenueServiceByMe revenueServiceByMe = new RevenueServiceByMe();
        orderServiceByMe.generateOrders1().subscribe(inventoryServiceByMe.setNewInventory(), (e) -> System.out.println(e));
        orderServiceByMe.generateOrders1().subscribe(revenueServiceByMe.setNewRevenue(), (e) -> System.out.println(e));

        Util.sleepSeconds(60);
    }

}
