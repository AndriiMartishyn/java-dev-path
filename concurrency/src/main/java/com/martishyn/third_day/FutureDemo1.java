package com.martishyn.third_day;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureDemo1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            final String taskName = "Task " + i;
           futures.add(executorService.submit(() -> {
               try {
                   Thread.sleep(2000);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
                return taskName + " is done!";
           }));
        }
        for (Future<String> future : futures) {
            System.out.println("Future -> " + future.get());
        }
        executorService.shutdownNow();
        System.out.println("All tasks are done!");
    }
}
