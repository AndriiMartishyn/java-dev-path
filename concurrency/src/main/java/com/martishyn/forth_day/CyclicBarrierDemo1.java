package com.martishyn.forth_day;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierDemo1 {

    private static final String API_URL = "https://api.restful-api.dev/objects";

    private static final RestTemplate restTemplate = new RestTemplate();

    private static class BarrierRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Last thread performing this runnable " + Thread.currentThread().getName());
            String newObject = """
                    {
                       "name": "Apple MacBook Pro 2025",
                       "data": {
                          "year": 2019,
                          "price": 1849.99,
                          "CPU model": "Intel Core i9",
                          "Hard disk size": "1 TB"
                       }
                    }
                    """;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(newObject, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request , String.class);
            System.out.println("Last object: " + response.getBody());
        }
    }

    private record ApiWorker(CyclicBarrier cyclicBarrier) implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("Fetching api data from " + API_URL + " thread: " + Thread.currentThread().getName());
                final String fetchedData = restTemplate.getForObject(API_URL, String.class);
                System.out.println("Fetched data " + fetchedData);
                Thread.sleep(500);
                System.out.printf("Thread %s reached barrier.%n", Thread.currentThread().getName());
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

public static void main(String[] args) {
    int apiCount = 3;
    CyclicBarrier cyclicBarrier = new CyclicBarrier(apiCount, new BarrierRunnable());
    ExecutorService executorService = Executors.newFixedThreadPool(apiCount);
    for (int i = 0; i < apiCount; i++) {
        executorService.submit(new ApiWorker(cyclicBarrier));
    }
    executorService.shutdown();

}
}
