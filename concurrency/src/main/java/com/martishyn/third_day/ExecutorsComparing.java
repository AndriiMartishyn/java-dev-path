package com.martishyn.third_day;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorsComparing {

    public static void main(String[] args) {
        Runnable task =  () ->  {
            try {
                Thread.sleep(200);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        measure("FixedThreadPool", Executors.newFixedThreadPool(4), task, 100);
        measure("CachedTreadPool", Executors.newCachedThreadPool(), task, 100); //better for short term tasks

    }

    private static void measure(String poolName, ExecutorService executorService, Runnable task, int threadsCount) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < threadsCount; i++) {
            executorService.submit(task);
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);

        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(poolName + " took " + (end - start) + " ms");
    }
}
