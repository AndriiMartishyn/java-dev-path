package com.martishyn.first_day;

public class Threads1 {

    public static void main(String[] args) {

        Thread t1 = new Thread(new Task1());

        t1.run();

        t1.start();

        Thread t2 = new Task2();

        Thread t3 = new Task2();

        t2.start();

        t3.start();
    }


    private static class Task1 implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is running...");

        }
    }

    private static class Task2 extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is running...");
        }
    }
}


