package com.martishyn.forth_day;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo1 {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Worker worker = new Worker(countDownLatch);
        Waiter waiter = new Waiter(countDownLatch);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(waiter);
        for (int i = 0; i < 4; i++) {
            executorService.submit(worker);
        }
        executorService.shutdown();
    }

    private record Worker(CountDownLatch countDownLatch) implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " before countdown, latch=" + countDownLatch.getCount());
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName() + " after countdown, latch=" + countDownLatch.getCount());

        }
    }

    private record Waiter(CountDownLatch countDownLatch) implements Runnable {

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Waiter released " + Thread.currentThread().getName());

        }
    }
}
