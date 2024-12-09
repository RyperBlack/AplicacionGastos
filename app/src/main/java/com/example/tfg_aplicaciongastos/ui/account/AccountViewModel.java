package com.example.tfg_aplicaciongastos.ui.account;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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
    private final SharedPreferences sharedPreferences;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new AccountDBHelper(application);
        sharedPreferences = application.getSharedPreferences("account_preferences", Context.MODE_PRIVATE);
        accountsLiveData = new MutableLiveData<>();
        loadAccounts();
    }

    public int getSelectedAccountId() {
        return sharedPreferences.getInt("selected_account_id", -1);
    }

    public void setSelectedAccount(Account account) {
        sharedPreferences.edit().putInt("selected_account_id", account.getId()).apply();
    }

    public void loadAccounts() {
        List<Account> accounts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "accounts",
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

    public double getTotalOfAllAccounts() {
        List<Account> accounts = accountsLiveData.getValue();
        if (accounts == null || accounts.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Account account : accounts) {
            total += account.getTotal();
        }
        return total;
    }

    public void deleteAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("accounts", "_id = ?", new String[]{String.valueOf(account.getId())});
        db.close();
        loadAccounts();
    }
}
