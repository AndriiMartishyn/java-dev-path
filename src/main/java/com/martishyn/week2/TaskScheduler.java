package com.martishyn.week2;

import java.util.List;
import java.util.PriorityQueue;

public class TaskScheduler<Task> {
    private final PriorityQueue<Task> priorityQueue;


    public TaskScheduler(List<Task> list) {
        this.priorityQueue = new PriorityQueue<>(list);
    }

    public Task processNextTask(){
         Task polledTask = priorityQueue.poll();
         priorityQueue.remove(polledTask);
         return polledTask;
    }
}
