package com.martishyn.week2;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskTests {

    private List<Task> getSampleTasks() {
        return List.of(
                new Task("1", "Fix bug", 1, "Alice", LocalDate.of(2025, 8, 10)),
                new Task("2", "Write tests", 3, "Bob", LocalDate.of(2025, 8, 12)),
                new Task("3", "Deploy app", 2, "Alice", LocalDate.of(2025, 8, 15)),
                new Task("4", "Update docs", 5, "Charlie", LocalDate.of(2025, 8, 20)),
                new Task("5", "Code review", 2, "Bob", LocalDate.of(2025, 8, 11)),
                new Task("6", "Fix login", 1, "Diana", LocalDate.of(2025, 8, 9)),
                new Task("7", "Refactor service", 4, "Charlie", LocalDate.of(2025, 8, 18)),
                new Task("8", "Optimize query", 3, "Alice", LocalDate.of(2025, 8, 14)),
                new Task("9", "Improve UI", 6, "Diana", LocalDate.of(2025, 8, 25)),
                new Task("10", "Setup CI", 2, "Bob", LocalDate.of(2025, 8, 13))
        );
    }

    @Test
    void testComparatorSorting() {
        List<Task> tasks = new ArrayList<>(getSampleTasks());
        tasks.sort(Task.taskComparator());

        for (int i = 1; i < tasks.size(); i++) {
            assertTrue(tasks.get(i - 1).getPriority() <= tasks.get(i).getPriority(),
                    "Tasks should be sorted by priority");
        }
    }

    @Test
    void testGetSortedAndOrderedTasks_UniqueByTitle() {
        List<Task> tasks = new ArrayList<>(getSampleTasks());
        tasks.add(new Task("11", "Fix bug", 10, "Eve", LocalDate.of(2025, 8, 30)));

        List<Task> uniqueSorted = Task.getSortedAndOrderedTasks(tasks);

        Set<String> seenTitles = new HashSet<>();
        for (Task t : uniqueSorted) {
            assertTrue(seenTitles.add(t.getTitle()), "Titles must be unique in result");
        }
    }

    @Test
    void testGroupTasksByAssignee() {
        Map<String, List<Task>> grouped = Task.groupTasksByAssignee(getSampleTasks());

        assertEquals(4, grouped.size());
        assertTrue(grouped.containsKey("Alice"));
        assertEquals(3, grouped.get("Alice").size());
    }

    @Test
    void testComparableForPriorityQueue() {
        PriorityQueue<Task> pq = new PriorityQueue<>(getSampleTasks());

        Task prev = pq.poll();
        while (!pq.isEmpty()) {
            Task curr = pq.poll();
            assertNotNull(prev);
            assertTrue(prev.getPriority() <= curr.getPriority(),
                    "PriorityQueue must return tasks in ascending priority order");
            prev = curr;
        }
    }
}
