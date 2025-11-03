package com.martishyn.second_day;

import java.util.concurrent.locks.ReentrantLock;

public class CounterDemo {

    static class CounterService {
        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    static class ReentrantCounterService {
        private int count = 0;

        private final ReentrantLock lock = new ReentrantLock(false);

        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }

        public int getCount() {
            return count;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        // CounterService counterService = new CounterService();
        ReentrantCounterService counterService = new ReentrantCounterService();
        Runnable task = () -> {
            for (int i = 0; i < 100_000; i++) {
                counterService.increment();
            }
        };
        Thread[] threads = new Thread[10];
        var start = System.nanoTime();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        var end = System.nanoTime();
        System.out.println("Time: " + (end - start) / 1_000_000);
        System.out.println(counterService.getCount());

    }
}
