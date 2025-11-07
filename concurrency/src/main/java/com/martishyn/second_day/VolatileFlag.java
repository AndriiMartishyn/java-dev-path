package com.martishyn.second_day;

public class VolatileFlag implements Runnable {

    volatile boolean running = true;

    @Override
    public void run() {
        System.out.println("Worker started..." + Thread.currentThread().getName());
        while (running) {
            System.out.println("Worker is waiting " + Thread.currentThread().getName());
        }
        System.out.println("Worker stopped. " + Thread.currentThread().getName());
    }

    public void stop() {
        running = false;
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileFlag worker = new VolatileFlag();
        Thread t1 = new Thread(worker);
        Thread t2 = new Thread(worker);
        t1.start();
        Thread.sleep(1000);

        worker.stop();
        t2.start();
        System.out.println("Main thread requested stop.");

    }
}
