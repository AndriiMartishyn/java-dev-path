package com.martishyn.third_day;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class ExecutorShutdown {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(() -> {
            System.out.println("Task is running");
        });

        executorService.shutdown();
        System.out.println("Executor shutdown > " + executorService.isShutdown());

        try {
            executorService.submit(() -> {
                System.out.println("Second task is in queue");
            });
        } catch (RejectedExecutionException e) {
            System.out.println("Submit after shutdown");

        }

    }
}

