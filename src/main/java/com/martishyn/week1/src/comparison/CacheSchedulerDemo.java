package com.martishyn.week1.src.comparison;

import java.time.LocalDate;

public class CacheSchedulerDemo {

    public static void main(String[] args) {
        RecentQueryCache recentQueryCache = new RecentQueryCache();

        recentQueryCache.addQuery("Remove");
        recentQueryCache.addQuery("World");
        recentQueryCache.addQuery("!");
        recentQueryCache.addQuery("Goodbye");
        recentQueryCache.addQuery("World");
        recentQueryCache.addQuery("!");
        recentQueryCache.addQuery("Goodbye");
        recentQueryCache.addQuery("World");
        recentQueryCache.addQuery("!");
        recentQueryCache.addQuery("Goodbye");
        recentQueryCache.addQuery("World");
        System.out.println(recentQueryCache.getMostRecentQuery());
        System.out.println(recentQueryCache.recentQueries.getFirst());
        System.out.println(recentQueryCache.recentQueries.getLast());


        System.out.println("===========================\n\n");
        Task task1 = new Task();
        task1.title = "Buy milk";
        task1.scheduledAt = LocalDate.of(2021, 12, 4);
        task1.priority = 1;

        Task task2 = new Task();
        task2.title = "Buy eggs";
        task2.scheduledAt = LocalDate.of(2021, 12, 2);
        task2.priority = 2;

        Task task3 = new Task();
        task3.title = "Buy bread";
        task3.scheduledAt = LocalDate.of(2021, 12, 3);
        task3.priority = 3;

        TaskScheduler taskScheduler = new TaskScheduler();
        taskScheduler.addTask(task1);
        taskScheduler.addTask(task2);
        taskScheduler.addTask(task3);
        System.out.println(taskScheduler.pollNext());
        System.out.println(taskScheduler.pollNext());
        System.out.println(taskScheduler.pollNext());


    }
}
