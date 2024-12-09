package com.example.tfg_aplicaciongastos.ui.exchanges.createExchange;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;

import java.util.concurrent.Executor;

/** @noinspection ALL*/
public class CreateExchangeViewModelFactory implements ViewModelProvider.Factory {
    private final Executor executorService;
    private final AccountDBHelper dbHelper;

    public CreateExchangeViewModelFactory(Executor executorService, AccountDBHelper dbHelper) {
        this.executorService = executorService;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CreateExchangeViewModel.class)) {
            return (T) new CreateExchangeViewModel(executorService, dbHelper);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
