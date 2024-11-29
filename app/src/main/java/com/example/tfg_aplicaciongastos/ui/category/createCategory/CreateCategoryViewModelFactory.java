package com.example.tfg_aplicaciongastos.ui.category.createCategory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;

import java.util.concurrent.Executor;

/**
 * @noinspection unchecked
 */
public class CreateCategoryViewModelFactory implements ViewModelProvider.Factory {
    private final Executor executorService;
    private final AccountDBHelper dbHelper;

    public CreateCategoryViewModelFactory(Executor executorService, AccountDBHelper dbHelper) {
        this.executorService = executorService;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CreateCategoryViewModel.class)) {
            return (T) new CreateCategoryViewModel(executorService, dbHelper);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
