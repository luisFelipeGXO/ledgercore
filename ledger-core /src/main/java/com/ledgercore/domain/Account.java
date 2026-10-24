package com.ledgercore.domain;

import java.util.List;

public class Account {

    private int id;
    private String name;
    private double balance;
    private boolean status;
    private List<String> history;

    public Account(int id, String name, double balance, boolean status){
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.status = true;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public void debit(double value){
        balance-=value;
    }

    public void credit(double value){
        balance+=value;
    }

}
