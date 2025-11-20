package com.martishyn.forth_day;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreApiDemo2 {

    private static Semaphore parkingSemaphore;

    private record CarAsThread(int taskNumber) implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("Car " + taskNumber + " is going to park...");
                parkingSemaphore.acquire();
                System.out.println("Car " + taskNumber + " is parked....");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            } finally {
                System.out.println("Car " + taskNumber + " is leaving parking lot...");
                parkingSemaphore.release();
            }
        }
    }

    public static void main(String[] args) {
        int parkingLots = 5;
        parkingSemaphore = new Semaphore(parkingLots, true);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
         for (int i = 0; i < 10; i++) {
             int task = i;
             executorService.submit(() -> {
              new CarAsThread(task).run();
             });
         }
         executorService.shutdown();
         }
}
