package com.martishyn.first_day;

public class InterleavedNumbersPrinter {

    public static void main(String[] args) throws InterruptedException {
        // Create multiple threads with different speeds
        Thread t1 = new NumberPrinter("FastThread", 1, 5, 200);
        Thread t2 = new NumberPrinter("MediumThread", 1, 5, 400);
        Thread t3 = new NumberPrinter("SlowThread", 1, 5, 600);

        // Start all threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for all to complete
        t1.join();
        t2.join();
        t3.join();

        System.out.println("All threads completed!");
    }

    private static class NumberPrinter extends Thread {

        private final String name;
        private final int start;
        private final int end;
        private final int delay;

        private NumberPrinter(String name, int start, int end, int delay) {
            this.name = name;
            this.start = start;
            this.end = end;
            this.delay = delay;
        }

        @Override
        public void run() {
            for (int i = start; i <= end; i++) {
                System.out.println(getName() + " → " + i);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(getName() + " finished.");
        }
    }
}
