package com.example.tfg_aplicaciongastos.ddbb.helpers;

import static com.example.tfg_aplicaciongastos.ddbb.helpers.AccountContract.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AccountDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "accounts.db";
    private final static int DATABASE_VERSION = 1;

    // Sentencia SQL para crear la tabla exchanges
    private final static String SQL_CREATE_TABLE_EXCHANGES =
            "CREATE TABLE " + exchangesEntry.TABLE_NAME + " (" +
                    exchangesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    exchangesEntry.ACCOUNT_ID + "INTEGER, " +
                    exchangesEntry.CATEGORY_ID + "INTEGER, " +
                    exchangesEntry.NAME + " TEXT NOT NULL, " +
                    exchangesEntry.TYPE + " TEXT NOT NULL, " +
                    exchangesEntry.QUANTITY + " TEXT NOT NULL " +
                    ");";


    // Sentencia SQL para crear la tabla accounts
    private final static String SQL_CREATE_TABLE_ACCOUNTS =
            "CREATE TABLE " + accountEntry.TABLE_NAME + " (" +
                    accountEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    accountEntry.NAME + " TEXT NOT NULL, " +
                    accountEntry.TOTAL + " TEXT NOT NULL" +
                    ");";

    // Sentencia SQL para crear la tabla categories
    private final static String SQL_CREATE_TABLE_CATEGORIES =
            "CREATE TABLE " + categoryEntry.TABLE_NAME + " (" +
                    categoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    categoryEntry.ACCOUNT_ID + "INTEGER," +
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
        // Ejecutar las sentencias SQL para crear las tablas
        db.execSQL(SQL_CREATE_TABLE_ACCOUNTS);
        db.execSQL(SQL_CREATE_TABLE_CATEGORIES);
        db.execSQL(SQL_CREATE_TABLE_EXCHANGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}