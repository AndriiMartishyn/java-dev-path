package com.martishyn.third_day;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InvokeDemo {
    public static void main(String[] args) throws InterruptedException {
        Callable<String> callable = () -> "Callable result 1";
        Callable<String> callable2 = () -> "Callable result 2";
        Callable<String> callable3 = () -> "Callable result 3";

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        final List<Future<String>> futures = executorService.invokeAll(List.of(callable, callable2, callable3));

        futures.forEach(future -> {
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        executorService.shutdown();
    }
}
