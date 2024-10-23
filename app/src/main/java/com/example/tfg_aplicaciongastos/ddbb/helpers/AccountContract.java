package com.example.tfg_aplicaciongastos.ddbb.helpers;

import android.provider.BaseColumns;

public class AccountContract {

    public static abstract class exchangesEntry implements BaseColumns{
        public static final String TABLE_NAME = "Exchanges";
        public static final String ACCOUNT_ID = "Account ID";
        public static final String CATEGORY_ID = "Category ID";
        public static final String NAME = "Name";
        public static final String TYPE = "Type";
        public static final String QUANTITY = "Quantity";
    }

    public static abstract class accountEntry implements BaseColumns{
        public static final String TABLE_NAME = "Accounts";
        public static  final String NAME = "Name";
        public static final String TOTAL = "Total";
    }

    public static abstract class categoryEntry implements BaseColumns{
        public static final String TABLE_NAME = "Categories";
        public static final String ACCOUNT_ID = "Account ID";
        public static  final String NAME = "Name";
        public static final String HEXCODE = "HexCode";
    }
}
