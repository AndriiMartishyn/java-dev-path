package com.martishyn.third_day;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ParallelTaskProcessor {

    private static final int TASKS_COUNT = 20;

    private static class Job implements Callable<String> {

        private final int id;

        public Job(int id) {
            this.id = id;
        }

        /**
         * There we will simulate real job (like api calls, files processing, etc)
         * with sleep
         **/
        @Override
        public String call() {
            long start = System.currentTimeMillis();
            try {
                Thread.sleep(200 + new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            return "Job " + "with id " + id + " is completed in " + (end - start) + " ms";
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ExecutorService executorService2 = Executors.newCachedThreadPool();
        ExecutorService executorService3 = new ThreadPoolExecutor(
                4, 5, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));

        executeOnThreadPool(executorService, "fixed");
        executeOnThreadPool(executorService2, "cached");
        executeOnThreadPool(executorService3, "custom");

    }

    private static void executeOnThreadPool(ExecutorService executorService, String executorType) throws InterruptedException, ExecutionException {
        System.out.println("Starting... on " + executorType);
        long start = System.currentTimeMillis();
        for (int i = 0; i < TASKS_COUNT; i++) {
            executorService.submit(new Job(i));
            Future<String> submitedtask = executorService.submit(new Job(i));
           System.out.println(submitedtask.get());


        }
        executorService.shutdown();
        System.out.println("Finished in " + (System.currentTimeMillis() - start) + " ms");
    }
}
