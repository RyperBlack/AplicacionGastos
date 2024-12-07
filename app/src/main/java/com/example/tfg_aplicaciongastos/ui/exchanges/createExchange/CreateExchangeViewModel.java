package com.example.tfg_aplicaciongastos.ui.exchanges.createExchange;

import androidx.lifecycle.ViewModel;

import com.example.tfg_aplicaciongastos.ddbb.classes.Category;
import com.example.tfg_aplicaciongastos.ddbb.classes.Exchanges;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;

import java.util.concurrent.Executor;

public class CreateExchangeViewModel extends ViewModel {
    private final Executor executorService;
    private final AccountDBHelper dbHelper;

    public CreateExchangeViewModel(Executor executorService, AccountDBHelper dbHelper) {
        this.executorService = executorService;
        this.dbHelper = dbHelper;
    }

    public void addExchange(Exchanges exchange) {
        executorService.execute(() -> dbHelper.insertExchange(exchange));
    }

    public void updateExchange(Exchanges exchange) {
        executorService.execute(() -> dbHelper.updateExchange(exchange));
    }
}
