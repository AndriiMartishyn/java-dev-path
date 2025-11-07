package com.martishyn.first_second_recap;

import java.util.concurrent.locks.ReentrantLock;

public class MultithreadingBasicsRecap2 {

    private static class UnsafeCounter {
        private int counter;

        public void increment() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }
    }

    private static class SafeCounter {
        private volatile int counter;

        public synchronized void increment() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }
    }

    private static class SynchronizedCounter {
        private ReentrantLock lock = new ReentrantLock();
        private volatile int counter;

        public void increment() {
            lock.lock();
            try {
                counter++;
            } finally {
                lock.unlock();
            }
        }

        public int getCounter() {
            return counter;
        }
    }


    public static void main(String[] args) {
        //UnsafeCounter counter = new UnsafeCounter(); //faster but not synchronized
        //SafeCounter counter = new SafeCounter();//safer but slower
        SynchronizedCounter counter = new SynchronizedCounter(); //safer and faster
        Thread[] threads = new Thread[10];
        System.out.println("Starting threads...");
        var start = System.nanoTime();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100_000; j++) {
                    counter.increment();
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Counter is " + counter.getCounter());
        System.out.println("Done!");
        System.out.println((System.nanoTime() - start) / 1_000_000);
    }
}
