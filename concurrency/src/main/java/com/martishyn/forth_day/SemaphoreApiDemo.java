package com.martishyn.forth_day;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreApiDemo {

    private static final String API_URL = "https://api.restful-api.dev/objects";

    private static final RestTemplate restTemplate = new RestTemplate();

    private static final Semaphore semaphore = new Semaphore(3, true);

    private record ApiWorker(int taskNumber) implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("Thread " + taskNumber + " waiting for permit...");
                semaphore.acquire();
                System.out.println("Thread " + taskNumber + " got permit! Requesting...");
                final String fetchedData = restTemplate.getForObject(API_URL, String.class);
                System.out.println("Fetched data from API " + fetchedData);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            } finally {
                System.out.println("Thread " + taskNumber + " releasing permit...");
                semaphore.release();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
         for (int i = 0; i < 10; i++) {
             int task = i;
             executorService.submit(() -> {
                 new ApiWorker(task).run();
             });
         }
         executorService.shutdown();
         }
}
