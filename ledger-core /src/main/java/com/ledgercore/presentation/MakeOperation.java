package com.ledgercore.presentation;

import com.ledgercore.application.AccountService;
import com.ledgercore.domain.Account;
import com.ledgercore.domain.Transfer;

import java.util.Scanner;

public class MakeOperation {

    private AccountService service;

    public MakeOperation(AccountService service) {
        Scanner scanner = new Scanner(System.in);
        int n;

        do {
            System.out.println("1. Debit");
            System.out.println("2. Credit");
            System.out.println("3. Transfer");
            System.out.println("4. Back");

            n = scanner.nextInt();

            switch(n) {
                case 1:
                    scanner.nextLine();
                    System.out.println("Recipient account ID:");
                    int debitID = scanner.nextInt();
                    System.out.println("Value");
                    double debitValue = scanner.nextDouble();
                    if(service.findAccount(debitID) != null && debitValue <= service.findAccount(debitID).getBalance()) {
                        service.addDebit(debitID, debitValue);
                        System.out.println("Successful.");
                    } else {
                        System.out.println("Error");
                    }
                    break;
                case 2:
                    scanner.nextLine();
                    System.out.println("Recipient account ID:");
                    int creditId = scanner.nextInt();
                    System.out.println("Value");
                    double creditValue = scanner.nextDouble();
                    if(service.findAccount(creditId) != null) {
                        service.addCredit(creditId, creditValue);
                        System.out.println("Successful.");
                    } else {
                        System.out.println("Error");
                    }
                    break;
                case 3:
                    scanner.nextLine();
                    System.out.println("Enter the account ID that will be making the transfer:");
                    int transOne = scanner.nextInt();
                    System.out.println("Enter the ID of the account that will receive the transfer:");
                    int transTwo = scanner.nextInt();
                    System.out.println("Value:");
                    double valueTrans = scanner.nextDouble();
                    Transfer transfer = new Transfer(service);
                    transfer.makeTransfer(transOne, transTwo, valueTrans);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("ERROR");
            }
        } while (n != 4);
    }
}
