package com.example.tfg_aplicaciongastos.ddbb.helpers;

import static com.example.tfg_aplicaciongastos.ddbb.helpers.AccountContract.accountEntry;
import static com.example.tfg_aplicaciongastos.ddbb.helpers.AccountContract.categoryEntry;
import static com.example.tfg_aplicaciongastos.ddbb.helpers.AccountContract.exchangesEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.tfg_aplicaciongastos.ddbb.classes.Account;
import com.example.tfg_aplicaciongastos.ddbb.classes.Category;
import com.example.tfg_aplicaciongastos.ddbb.classes.Exchanges;

public class AccountDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "accounts.db";
    private final static int DATABASE_VERSION = 1;

    private final static String SQL_CREATE_TABLE_EXCHANGES =
            "CREATE TABLE " + exchangesEntry.TABLE_NAME + " (" +
                    exchangesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    exchangesEntry.ACCOUNT_ID + "INTEGER, " +
                    exchangesEntry.CATEGORY_ID + "INTEGER, " +
                    exchangesEntry.NAME + " TEXT NOT NULL, " +
                    exchangesEntry.TYPE + " TEXT NOT NULL, " +
                    exchangesEntry.QUANTITY + " TEXT NOT NULL, " +
                    exchangesEntry.DATE + " TEXT NOT NULL " +
                    ");";

    private final static String SQL_CREATE_TABLE_ACCOUNTS =
            "CREATE TABLE " + accountEntry.TABLE_NAME + " (" +
                    accountEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    accountEntry.NAME + " TEXT NOT NULL, " +
                    accountEntry.TOTAL + " TEXT NOT NULL" +
                    ");";

    private final static String SQL_CREATE_TABLE_CATEGORIES =
            "CREATE TABLE " + categoryEntry.TABLE_NAME + " (" +
                    categoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    categoryEntry.TYPE + " INTEGER, " +
                    categoryEntry.NAME + " TEXT NOT NULL, " +
                    categoryEntry.HEXCODE + " TEXT NOT NULL " +
                    ");";

    // Constructor
    public AccountDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public AccountDBHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_ACCOUNTS);
        db.execSQL(SQL_CREATE_TABLE_CATEGORIES);
        db.execSQL(SQL_CREATE_TABLE_EXCHANGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertAccount(Account account) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", account.getName());
        values.put("total", account.getTotal());
        db.insert("accounts", null, values);
        db.close();
    }

    public void updateAccount(Account account) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", account.getName());
        values.put("total", account.getTotal());
        db.update("accounts", values, "_id = ?", new String[]{String.valueOf(account.getId())});
        db.close();
    }

    public void insertCategory(Category category) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("type", category.getType());
        values.put("name", category.getName());
        values.put("hexcode", category.getHexCode());
        db.insert("category", null, values);
        db.close();
    }

    public void updateCategory(Category category) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("type", category.getType());
        values.put("name", category.getName());
        values.put("hexcode", category.getHexCode());
        db.update("category", values, "_id = ?", new String[]{String.valueOf(category.getId())});
    }

    public void insertExchange(Exchanges exchange) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("account", exchange.getAccountId());
        values.put("name", exchange.getName());
        values.put("category", exchange.getCategoryId());
        values.put("type", exchange.getType());
        values.put("quantity", exchange.getQuantity());
        values.put("date", exchange.getDate());

        db.insert("exchanges", null, values);

        updateAccountTotal(exchange.getAccountId(), exchange.getQuantity(), exchange.getType());
    }

    public void updateExchange(Exchanges exchange) {
        SQLiteDatabase db = getWritableDatabase();

        double previousQuantity;
        Cursor cursor = db.query("exchanges", new String[]{"quantity", "type"},
                "_id = ?", new String[]{String.valueOf(exchange.getId())}, null, null, null);
        if (cursor.moveToFirst()) {
            previousQuantity = cursor.getDouble(0);
            int previousType = cursor.getInt(1);

            updateAccountTotal(exchange.getAccountId(), -previousQuantity, previousType);
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("account", exchange.getAccountId());
        values.put("name", exchange.getName());
        values.put("category", exchange.getCategoryId());
        values.put("type", exchange.getType());
        values.put("quantity", exchange.getQuantity());
        values.put("date", exchange.getDate());

        db.update("exchanges", values, "_id = ?", new String[]{String.valueOf(exchange.getId())});
        updateAccountTotal(exchange.getAccountId(), exchange.getQuantity(), exchange.getType());
    }

    private void updateAccountTotal(int accountId, double quantity, int type) {
        SQLiteDatabase db = getWritableDatabase();

        double currentTotal = 0;
        Cursor cursor = db.query("accounts", new String[]{"total"},
                "_id = ?", new String[]{String.valueOf(accountId)}, null, null, null);
        if (cursor.moveToFirst()) {
            currentTotal = cursor.getDouble(0);
        }
        cursor.close();

        if (type == 1) {
            currentTotal -= quantity;
        } else if (type == 0) {
            currentTotal += quantity;
        }

        ContentValues totalUpdate = new ContentValues();
        totalUpdate.put("total", currentTotal);
        db.update("accounts", totalUpdate, "_id = ?", new String[]{String.valueOf(accountId)});
    }

}