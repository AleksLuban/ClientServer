package com.javastart.server;

import java.util.StringJoiner;

public class Account {
    private long id;
    private String name;
    private Bill bill;

    public Account(long id, String name, Bill bill) {
        this.id = id;
        this.name = name;
        this.bill = bill;
    }

    public Account() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Account.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("bill=" + bill)
                .toString();
    }
}
