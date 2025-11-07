package com.martishyn.second_day;

public class SharedData1 {

    int counter = 0;

    public void increment() {
            counter++;
    }

    public static void main(String[] args) throws InterruptedException {
        SharedData1 sharedData1 = new SharedData1();
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
