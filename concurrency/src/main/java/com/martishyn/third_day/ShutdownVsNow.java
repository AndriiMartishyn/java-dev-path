package com.martishyn.third_day;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ShutdownVsNow {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        pool.submit(() -> {
            try {
                System.out.println("Task started");
                Thread.sleep(5000);
                System.out.println("Task finished");
            } catch (InterruptedException e) {
                System.out.println("Task interrupted");
            }
        });

        Thread.sleep(500); // Let it start

        pool.shutdown(); // graceful
      //  pool.shutdownNow(); // forceful

        boolean terminated = pool.awaitTermination(1, TimeUnit.SECONDS);
        System.out.println("awaitTermination returned: " + terminated);
        System.out.println("isTerminated(): " + pool.isTerminated());
    }
}
