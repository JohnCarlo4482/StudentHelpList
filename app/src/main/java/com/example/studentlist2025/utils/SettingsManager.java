package com.example.studentlist2025.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String PREF_NAME = "app_settings";
    private static final String KEY_THEME = "theme_preference";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private static final String KEY_TASK_REMINDERS = "task_reminders";
    private static final String KEY_EXPENSE_ALERTS = "expense_alerts";
    private static final String KEY_CONTACT_UPDATES = "contact_updates";
    private static final String KEY_WEEKLY_SUMMARY = "weekly_summary";
    private static final String KEY_USERNAME = "username";

    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_AUTO = 2;

    private SharedPreferences prefs;

    public SettingsManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Theme Settings
    public void setTheme(int theme) {
        prefs.edit().putInt(KEY_THEME, theme).apply();
    }

    public int getTheme() {
        return prefs.getInt(KEY_THEME, THEME_LIGHT);
    }

    // Notification Settings
    public void setNotificationsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply();
    }

    public boolean areNotificationsEnabled() {
        return prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }

    public void setTaskReminders(boolean enabled) {
        prefs.edit().putBoolean(KEY_TASK_REMINDERS, enabled).apply();
    }

    public boolean areTaskRemindersEnabled() {
        return prefs.getBoolean(KEY_TASK_REMINDERS, true);
    }

    public void setExpenseAlerts(boolean enabled) {
        prefs.edit().putBoolean(KEY_EXPENSE_ALERTS, enabled).apply();
    }

    public boolean areExpenseAlertsEnabled() {
        return prefs.getBoolean(KEY_EXPENSE_ALERTS, false);
    }

    public void setContactUpdates(boolean enabled) {
        prefs.edit().putBoolean(KEY_CONTACT_UPDATES, enabled).apply();
    }

    public boolean areContactUpdatesEnabled() {
        return prefs.getBoolean(KEY_CONTACT_UPDATES, false);
    }

    public void setWeeklySummary(boolean enabled) {
        prefs.edit().putBoolean(KEY_WEEKLY_SUMMARY, enabled).apply();
    }

    public boolean isWeeklySummaryEnabled() {
        return prefs.getBoolean(KEY_WEEKLY_SUMMARY, true);
    }

    // User Settings
    public void setUsername(String username) {
        prefs.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "User");
    }
}