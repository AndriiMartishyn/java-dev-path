package com.martishyn.first_second_recap;

public class MultithreadingBasicsRecap3 {

    private static class FlagHolder {

        private volatile boolean running = false;

        public void stop() {
            running = false;
        }

        public boolean isRunning() {
            return running;
        }

        public void start() {
            running = true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        FlagHolder flagHolder = new FlagHolder();
        flagHolder.start();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                if (flagHolder.isRunning()) {
                    System.out.println("hello");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            flagHolder.stop();
            System.out.println("FlagHolder stopped");
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }


}
