package com.youraccountingapp.report;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.youraccountingapp.R;
import com.youraccountingapp.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    
    private TextView tvTotalIncome, tvTotalExpense, tvNetProfit;
    private PieChart pieChart;
    private DatabaseHelper dbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        
        dbHelper = new DatabaseHelper(this);
        
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvNetProfit = findViewById(R.id.tvNetProfit);
        pieChart = findViewById(R.id.pieChart);
        
        loadFinancialSummary();
        setupPieChart();
    }
    
    private void loadFinancialSummary() {
        SharedPreferences prefs = getSharedPreferences("accounting_app", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        
        if (userId != -1) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            
            // Total Income (Revenue)
            String incomeQuery = "SELECT SUM(amount) FROM transactions " +
                               "WHERE user_id = ? AND category = 'Revenue'";
            Cursor incomeCursor = db.rawQuery(incomeQuery, new String[]{String.valueOf(userId)});
            double totalIncome = 0;
            if (incomeCursor.moveToFirst()) {
                totalIncome = incomeCursor.getDouble(0);
            }
            incomeCursor.close();
            
            // Total Expense
            String expenseQuery = "SELECT SUM(amount) FROM transactions " +
                                "WHERE user_id = ? AND category = 'Expense'";
            Cursor expenseCursor = db.rawQuery(expenseQuery, new String[]{String.valueOf(userId)});
            double totalExpense = 0;
            if (expenseCursor.moveToFirst()) {
                totalExpense = expenseCursor.getDouble(0);
            }
            expenseCursor.close();
            
            double netProfit = totalIncome - totalExpense;
            
            tvTotalIncome.setText(String.format("Rp %,.2f", totalIncome));
            tvTotalExpense.setText(String.format("Rp %,.2f", totalExpense));
            tvNetProfit.setText(String.format("Rp %,.2f", netProfit));
            
            // Set color based on profit/loss
            if (netProfit >= 0) {
                tvNetProfit.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvNetProfit.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        }
    }
    
    private void setupPieChart() {
        SharedPreferences prefs = getSharedPreferences("accounting_app", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        
        if (userId == -1) return;
        
        List<PieEntry> entries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // Query untuk kategori pengeluaran
        String query = "SELECT category, SUM(amount) as total FROM transactions " +
                      "WHERE user_id = ? AND category IN ('Expense', 'Revenue') " +
                      "GROUP BY category";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        
        while (cursor.moveToNext()) {
            String category = cursor.getString(0);
            float total = cursor.getFloat(1);
            entries.add(new PieEntry(total, category));
        }
        cursor.close();
        
        if (!entries.isEmpty()) {
            PieDataSet dataSet = new PieDataSet(entries, "Financial Distribution");
            dataSet.setColors(new int[]{getResources().getColor(R.color.colorExpense),
                                      getResources().getColor(R.color.colorIncome)});
            dataSet.setValueTextSize(12f);
            
            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("Financial Overview");
            pieChart.animateY(1000);
            pieChart.invalidate();
        }
    }
              }
