package com.youraccountingapp.models;

import java.util.Date;

public class Transaction {
    private int id;
    private Date date;
    private String description;
    private String accountType; // Kas, Bank, Piutang, etc.
    private String transactionType; // Debit/Kredit
    private double amount;
    private String category; // Pendapatan, Beban, Asset, etc.
    
    public Transaction() {}
    
    public Transaction(Date date, String description, String accountType, 
                      String transactionType, double amount, String category) {
        this.date = date;
        this.description = description;
        this.accountType = accountType;
        this.transactionType = transactionType;
        this.amount = amount;
        this.category = category;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
      }
