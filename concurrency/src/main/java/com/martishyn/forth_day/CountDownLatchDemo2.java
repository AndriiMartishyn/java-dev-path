package com.martishyn.forth_day;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo2 {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        ApiWorker worker = new ApiWorker(countDownLatch, "https://api.example.com/1");
        ApiWaiter waiter = new ApiWaiter(countDownLatch);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(waiter);
        for (int i = 0; i < 4; i++) {
            executorService.submit(worker);
        }
        executorService.shutdown();
    }

    private record ApiWorker(CountDownLatch countDownLatch, String api) implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("Fetching API data from " + api + " thread: " + Thread.currentThread().getName());
                Thread.sleep(Math.round(Math.random() * 2000));
                System.out.println("Fetched data from API " + api + " thread: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    private record ApiWaiter(CountDownLatch countDownLatch) implements Runnable {

        @Override
        public void run() {
            try {
                countDownLatch.await();
                System.out.println("All APIs fetched! Aggregating results...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
