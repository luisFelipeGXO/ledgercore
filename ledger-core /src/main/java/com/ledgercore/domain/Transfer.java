package com.ledgercore.domain;

import com.ledgercore.application.AccountService;

public class Transfer {

    private final AccountService account;
    private AccountService service;
    private Account accountOne;

    public Transfer(AccountService account) {
        this.account = account;
    }

    public Transfer makeTransfer(int transOne, int transTwo, double value){
        Transfer transfer = new Transfer(account);

        Account accoutOne = account.findAccount(transOne);
        Account accoutTwo = account.findAccount(transTwo);

        if(accoutOne != null && accoutTwo != null &&
        accoutOne.getBalance() > value) {
            account.addCredit(transTwo, value);
            account.addDebit(transOne, value);
            System.out.println("Successful!");
        } else {
            System.out.println("Error.");
            return null;
        }
        return transfer;
    }

}
