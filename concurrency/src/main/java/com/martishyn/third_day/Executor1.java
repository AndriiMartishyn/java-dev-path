package com.martishyn.third_day;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Executor1 {

    private static class SimpleCallable implements Callable<String> {

        @Override
        public String call() throws Exception {
            return "Callable result";
        }
    }

    private static class SimpleRunnable implements Runnable {

        @Override
        public void run()  {
            System.out.println("Runnable result");
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        final Future<?> runnableFuture = executorService.submit(new SimpleRunnable());
        final Future<String> callableFuture = executorService.submit(new SimpleCallable());
        System.out.println(runnableFuture.isDone());
        System.out.println(callableFuture.isDone());
        System.out.println(callableFuture.get());
        System.out.println(runnableFuture.get());

    }
}
