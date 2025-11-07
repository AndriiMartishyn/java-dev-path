package com.martishyn.third_day;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorShutdownNow {
    public static void main(String[] args) throws InterruptedException {
//        final ExecutorService executorService = Executors.newFixedThreadPool(2);
//        for (int i = 0; i < 3; i++) {
//            int taskId = i;
//            executorService.submit(() -> {
//                System.out.println("Task " + taskId + " started on thread " + Thread.currentThread().getName());
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                System.out.println("Task is exiting on thread " + Thread.currentThread().getName() + "");
//            });
//
//        }
//        Thread.sleep(1000);
//        System.out.println("Calling shutdownNow()");
//        final List<Runnable> pendingRunnables = executorService.shutdownNow();
//        System.out.println("Tasks not completed: " + pendingRunnables.size() + "");

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            try{
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Task is running");
        });
        System.out.println("Before shutdownNow() > " );
        System.out.println("Shutdown "  + executorService.isShutdown());
        System.out.println("Terminated " + executorService.isTerminated());
        executorService.shutdownNow();

        System.out.println("After shutdownNow() > " );
        System.out.println("After shutdownNow > " + executorService.isShutdown());
        System.out.println("After Terminated " + executorService.isTerminated());

        final boolean b = executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        System.out.println("is terminated method result " + b);
        System.out.println("After awaitTermination() > " + executorService.isShutdown());
        System.out.println("After awaitTermination() > " + executorService.isTerminated());


    }
}
