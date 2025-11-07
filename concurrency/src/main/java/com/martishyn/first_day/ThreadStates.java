package com.martishyn.first_day;

public class ThreadStates {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(3000);
                doSomething();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println(t1.getState() + " " + t1.getName());
        t1.start();
        System.out.println(t1.getState() + " " + t1.getName());
        Thread.sleep(10);
        System.out.println(t1.getState() + " " + t1.getName());
        doSomething();
        System.out.println(t1.getState() + " " + t1.getName());

    }


    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is running...");

        }
    }

    private static synchronized void doSomething() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("Thread name: " + Thread.currentThread().getName());
        System.out.println("We are in doSomething() synchronized method");
    }
}


