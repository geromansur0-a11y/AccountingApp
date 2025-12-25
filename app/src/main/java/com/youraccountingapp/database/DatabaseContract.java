package com.youraccountingapp.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    
    private DatabaseContract() {}
    
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_COMPANY = "company_name";
        public static final String COLUMN_CREATED_AT = "created_at";
    }
    
    public static class TransactionEntry implements BaseColumns {
        public static final String TABLE_NAME = "transactions";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ACCOUNT_TYPE = "account_type";
        public static final String COLUMN_TRANSACTION_TYPE = "transaction_type";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_USER_ID = "user_id";
    }
    
    public static class AccountEntry implements BaseColumns {
        public static final String TABLE_NAME = "accounts";
        public static final String COLUMN_ACCOUNT_NAME = "account_name";
        public static final String COLUMN_ACCOUNT_TYPE = "account_type"; // Asset, Liability, Equity, Revenue, Expense
        public static final String COLUMN_BALANCE = "balance";
        public static final String COLUMN_USER_ID = "user_id";
    }
      }
