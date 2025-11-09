package com.martishyn.third_day;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorDemo {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new java.util.concurrent.ThreadPoolExecutor(2,
                4,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2),
                new ThreadPoolExecutor.DiscardPolicy());


        for (int i = 0; i < 10; i++) {
            int taskId = i;
            threadPoolExecutor.execute(() -> {
                System.out.println("Task " + taskId + " is started by " + Thread.currentThread().getName());
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Task " + taskId + " is finished by " + Thread.currentThread().getName());
            });
            System.out.println("Pool size: " + threadPoolExecutor.getPoolSize() +
                    ", Active: " + threadPoolExecutor.getActiveCount() +
                    ", Queue size: " + threadPoolExecutor.getQueue().size());
        }
        threadPoolExecutor.shutdown();

        //task 1 -> thread 1 -> sleep
        // task 2 -> thread 2 -> sleep
        // queue is empty -> task 3 -> thread are busy -> put in QUEUE (1)
        // queue is not full -> task 4 -> thread are busy -> put in QUEUE(2)
        //queue is FULL -> task 5 -> create new 3rd thread -> sleep
        // ququq is full -> task 6 -> create new 4th tread -> sleep
        //task 7 / 8 / 9 /10 -> rejected because pool is full as per policy
    }
}
