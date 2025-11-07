package com.martishyn.second_day;

public class SharedData2 {

    int counter = 0;

    public synchronized void increment() {
            counter++;
    }

    public static void main(String[] args) throws InterruptedException {
        SharedData2 sharedData1 = new SharedData2();
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

        System.out.println("Counter is " + sharedData1.counter);


    }
}
