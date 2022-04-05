package com.rp.sec02;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec08FluxInterval {

    public static void main(String[] args) {

        //This flux interval can be useful when we need to poll some data or something periodically
        //like polling data each second or something like that
        Flux.interval(Duration.ofSeconds(1))
                .subscribe(Util.onNext());

        Util.sleepSeconds(5);

    }

}
