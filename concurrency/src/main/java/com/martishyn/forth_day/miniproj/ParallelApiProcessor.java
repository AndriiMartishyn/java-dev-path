package com.martishyn.forth_day.miniproj;

import com.martishyn.forth_day.ExchangerDemo;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ParallelApiProcessor {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final List<String> urls = List.of(
            "https://api.restful-api.dev/objects/1",
            "https://api.restful-api.dev/objects/2",
            "https://api.restful-api.dev/objects/3",
            "https://api.restful-api.dev/objects/4",
            "https://api.restful-api.dev/objects/5"
    );

    private static final int WORKERS_COUNT = 5;
    private static final BlockingQueue<String> fetchedQueue = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> parsedQueue = new LinkedBlockingQueue<>();
    private static final Queue<List<String>> finalMergedResults = new ConcurrentLinkedQueue<>();



    private record FetchWorker(int taskNumber, String url, CountDownLatch countDownLatch) implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("Task number : " + taskNumber + " going to fetch " + url + " by " + Thread.currentThread().getName());
                String fetchedData = restTemplate.getForObject(url, String.class);
                countDownLatch.countDown();
                System.out.println("Task number : " + taskNumber + " fetched " + fetchedData + " by " + Thread.currentThread().getName());
                if (fetchedData != null) {
                    fetchedQueue.put(fetchedData);
                }
                System.out.println("Task number : " + taskNumber + " put fetched data to queue by " + Thread.currentThread().getName());
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private record ParseWorker(Semaphore semaphore) implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("Worker " + Thread.currentThread().getName() + " is acquiring semaphore");
                semaphore.acquire();
                String takenFromQueue = fetchedQueue.take();
                Thread.sleep(300);
                String changedObject = takenFromQueue.replaceAll("name", "productName").replaceAll("data", "productData");
                System.out.println("Worker " + Thread.currentThread().getName() + " changed object: " + changedObject + " and put it to queue");
                parsedQueue.put(changedObject);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
            }
        }
    }

    private record MergeWorker(Exchanger<List<String>> exchanger, CyclicBarrier barrier) implements Runnable {

        @Override
        public void run() {
            try {
                List<String> myChunk = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    myChunk.add(parsedQueue.take());
                }
                System.out.println("Worker " + Thread.currentThread().getName() + " prepared 2 chunks of data: " + myChunk);
                List<String> receivedData = exchanger.exchange(myChunk);
                System.out.println("Worker " + Thread.currentThread().getName() + " received data: " + receivedData);
                myChunk.addAll(receivedData);
                System.out.println("Worker " + Thread.currentThread().getName() + " merged results: " + myChunk);
                finalMergedResults.add(myChunk);
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(5);
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        for (int i = 0; i < WORKERS_COUNT; i++) {
            executorService.submit(new FetchWorker(i, urls.get(i), countDownLatch));
        }
        try {
            countDownLatch.await(); // we wait there until fetchers will done their work
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("-----Fetched APIs----");

        final Semaphore semaphore = new Semaphore(2);
        for (int i = 0; i < WORKERS_COUNT; i++) {
            executorService.submit(new ParseWorker(semaphore));
        }
        Exchanger<List<String>> exchanger = new Exchanger<>();
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {

            System.out.println("Final merge is done");
        });

        executorService.submit(new MergeWorker(exchanger, barrier));
        executorService.submit(new MergeWorker(exchanger, barrier));

        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("ALL DONE. Final results:");
        System.out.println(finalMergedResults);
    }
}
