package com.martishyn.week3;

import java.util.Comparator;
import java.util.List;

public class Transaction {

    String id;
    String user;
    double amount;

    public Transaction(String id, String user, double amount) {
        this.id = id;
        this.user = user;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String getUser() {
        return user;
    }

    public static double calculateSumOfTransactions(List<? extends Transaction> transactions){
        return transactions.stream()
                .map(transaction -> transaction.amount)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public static String findUserWithBiggestTransaction(List<? extends Transaction> transactions) {
        return transactions.stream().max(Comparator.comparing(Transaction::getAmount))
                .map(Transaction::getUser)
                .orElse(null);
    }
}
