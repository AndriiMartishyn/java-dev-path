package com.martishyn.second_day;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class SharedData4 {

    int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SharedData4 sharedData1 = new SharedData4();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                sharedData1.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                sharedData1.increment();

            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Counter is " + sharedData1.count);


    }
}
