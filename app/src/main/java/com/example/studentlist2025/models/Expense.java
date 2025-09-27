package com.example.studentlist2025.models;

public class Expense {
    private String id;
    private double amount;
    private String category;
    private String description;
    private String date;
    private long timestamp;

    public Expense(double amount, String category, String description, String date) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() { return id; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public long getTimestamp() { return timestamp; }

    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
}