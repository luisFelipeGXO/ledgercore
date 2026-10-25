package com.ledgercore.application;

import com.ledgercore.domain.Account;
import com.ledgercore.domain.Transfer;

import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private int nextId = 1;
    private List<Account> accounts = new ArrayList<>();
    private List<Transfer> transfers = new ArrayList<>();

    public Account createAccount(String holder, double balance) {
        Account account = new Account(nextId,holder,balance,true);
        if(holder.isBlank()) {
            System.out.println("No name.");
        } else {
            accounts.add(account);
            nextId ++;
            System.out.println("Successful! \n" +account);
            return account;
        }
        return null;
    }

    public List<Account> listAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        return "AccountService{" +
                "Id=" + nextId +
                ", accounts=" + accounts +
                '}';
    }

    public Account findAccount(int id){
        for (Account account : accounts) {
            if(account.getId()==id) {
                return account;
            }
        }
        return null;
    }


    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addCredit(int recipientId, double value) {
        Account account = findAccount(recipientId);
        if(account != null) {
            account.credit(value);
        } else {
            System.out.println("Error");
        }
    }

    public void addDebit(int recipientId, double value) {
        Account account = findAccount(recipientId);
        if(account != null) {
            account.debit(value);
        } else {
            System.out.println("Error");
        }
    }

    public List<Transfer> listTransfers() {
        return transfers;
    }

    public void addTransfer(Transfer transfer) {
        transfers.add(transfer);
    }
}
