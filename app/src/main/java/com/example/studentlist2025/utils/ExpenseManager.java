package com.example.studentlist2025.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.studentlist2025.models.Expense;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpenseManager {
    private static final String PREF_NAME = "expenses";
    private static final String KEY_EXPENSES = "expense_list";
    private Context context;
    private SharedPreferences prefs;
    private Gson gson;

    public ExpenseManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void addExpense(Expense expense) {
        List<Expense> expenses = getAllExpenses();
        expenses.add(expense);
        saveExpenses(expenses);
    }

    public List<Expense> getAllExpenses() {
        String json = prefs.getString(KEY_EXPENSES, "[]");
        Type type = new TypeToken<List<Expense>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public List<Expense> getExpensesForMonth(int year, int month) {
        List<Expense> allExpenses = getAllExpenses();
        List<Expense> monthlyExpenses = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        for (Expense expense : allExpenses) {
            calendar.setTimeInMillis(expense.getTimestamp());
            if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month) {
                monthlyExpenses.add(expense);
            }
        }
        return monthlyExpenses;
    }

    public double getTotalExpenses() {
        List<Expense> expenses = getAllExpenses();
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    public double getMonthlyTotal(int year, int month) {
        List<Expense> monthlyExpenses = getExpensesForMonth(year, month);
        double total = 0;
        for (Expense expense : monthlyExpenses) {
            total += expense.getAmount();
        }
        return total;
    }

    private void saveExpenses(List<Expense> expenses) {
        String json = gson.toJson(expenses);
        prefs.edit().putString(KEY_EXPENSES, json).apply();
    }

    public String[] getExpenseCategories() {
        return new String[]{
                "Food & Dining",
                "Transportation",
                "Shopping",
                "Entertainment",
                "Bills & Utilities",
                "Health & Medical",
                "Travel",
                "Education",
                "Gifts & Donations",
                "Personal Care",
                "Home & Garden",
                "Other"
        };
    }
}