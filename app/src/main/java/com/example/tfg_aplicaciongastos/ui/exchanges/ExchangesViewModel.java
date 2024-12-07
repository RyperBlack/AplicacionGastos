package com.example.tfg_aplicaciongastos.ui.exchanges;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;
import com.example.tfg_aplicaciongastos.ddbb.classes.Exchanges;

import java.util.ArrayList;
import java.util.List;

public class ExchangesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Exchanges>> exchangesLiveData = new MutableLiveData<>();
    private final AccountDBHelper dbHelper;

    public ExchangesViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new AccountDBHelper(application.getApplicationContext());
    }

    public LiveData<List<Exchanges>> getExchanges() {
        return exchangesLiveData;
    }

    public void loadExchanges(Context context, int type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account_preferences", Context.MODE_PRIVATE);
        int accountId = sharedPreferences.getInt("selected_account_id", -1);

        if (accountId == -1) {
            exchangesLiveData.postValue(new ArrayList<>());
            return;
        }

        List<Exchanges> exchanges = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT e._id, e.account, e.name, e.category, e.type, e.quantity, e.date, c.name as categoryName, c.hexcode as categoryColor " +
                "FROM exchanges e " +
                "INNER JOIN category c ON e.category = c._id " +
                "WHERE e.account = ? AND e.type = ? " +
                "ORDER BY e._id DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(accountId), String.valueOf(type)});

        while (cursor.moveToNext()) {
            exchanges.add(new Exchanges(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getDouble(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
            ));
        }
        cursor.close();
        db.close();

        exchangesLiveData.postValue(exchanges);
    }
}
