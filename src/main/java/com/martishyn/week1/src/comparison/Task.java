package com.martishyn.week1.src.comparison;

import java.time.LocalDate;
import java.util.Comparator;

public class Task  {
    String title;
    LocalDate scheduledAt;
    int priority;

    static Comparator<? super Task> taskDeadlineComparator() {
        return Comparator.comparing(((Task task) -> task.priority)).thenComparing(task -> task.scheduledAt);
    }

    static Comparator<? super Task> taskDeadlineTitleComparator() {
        return Comparator.comparing(((Task task) -> task.scheduledAt)).thenComparing(task -> task.title);
    }

    @Override
    public String toString(){
        return String.format("Task{title='%s', deadline=%s, priority=%d}", title, scheduledAt, priority);
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getScheduledAt() {
        return scheduledAt;
    }

    public int getPriority() {
        return priority;
    }
}
