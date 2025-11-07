package com.martishyn.third_day;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InvokeAnyDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Callable<String> callable = () -> "Callable result 1";
        Callable<String> callable2 = () -> "Callable result 2";
        Callable<String> callable3 = () -> "Callable result 3";

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        final String randomFutureResult = executorService.invokeAny(List.of(callable, callable2, callable3));
        System.out.println(randomFutureResult);

        executorService.shutdown();
    }
}
