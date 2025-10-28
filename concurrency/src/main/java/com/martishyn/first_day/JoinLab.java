package com.martishyn.first_day;

public class JoinLab {

    public static void main(String[] args) throws InterruptedException {
        doJoinLogic();
    }

    static void doJoinLogic() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("T1 is done");

        });


        Thread t2 = new Thread(() -> {
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("T2 is done after waiting for T1");

        });

        System.out.println("Main thread is waiting for t1 and t2 to start");
        System.out.println("Starting t1 and t2");
        System.out.println("Main thread state " + Thread.currentThread().getState());
        System.out.println("T1 thread state " + t1.getState());
        System.out.println("T2 thread state " + t2.getState());
        System.out.println("============");

        t2.start();
        t1.start();

        System.out.println("[Main] Waiting for t1 and t2 to finish...");
        System.out.println("T1 thread state " + t1.getState());
        System.out.println("T2 thread state " + t2.getState());
        System.out.println("============");
        t1.join(); // main waits until t1 is done
        t2.join(); // then waits until t2 is done
        System.out.println("Main thread state after join " + Thread.currentThread().getState());
        System.out.println("T1 thread state after join"  + t1.getState());
        System.out.println("T2 thread state after join " + t2.getState());
        System.out.println("============");


        System.out.println("[Main] All threads finished!");


    }

}
