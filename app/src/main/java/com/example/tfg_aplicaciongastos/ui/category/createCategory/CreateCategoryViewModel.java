package com.example.tfg_aplicaciongastos.ui.category.createCategory;

import androidx.lifecycle.ViewModel;

import com.example.tfg_aplicaciongastos.ddbb.classes.Category;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;

import java.util.concurrent.Executor;

public class CreateCategoryViewModel extends ViewModel {
    private final Executor executorService;
    private final AccountDBHelper dbHelper;

    public CreateCategoryViewModel(Executor executorService, AccountDBHelper dbHelper) {
        this.executorService = executorService;
        this.dbHelper = dbHelper;
    }

    public void addCategory(Category category) {
        executorService.execute(() -> dbHelper.insertCategory(category));
    }

    public void updateCategory(Category category) {
        executorService.execute(() -> dbHelper.updateCategory(category));
    }
}