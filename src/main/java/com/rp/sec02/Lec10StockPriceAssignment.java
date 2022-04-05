package com.rp.sec02;

import com.rp.sec02.assignment.StockPricePublisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

public class Lec10StockPriceAssignment {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        StockPricePublisher.getPrice()
                .subscribeWith(new Subscriber<Integer>() {

                    //Now we are storing the subscription ins an instance variable, so taht we can cancel it
                    private Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        //This will basically act like an unbounded subscription, so it says, give me all what you have
                        //If this request is not present the flux won't emit any signal
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer price) {
                        System.out.println(LocalDateTime.now() + " : Price : " + price);
                        if(price > 110 || price < 90){
                            this.subscription.cancel();
                            latch.countDown();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        latch.countDown();
                    }

                    @Override
                    public void onComplete() {
                        latch.countDown();
                    }
                });

        latch.await();
    }


}
