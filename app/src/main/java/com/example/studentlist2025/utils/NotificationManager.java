package com.example.studentlist2025.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationManager {
    private static final String PREF_NAME = "notifications";
    private static final String KEY_NOTIFICATIONS = "notification_list";
    private Context context;
    private SharedPreferences prefs;

    public NotificationManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void addNotification(String message) {
        Set<String> notifications = prefs.getStringSet(KEY_NOTIFICATIONS, new HashSet<>());
        notifications.add(System.currentTimeMillis() + ":" + message);
        prefs.edit().putStringSet(KEY_NOTIFICATIONS, notifications).apply();
    }

    public List<String> getNotifications() {
        Set<String> notificationSet = prefs.getStringSet(KEY_NOTIFICATIONS, new HashSet<>());
        List<String> notifications = new ArrayList<>();
        for (String notification : notificationSet) {
            String[] parts = notification.split(":", 2);
            if (parts.length == 2) {
                notifications.add(parts[1]);
            }
        }
        return notifications;
    }

    public void clearAllNotifications() {
        prefs.edit().remove(KEY_NOTIFICATIONS).apply();
    }

    public int getNotificationCount() {
        Set<String> notifications = prefs.getStringSet(KEY_NOTIFICATIONS, new HashSet<>());
        return notifications.size();
    }
}
