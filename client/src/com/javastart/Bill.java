package com.javastart;

import java.util.StringJoiner;

public class Bill {
    private int amount;

    public Bill(int amount) {
        this.amount = amount;
    }

    public Bill() {

    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Bill.class.getSimpleName() + "[", "]")
                .add("amount=" + amount)
                .toString();
    }
}
