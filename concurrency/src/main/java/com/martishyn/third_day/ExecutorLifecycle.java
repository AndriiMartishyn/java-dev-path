package com.martishyn.third_day;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorLifecycle {

    public static void checkShutdown(ExecutorService service) {
        for (int i = 0; i < 1000; i++) {
            int taskId = i;
            service.submit(() -> {
                System.out.println("Task " + taskId + " started on thread " + Thread.currentThread().getName());
            });
        }
        service.shutdown();
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(3);
        checkShutdown(service);

//            try {
//                if (!service.awaitTermination(5, TimeUnit.SECONDS)) {
//                    System.out.println("Forcing shutdown of service...");
//                    service.shutdownNow();
//                }
//            } catch (InterruptedException e) {
//                service.shutdownNow();
//            }

    }
}
