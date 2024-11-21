package com.example.tfg_aplicaciongastos.ui.account.createAccount;

import androidx.lifecycle.ViewModel;

import com.example.tfg_aplicaciongastos.ddbb.classes.Account;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;
import com.example.tfg_aplicaciongastos.ui.account.AccountViewModel;

import java.util.concurrent.Executor;

public class CreateAccountViewModel extends ViewModel {
    private final Executor executorService;
    private final AccountDBHelper dbHelper;

    public CreateAccountViewModel(Executor executorService, AccountDBHelper dbHelper) {
        this.executorService = executorService;
        this.dbHelper = dbHelper;
    }

    public void addAccount(Account account) {
        executorService.execute(() -> dbHelper.insertAccount(account));
    }

    public void updateAccount(Account account) {
        executorService.execute(() -> dbHelper.updateAccount(account));
    }
}
