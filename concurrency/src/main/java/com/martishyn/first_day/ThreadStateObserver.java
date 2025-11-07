package com.martishyn.first_day;

public class ThreadStateObserver {

    public static void main(String[] args) throws InterruptedException {
        // Step 1: Define the worker thread
        Thread worker = new Thread(() -> {
            System.out.println("[Worker] Started work...");
            try {
                // Sleep simulates waiting for something (e.g., I/O, timer)
                Thread.sleep(1000);
                System.out.println("[Worker] Woke up, doing more work...");
                Thread.sleep(1000);
                System.out.println("[Worker] Done working!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Step 2: Print initial state
        System.out.println("[Main] Initial state: " + worker.getState()); // NEW

        // Step 3: Start the thread
        worker.start();
        System.out.println("[Main] After start(): " + worker.getState()); // RUNNABLE (likely)

        // Step 4: Periodically observe the state
        while (worker.isAlive()) {
            Thread.State state = worker.getState();
            System.out.println("[Main] Observing state: " + state);

            // Sleep a bit between observations (otherwise it prints too fast)
            Thread.sleep(300);
        }

        // Step 5: After the thread is done
        System.out.println("[Main] Final state: " + worker.getState()); // TERMINATED
        System.out.println("[Main] Observation complete!");
    }
}
