package com.martishyn.week2;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Task implements Comparable<Task>{
    private final String id;
    private final String title;
    private final int priority; // 1 - highest, 10 - lowest
    private final String assignee;
    private final LocalDate dueDate;

    public Task(String id, String title, int priority, String assignee, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.assignee = assignee;
        this.dueDate = dueDate;
    }

    public static Comparator<? super Task> taskComparator() {
        return Comparator.comparing(Task::getPriority)
                .thenComparing(task -> task.dueDate)
                .thenComparing(task -> task.title);
    }

    public static List<Task> getSortedAndOrderedTasks(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.toMap(
                        Task::getTitle,                // ключ → title
                        task -> task,                  // значення → сам Task
                        (t1, t2) -> t1                  // якщо дублікати → залишаємо перший
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .toList();
    }

    public static Map<String, List<Task>> groupTasksByAssignee(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::getAssignee, Collectors.toList()));
    }

    public String getTitle() {
        return title;
    }

    public int getPriority() {
        return priority;
    }

    public String getAssignee() {
        return assignee;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", priority=" + priority +
                ", assignee='" + assignee + '\'' +
                ", dueDate=" + dueDate +
                '}';
    }

    @Override
    public int compareTo(Task o) {
        return this.priority - o.priority;
    }
}
