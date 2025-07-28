package com.martishyn.week1.src.comparison;

import java.util.ArrayDeque;
import java.util.Deque;

public class RecentQueryCache {

    Deque<String> recentQueries = new ArrayDeque<>();
    int capacity = 10;


    public void addQuery(String query){
        if (recentQueries.size() >= capacity) {
            recentQueries.removeFirst();
        }
        recentQueries.addLast(query);
    }

    public String getMostRecentQuery(){
        return recentQueries.peekLast();
    }
}
