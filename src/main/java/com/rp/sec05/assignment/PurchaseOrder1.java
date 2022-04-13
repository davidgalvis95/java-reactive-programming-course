package com.rp.sec05.assignment;

import com.rp.courseutil.Util;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PurchaseOrder1 {

    private String item;
    private double price;
    private String category;
    private int quantity;

    public PurchaseOrder1() {
        this.item = Math.round(Math.random()) == 0? "Milk": "Bread";
        this.price = Double.parseDouble(Util.faker().commerce().price());
        this.category = Util.faker().commerce().department();
        this.quantity = Util.faker().random().nextInt(1, 10);
    }
}
