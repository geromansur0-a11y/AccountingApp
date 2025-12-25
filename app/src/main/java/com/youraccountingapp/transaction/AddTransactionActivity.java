package com.youraccountingapp.transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.youraccountingapp.R;
import com.youraccountingapp.database.DatabaseHelper;
import com.youraccountingapp.models.Transaction;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {
    
    private EditText etDate, etDescription, etAmount;
    private Spinner spAccountType, spTransactionType, spCategory;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private Calendar calendar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        
        dbHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();
        
        initializeViews();
        setupSpinners();
        setupDatePicker();
        
        btnSave.setOnClickListener(v -> saveTransaction());
    }
    
    private void initializeViews() {
        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);
        etAmount = findViewById(R.id.etAmount);
        spAccountType = findViewById(R.id.spAccountType);
        spTransactionType = findViewById(R.id.spTransactionType);
        spCategory = findViewById(R.id.spCategory);
        btnSave = findViewById(R.id.btnSave);
    }
    
    private void setupSpinners() {
        // Account types
        String[] accountTypes = {"Kas", "Bank", "Piutang", "Persediaan", "Peralatan"};
        ArrayAdapter<String> accountAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, accountTypes);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAccountType.setAdapter(accountAdapter);
        
        // Transaction types
        String[] transactionTypes = {"Debit", "Kredit"};
        ArrayAdapter<String> transactionAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, transactionTypes);
        transactionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTransactionType.setAdapter(transactionAdapter);
        
        // Categories
        String[] categories = {"Asset", "Liability", "Equity", "Revenue", "Expense"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);
    }
    
    private void setupDatePicker() {
        etDate.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(
                AddTransactionActivity.this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    etDate.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        });
    }
    
    private void saveTransaction() {
        // Get current user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("accounting_app", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String description = etDescription.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        
        if (description.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountStr);
            
            Transaction transaction = new Transaction(
                calendar.getTime(),
                description,
                spAccountType.getSelectedItem().toString(),
                spTransactionType.getSelectedItem().toString(),
                amount,
                spCategory.getSelectedItem().toString()
            );
            
            long result = dbHelper.addTransaction(transaction, userId);
            
            if (result != -1) {
                Toast.makeText(this, "Transaction saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save transaction", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
        }
    }
                                  }
