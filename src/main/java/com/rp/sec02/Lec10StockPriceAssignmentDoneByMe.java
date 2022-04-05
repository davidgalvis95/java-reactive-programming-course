package com.rp.sec02;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class Lec10StockPriceAssignmentDoneByMe {
    public static void main(String[] args) {
        Flux.interval(Duration.ofSeconds(1))
                .map(tick -> {
//                    System.out.println("Tick: " + tick);
                    return generateStockValue();
                })
                .takeUntil(value -> {
//                    System.out.println("Value: " + value);
                    return value > 15 || value < 6;
                })
                .subscribe(onNext(), Util.onError(), Util.onComplete());

        Util.sleepSeconds(30);
    }

    private static Integer generateStockValue(){
        return (int) Math.floor(Math.random() * 20);
    }

    public static Consumer<Object> onNext(){
        return o -> System.out.println("Stock price at "+ LocalDateTime.now() + ": "  + o);
    }

}
