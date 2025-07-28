package com.martishyn.week1.src.comparison;

import java.util.Comparator;
import java.util.PriorityQueue;

public class TaskScheduler {

    private final PriorityQueue<Task> tasks = new PriorityQueue<>(Comparator.comparing(Task::getScheduledAt));


    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task pollNext(){
        return tasks.poll();
    }
}
