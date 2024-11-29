package com.example.tfg_aplicaciongastos.ui.account;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tfg_aplicaciongastos.ddbb.classes.Account;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;

import java.util.ArrayList;
import java.util.List;

public class AccountViewModel extends AndroidViewModel {

    private final AccountDBHelper dbHelper;
    private final MutableLiveData<List<Account>> accountsLiveData;
    private final MutableLiveData<Account> selectedAccountLiveData = new MutableLiveData<>();

    public AccountViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new AccountDBHelper(application);
        accountsLiveData = new MutableLiveData<>();
        loadAccounts();
    }

    public LiveData<Account> getSelectedAccount() {
        return selectedAccountLiveData;
    }

    public void setSelectedAccount(Account account) {
        selectedAccountLiveData.setValue(account);
    }

    public void loadAccounts() {
        List<Account> accounts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "accounts", // Tabla
                new String[]{"_id", "name", "total"},
                null, null, null, null,
                "_id DESC"
        );

        while (cursor.moveToNext()) {
            accounts.add(new Account(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2)
            ));
        }
        cursor.close();
        db.close();

        accountsLiveData.postValue(accounts);
    }

    public LiveData<List<Account>> getAccounts() {
        return accountsLiveData;
    }

    public void deleteAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("accounts", "_id = ?", new String[]{String.valueOf(account.getId())});
        db.close();

        loadAccounts();
    }
}