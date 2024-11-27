package com.example.tfg_aplicaciongastos.ui.category;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tfg_aplicaciongastos.ddbb.classes.Account;
import com.example.tfg_aplicaciongastos.ddbb.classes.Category;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private final AccountDBHelper dbHelper;
    private final MutableLiveData<List<Category>> categoriesLiveData;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new AccountDBHelper(application);
        categoriesLiveData = new MutableLiveData<>();
        loadCategories();
    }

    public void loadCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "categories",
                new String[]{"_id","account Id", "name", "hexcode"},
                null, null, null, null,
                "_id DESC"
        );

        while (cursor.moveToNext()) {
            categories.add(new Category(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
            ));
        }
        cursor.close();
        db.close();

        categoriesLiveData.postValue(categories);
    }

    public LiveData<List<Category>> getCategories() {
        return categoriesLiveData;
    }

    public void deleteCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("category", "_id = ?", new String[]{String.valueOf(category.getId())});
        db.close();

        loadCategories();
    }
}