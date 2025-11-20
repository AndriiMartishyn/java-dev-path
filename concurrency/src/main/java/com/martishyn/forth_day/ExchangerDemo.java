package com.martishyn.forth_day;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Exchanger;

public class ExchangerDemo {

    private static final String API_URL_1 = "https://api.restful-api.dev/objects/1";

    private static final String API_URL_2 = "https://api.restful-api.dev/objects/2";

    private static final Exchanger<String> exchanger = new Exchanger<>();

    private static final RestTemplate restTemplate = new RestTemplate();

    private record ApiWorker(String url) implements Runnable {

        @Override
        public void run() {
            try{
                System.out.println(Thread.currentThread().getName() + " fetching " + url);
                String result = restTemplate.getForObject(url, String.class);
                System.out.println(Thread.currentThread().getName() + " has " + result + " for exchange");
                Thread.sleep(500);

                System.out.println(Thread.currentThread().getName() + " waiting for exchange ");
                String received = exchanger.exchange(result);

                System.out.println(Thread.currentThread().getName() +
                        " received data: " + received);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) {
        Thread t1 = new Thread(new ApiWorker(API_URL_1));
        Thread t2 = new Thread(new ApiWorker( API_URL_2));
        t1.start();
        t2.start();
    }
}
