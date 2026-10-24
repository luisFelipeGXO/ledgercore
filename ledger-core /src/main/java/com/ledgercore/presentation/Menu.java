package com.ledgercore.presentation;

import com.ledgercore.domain.Account;
import com.ledgercore.application.AccountService;

import java.util.List;
import java.util.Scanner;

public class Menu {

    public Menu() {
        Scanner scanner = new Scanner(System.in);
        AccountService service = new AccountService();
        List<Account> accounts = service.listAccounts();

        int n;

        do {
            System.out.println("=== LedgerCore ===");
            System.out.println("");
            System.out.println("1. Create account");
            System.out.println("2. Consult account");
            System.out.println("3. Make operation");
            System.out.println("4. Consult history");
            System.out.println("5. List all accounts");
            System.out.println("0. Exit");

            n = scanner.nextInt();

            switch(n) {
                case 0:
                    System.out.println("Closgin LedgerCore...");
                    return;
                case 1:
                    scanner.nextLine();
                    System.out.println("Account name:");
                    String name = scanner.nextLine();
                    double balance = 0;
                    Account accountcreate = service.createAccount(name,balance);
                    break;
                case 2:
                    scanner.nextLine();
                    System.out.println("Type the ID: ");
                    int id = scanner.nextInt();

                    Account account = service.findAccount(id);

                    if(account != null){
                        System.out.println(account);
                    } else {
                        System.out.println("Accout not found.");
                    }
                    break;
                case 3:
                    scanner.nextLine();
                    new MakeOperation(service);
                    break;
                case 5:
                    scanner.nextLine();
                    for(Account accountList : accounts) {
                        System.out.println(accountList);
                    }
                    break;

            }
        }
        while (n != 0);

        scanner.close();
    }

}

