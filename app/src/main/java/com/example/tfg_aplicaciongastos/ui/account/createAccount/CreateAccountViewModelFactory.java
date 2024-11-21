package com.example.tfg_aplicaciongastos.ui.account.createAccount;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;

import java.util.concurrent.Executor;

public class CreateAccountViewModelFactory implements ViewModelProvider.Factory {
    private final Executor executorService;
    private final AccountDBHelper dbHelper;

    public CreateAccountViewModelFactory(Executor executorService, AccountDBHelper dbHelper) {
        this.executorService = executorService;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CreateAccountViewModel.class)) {
            return (T) new CreateAccountViewModel(executorService, dbHelper);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
