package com.youraccountingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "accounting_app.db";
    private static final int DATABASE_VERSION = 1;
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + 
            DatabaseContract.UserEntry.TABLE_NAME + " (" +
            DatabaseContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
            DatabaseContract.UserEntry.COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
            DatabaseContract.UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
            DatabaseContract.UserEntry.COLUMN_COMPANY + " TEXT, " +
            DatabaseContract.UserEntry.COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        
        // Create transactions table
        String SQL_CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + 
            DatabaseContract.TransactionEntry.TABLE_NAME + " (" +
            DatabaseContract.TransactionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.TransactionEntry.COLUMN_DATE + " DATE NOT NULL, " +
            DatabaseContract.TransactionEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            DatabaseContract.TransactionEntry.COLUMN_ACCOUNT_TYPE + " TEXT NOT NULL, " +
            DatabaseContract.TransactionEntry.COLUMN_TRANSACTION_TYPE + " TEXT NOT NULL, " +
            DatabaseContract.TransactionEntry.COLUMN_AMOUNT + " REAL NOT NULL, " +
            DatabaseContract.TransactionEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
            DatabaseContract.TransactionEntry.COLUMN_USER_ID + " INTEGER, " +
            "FOREIGN KEY (" + DatabaseContract.TransactionEntry.COLUMN_USER_ID + ") " +
            "REFERENCES " + DatabaseContract.UserEntry.TABLE_NAME + "(" + DatabaseContract.UserEntry._ID + "));";
        
        // Create accounts table
        String SQL_CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + 
            DatabaseContract.AccountEntry.TABLE_NAME + " (" +
            DatabaseContract.AccountEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.AccountEntry.COLUMN_ACCOUNT_NAME + " TEXT NOT NULL, " +
            DatabaseContract.AccountEntry.COLUMN_ACCOUNT_TYPE + " TEXT NOT NULL, " +
            DatabaseContract.AccountEntry.COLUMN_BALANCE + " REAL DEFAULT 0, " +
            DatabaseContract.AccountEntry.COLUMN_USER_ID + " INTEGER, " +
            "FOREIGN KEY (" + DatabaseContract.AccountEntry.COLUMN_USER_ID + ") " +
            "REFERENCES " + DatabaseContract.UserEntry.TABLE_NAME + "(" + DatabaseContract.UserEntry._ID + "));";
        
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_TRANSACTIONS_TABLE);
        db.execSQL(SQL_CREATE_ACCOUNTS_TABLE);
        
        Log.d("DatabaseHelper", "Database created successfully");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TransactionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.AccountEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }
    
    // Insert transaction
    public long addTransaction(Transaction transaction, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TransactionEntry.COLUMN_DATE, 
                  new SimpleDateFormat("yyyy-MM-dd").format(transaction.getDate()));
        values.put(DatabaseContract.TransactionEntry.COLUMN_DESCRIPTION, 
                  transaction.getDescription());
        values.put(DatabaseContract.TransactionEntry.COLUMN_ACCOUNT_TYPE, 
                  transaction.getAccountType());
        values.put(DatabaseContract.TransactionEntry.COLUMN_TRANSACTION_TYPE, 
                  transaction.getTransactionType());
        values.put(DatabaseContract.TransactionEntry.COLUMN_AMOUNT, 
                  transaction.getAmount());
        values.put(DatabaseContract.TransactionEntry.COLUMN_CATEGORY, 
                  transaction.getCategory());
        values.put(DatabaseContract.TransactionEntry.COLUMN_USER_ID, userId);
        
        return db.insert(DatabaseContract.TransactionEntry.TABLE_NAME, null, values);
    }
    
    // Get all transactions
    public List<Transaction> getAllTransactions(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT * FROM " + DatabaseContract.TransactionEntry.TABLE_NAME + 
                      " WHERE " + DatabaseContract.TransactionEntry.COLUMN_USER_ID + " = ? " +
                      " ORDER BY " + DatabaseContract.TransactionEntry.COLUMN_DATE + " DESC";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(0));
                transaction.setDate(new Date(cursor.getString(1)));
                transaction.setDescription(cursor.getString(2));
                transaction.setAccountType(cursor.getString(3));
                transaction.setTransactionType(cursor.getString(4));
                transaction.setAmount(cursor.getDouble(5));
                transaction.setCategory(cursor.getString(6));
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactions;
    }
    
    // Get balance sheet data
    public Map<String, Double> getBalanceSheetData(int userId) {
        Map<String, Double> balances = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        // Query untuk total assets
        String assetsQuery = "SELECT SUM(amount) FROM " + 
            DatabaseContract.TransactionEntry.TABLE_NAME + 
            " WHERE " + DatabaseContract.TransactionEntry.COLUMN_USER_ID + " = ? " +
            " AND " + DatabaseContract.TransactionEntry.COLUMN_CATEGORY + " = 'Asset'";
        
        // Query untuk total liabilities
        String liabilitiesQuery = "SELECT SUM(amount) FROM " + 
            DatabaseContract.TransactionEntry.TABLE_NAME + 
            " WHERE " + DatabaseContract.TransactionEntry.COLUMN_USER_ID + " = ? " +
            " AND " + DatabaseContract.TransactionEntry.COLUMN_CATEGORY + " = 'Liability'";
            
        // Similar queries for Equity, Revenue, Expense
        
        return balances;
    }
          }
