package com.martishyn.second_day;

import javax.net.ssl.SSLHandshakeException;
import java.util.LinkedList;
import java.util.Queue;

public class CoordinationExample {

    private static class Shared {

        private boolean isReady = false;

        synchronized void waitForReady() {
            while (!isReady) {
                try {
                    System.out.println("Waiting before start!" + Thread.currentThread().getName());
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Proceeding after ready!" + Thread.currentThread().getName());
        }

        synchronized void setReady() {
            isReady = true;
            System.out.println("notifying");
            notifyAll();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Shared shared = new Shared();

        Thread t1 = new Thread(shared::waitForReady);

        Thread t2 = new Thread(shared::waitForReady);

        Thread notifier = new Thread(shared::setReady);

        t1.start();
        t2.start();
        notifier.start();

        t1.join();
        t2.join();
        notifier.join();

        System.out.println("Main thread is done");
    }
}
