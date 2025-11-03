package com.martishyn.second_day;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {

    public static void main(String[] args) {
        SharedBuffer sharedBuffer = new SharedBuffer();
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                sharedBuffer.produce(i);
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                sharedBuffer.consume();
            }
        });
        producer.start();
        consumer.start();
    }

    private static class SharedBuffer {

        private Queue<Integer> queue = new LinkedList<>();

        private final int CAPACITY = 2;

        public synchronized void produce(int i) {
            while (queue.size() == CAPACITY) {
                try {
                    System.out.println("Buffer is full, waiting...");
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            queue.add(i);
            System.out.println("Produced " + i);
            notify();
        }

        public synchronized void consume() {
            while (queue.isEmpty()) {
                try {
                    System.out.println("Buffer is empty, waiting");
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            int consumed = queue.poll();
            System.out.println("Consumed " + consumed);
            notify();
        }
    }
}
