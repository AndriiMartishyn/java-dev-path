package com.martishyn.first_second_recap;


public class MultithreadingBasicsRecap {

    static void firstTask() throws InterruptedException { //interleaving
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.printf("Thread %d: %d\n", Thread.currentThread().getId(), i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.printf("Thread %d: %d\n", Thread.currentThread().getId(), i);
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static void secondTask() { //join -> waiting other thread until completion
        Runnable firstTask = () -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(200);
                    System.out.println("Thread " + Thread.currentThread().getId() + " working before second thread");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread = new Thread(firstTask);
        Thread thread2 = new Thread(() -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println("Done");
            }
        });
        thread.start();
        thread2.start();
    }


    public static void main(String[] args) throws InterruptedException {
        firstTask();
        secondTask();

    }
}
