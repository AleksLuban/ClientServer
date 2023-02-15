package com.javastart.server;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static DataBase INSTANCE;
    private List<Account> databaseAccount = new ArrayList<>();

    public List<Account> getDatabaseAccount() {
        return databaseAccount;
    }

    private DataBase() {

    }

    public static DataBase getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DataBase();
        }
        return INSTANCE;
    }

    public void addAccount(Account account) {
        databaseAccount.add(account);
    }

    public boolean checkAccount(Account account) {
        boolean v = true;
        for (Account account1 : databaseAccount) {
            if (account.getId() == account1.getId()) {
                v = false;
            }
        }
        return v;
    }

    public void printAllAccount() {
        for (Account account2 : databaseAccount) {
            System.out.println(account2.toString());
        }
    }
}
