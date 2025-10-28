package com.martishyn.second_day;

import java.util.concurrent.atomic.AtomicInteger;

public class SharedData3 {

    AtomicInteger atomicInteger = new AtomicInteger(0);


    public static void main(String[] args) throws InterruptedException {
        SharedData3 sharedData1 = new SharedData3();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                sharedData1.atomicInteger.incrementAndGet();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                sharedData1.atomicInteger.incrementAndGet();

            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Counter is " + sharedData1.atomicInteger.get());


    }
}
