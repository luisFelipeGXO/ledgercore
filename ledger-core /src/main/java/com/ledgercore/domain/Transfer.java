package com.ledgercore.domain;

import com.ledgercore.application.AccountService;

public class Transfer {

    private final AccountService account;
    private static int nextId = 1;
    private int id;
    private int originId;
    private int destinationId;
    private double valueTrans;

    public Transfer(AccountService account) {
        this.account = account;
    }

    public Transfer makeTransfer(int transOne, int transTwo, double value){
        Transfer transfer = new Transfer(account);

        Account accoutOne = account.findAccount(transOne);
        Account accoutTwo = account.findAccount(transTwo);

        if(accoutOne != null && accoutTwo != null &&
        accoutOne.getBalance() > value) {
            transfer.id = nextId;
            transfer.originId = transOne;
            transfer.destinationId = transTwo;
            transfer.valueTrans = value;

            account.addCredit(transTwo, value);
            account.addDebit(transOne, value);
            nextId++;
            account.addTransfer(transfer);

            System.out.println("Successful! Transaction ID: " +transfer.id);
        } else {
            System.out.println("Error.");
            return null;
        }
        return transfer;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                ", id=" + id +
                ", originId=" + originId +
                ", destinationId=" + destinationId +
                ", valueTrans=" + valueTrans +
                '}';
    }
}
