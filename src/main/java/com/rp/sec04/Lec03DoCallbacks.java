package com.rp.sec04;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

public class Lec03DoCallbacks {

    public static void main(String[] args) {

        Flux.create(fluxSink -> {
                    System.out.println("inside create");
                    for (int i = 0; i < 5; i++) {
                        fluxSink.next(i);
                    }
                    // fluxSink.complete();
                    fluxSink.error(new RuntimeException("oops"));
                    System.out.println("--completed");
                })
                .doFirst(() -> System.out.println("doFirst"))//This is the first thing that is executed in the pipeline even before the 'create' stage, if there are multiple doFirst the are executed bottom-up, since the subscribe is the first thing
                .doOnSubscribe(s -> System.out.println("doOnSubscribe" + s))//The doOnSubscribe happens top-bottom because it goes from the publisher to the subscriber once the subscription initiated
                .doOnRequest(l -> System.out.println("doOnRequest : " + l))//This is executed when the request is sent, in this case this subscriber is requesting for everything, so it will be executed
                .doOnNext(o -> System.out.println("doOnNext : " + o))//Will be executed whenever the next sink is called
                .doOnCancel(() -> System.out.println("doOnCancel"))//This will be called for example when we use a take that will only take the elements until certain point
                .doOnComplete(() -> System.out.println("doOnComplete"))//When the flux completes this is executed
                .doOnError(err -> System.out.println("doOnError : " + err.getMessage()))//When there's an error this is executed
                .doOnTerminate(() -> System.out.println("doOnTerminate"))//This is like a do on complete
                .doFinally(signal -> System.out.println("doFinally 1 : " + signal))//Regardless of if there's and error or the flux completes, this do-hook is executed (in this case this is executed before the complete)
                .doOnDiscard(Object.class, o -> System.out.println("doOnDiscard : " + o)) // This is called when we dispose something, for example when we use the take, and those methods that were not received are discarded/disposed
                .take(2)//
                .doFinally(signal -> System.out.println("doFinally 2 : " + signal))//in this case this is executed after the complete, because of the take has already been completed
                .subscribe(Util.subscriber());


    }

}
