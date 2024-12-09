package com.example.tfg_aplicaciongastos.ui.exchanges;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tfg_aplicaciongastos.ddbb.classes.Exchanges;
import com.example.tfg_aplicaciongastos.ddbb.classes.PieChartData;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExchangesViewModel extends AndroidViewModel {

    private final MutableLiveData<PieChartData> pieChartData = new MutableLiveData<>();
    private final MutableLiveData<Double> totalLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Exchanges>> exchangesLiveData = new MutableLiveData<>();
    private final AccountDBHelper dbHelper;

    public ExchangesViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new AccountDBHelper(application.getApplicationContext());
    }

    public LiveData<List<Exchanges>> getExchanges() {
        return exchangesLiveData;
    }

    public LiveData<Double> getTotalLiveData() {
        return totalLiveData;
    }

    public LiveData<PieChartData> getPieChartData() {
        return pieChartData;
    }

    public void loadExchanges(Context context, int type, Date startDate, Date endDate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account_preferences", Context.MODE_PRIVATE);
        int accountId = sharedPreferences.getInt("selected_account_id", -1);

        if (accountId == -1) {
            exchangesLiveData.postValue(new ArrayList<>());
            return;
        }

        List<Exchanges> exchanges = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDateStr = dateFormat.format(startDate);
        String endDateStr = dateFormat.format(endDate);

        Log.d("QueryDebug", "Start Date: " + startDateStr + ", End Date: " + endDateStr);

        String query = "SELECT e._id, e.account, e.name, e.category, e.type, e.quantity, e.date, c.name as categoryName, c.hexcode as categoryColor " +
                "FROM exchanges e " +
                "INNER JOIN category c ON e.category = c._id " +
                "WHERE e.account = ? AND e.type = ? AND e.date BETWEEN ? AND ? " +
                "ORDER BY e._id DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(accountId), String.valueOf(type), startDateStr, endDateStr});

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


    public void loadPieChartData(Context context, int type, Date startDate, Date endDate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account_preferences", Context.MODE_PRIVATE);
        int accountId = sharedPreferences.getInt("selected_account_id", -1);

        if (accountId == -1) {
            pieChartData.postValue(null);
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT c.name, SUM(e.quantity) AS total, c.hexcode " +
                "FROM exchanges e " +
                "INNER JOIN category c ON e.category = c._id " +
                "WHERE e.account = ? AND e.type = ? AND e.date BETWEEN ? AND ? " +
                "GROUP BY c.name, c.hexcode";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDateStr = dateFormat.format(startDate);
        String endDateStr = dateFormat.format(endDate);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(accountId), String.valueOf(type), startDateStr, endDateStr});

        Map<String, Double> categoryData = new LinkedHashMap<>();
        List<String> categoryColors = new ArrayList<>();

        while (cursor.moveToNext()) {
            String categoryName = cursor.getString(0);
            double total = cursor.getDouble(1);
            String colorHex = cursor.getString(2);

            categoryData.put(categoryName, total);
            categoryColors.add(colorHex);
        }

        cursor.close();
        db.close();

        pieChartData.postValue(new PieChartData(categoryData, categoryColors));
    }

    public void getSelectedAccountTotal(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account_preferences", Context.MODE_PRIVATE);
        int accountId = sharedPreferences.getInt("selected_account_id", -1);

        if (accountId == -1) {
            totalLiveData.postValue(0.0);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double total = 0.0;
        Cursor cursor = db.query(
                "accounts",
                new String[]{"total"},
                "_id = ?",
                new String[]{String.valueOf(accountId)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();

        totalLiveData.postValue(total);
    }

    public void deleteExchange(Exchanges exchange, Date startDate, Date endDate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("exchanges", "_id = ?", new String[]{String.valueOf(exchange.getId())});
        db.close();
        loadExchanges(getApplication().getApplicationContext(), exchange.getType(), startDate, endDate);
    }

    public void updateAccountTotal(Context context, int accountId, Exchanges exchange) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        double adjustment = exchange.getQuantity() * (exchange.getType() == 1 ? 1 : -1);

        String query = "UPDATE accounts SET total = total + ? WHERE _id = ?";
        db.execSQL(query, new Object[]{adjustment, accountId});

        db.close();

        getSelectedAccountTotal(context);
    }
}
