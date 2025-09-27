package com.example.studentlist2025;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.studentlist2025.models.Contact;
import com.example.studentlist2025.models.Expense;
import com.example.studentlist2025.utils.ContactManager;
import com.example.studentlist2025.utils.ExpenseManager;
import com.example.studentlist2025.utils.NotificationManager;
import com.example.studentlist2025.utils.SettingsManager;
import com.example.studentlist2025.utils.ValidationUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    // UI Components
    private TextView tvWelcome, tvSelectedDate;
    private EditText etTaskTitle, etTaskDescription;
    private Button btnNotifications, btnSettings, btnAddExpenses, btnContactInfo;
    private Button btnQuickContact, btnSelectDate, btnAddTask, btnLogout;
    private LinearLayout llTaskList;

    // Data and Managers
    private List<Task> taskList;
    private Calendar selectedCalendar;
    private SimpleDateFormat dateFormat;
    private NotificationManager notificationManager;
    private ExpenseManager expenseManager;
    private ContactManager contactManager;
    private SettingsManager settingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeManagers();
        initializeComponents();
        setupClickListeners();
        loadInitialData();
    }

    private void initializeManagers() {
        notificationManager = new NotificationManager(this);
        expenseManager = new ExpenseManager(this);
        contactManager = new ContactManager(this);
        settingsManager = new SettingsManager(this);
    }

    private void initializeComponents() {
        // Header components
        tvWelcome = findViewById(R.id.tvWelcome);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnSettings = findViewById(R.id.btnSettings);

        // Quick action buttons
        btnAddExpenses = findViewById(R.id.btnAddExpenses);
        btnContactInfo = findViewById(R.id.btnContactInfo);

        // Task section components
        btnQuickContact = findViewById(R.id.btnQuickContact);
        etTaskTitle = findViewById(R.id.etTaskTitle);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnAddTask = findViewById(R.id.btnAddTask);

        // Other components
        llTaskList = findViewById(R.id.llTaskList);
        btnLogout = findViewById(R.id.btnLogout);

        // Initialize data structures
        taskList = new ArrayList<>();
        selectedCalendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        // Update notification badge
        updateNotificationButton();
    }

    private void setupClickListeners() {
        // Header buttons
        btnNotifications.setOnClickListener(v -> showNotifications());
        btnSettings.setOnClickListener(v -> openSettings());

        // Quick action buttons
        btnAddExpenses.setOnClickListener(v -> openExpenses());
        btnContactInfo.setOnClickListener(v -> showContactInfo());

        // Task section buttons
        btnQuickContact.setOnClickListener(v -> showQuickContact());
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnAddTask.setOnClickListener(v -> addNewTask());

        // Logout button
        btnLogout.setOnClickListener(v -> performLogout());
    }

    private void loadInitialData() {
        // Set welcome message with current user
        String username = settingsManager.getUsername();
        tvWelcome.setText("Welcome back, " + username + "!");

        // Load existing tasks
        loadExistingTasks();

        // Set initial date
        tvSelectedDate.setText(dateFormat.format(new Date()));

        // Update notification count
        updateNotificationButton();
    }

    // Notification Functions
    private void showNotifications() {
        List<String> notifications = notificationManager.getNotifications();
        int count = notificationManager.getNotificationCount();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("🔔 Notifications (" + count + ")");

        if (notifications.isEmpty()) {
            builder.setMessage("No new notifications");
        } else {
            StringBuilder notificationText = new StringBuilder();
            for (int i = 0; i < Math.min(notifications.size(), 10); i++) {
                notificationText.append("• ").append(notifications.get(i)).append("\n");
            }
            builder.setMessage(notificationText.toString());
        }

        builder.setPositiveButton("Mark All Read", (dialog, which) -> {
            notificationManager.clearAllNotifications();
            updateNotificationButton();
            Toast.makeText(this, "All notifications marked as read", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void updateNotificationButton() {
        int count = notificationManager.getNotificationCount();
        if (count > 0) {
            btnNotifications.setText("🔔(" + count + ")");
        } else {
            btnNotifications.setText("🔔");
        }
    }

    // Settings Function
    private void openSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("⚙️ Settings");

        String[] settingsOptions = {
                "Profile Settings",
                "Notification Preferences",
                "Theme Settings",
                "Data & Storage",
                "Privacy Settings",
                "About App"
        };

        builder.setItems(settingsOptions, (dialog, which) -> {
            String selectedOption = settingsOptions[which];
            handleSettingsOption(selectedOption);
        });

        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void handleSettingsOption(String option) {
        switch (option) {
            case "Profile Settings":
                // Intent to ProfileActivity
                Toast.makeText(this, "Opening Profile Settings...", Toast.LENGTH_SHORT).show();
                break;
            case "Notification Preferences":
                showNotificationSettings();
                break;
            case "Theme Settings":
                showThemeSettings();
                break;
            default:
                Toast.makeText(this, option + " selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void showNotificationSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notification Settings");

        boolean[] checkedItems = {true, true, false, true}; // Default settings
        String[] items = {"Task Reminders", "Expense Alerts", "Contact Updates", "Weekly Summary"};

        builder.setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
            // Handle notification preference changes
            Toast.makeText(this, items[which] + (isChecked ? " enabled" : " disabled"),
                    Toast.LENGTH_SHORT).show();
        });

        builder.setPositiveButton("Save", (dialog, which) -> {
            Toast.makeText(this, "Notification preferences saved", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showThemeSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Theme Settings");

        String[] themes = {"Light Theme", "Dark Theme", "Auto (System Default)"};
        int selectedTheme = 0; // Default selection

        builder.setSingleChoiceItems(themes, selectedTheme, (dialog, which) -> {
            // Handle theme change
            Toast.makeText(this, "Theme: " + themes[which], Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Expense Functions
    private void openExpenses() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("💰 Expense Manager");

        String[] expenseOptions = {
                "Add New Expense",
                "View Expense History",
                "Monthly Report",
                "Budget Settings",
                "Export Data"
        };

        builder.setItems(expenseOptions, (dialog, which) -> {
            String selectedOption = expenseOptions[which];
            handleExpenseOption(selectedOption);
        });

        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void handleExpenseOption(String option) {
        switch (option) {
            case "Add New Expense":
                showAddExpenseDialog();
                break;
            case "View Expense History":
                showExpenseHistory();
                break;
            case "Monthly Report":
                showMonthlyReport();
                break;
            default:
                Toast.makeText(this, option + " selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddExpenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Expense");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_expense, null);
        builder.setView(dialogView);

        EditText etAmount = dialogView.findViewById(R.id.etExpenseAmount);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerExpenseCategory);
        EditText etDescription = dialogView.findViewById(R.id.etExpenseDescription);

        // Setup category spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, expenseManager.getExpenseCategories());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        builder.setPositiveButton("Add Expense", (dialog, which) -> {
            String amountStr = etAmount.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            String description = etDescription.getText().toString().trim();

            if (ValidationUtils.isEmptyOrNull(amountStr)) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!ValidationUtils.isValidAmount(amountStr)) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());

            Expense expense = new Expense(amount, category, description, date);
            expenseManager.addExpense(expense);

            // Add notification
            notificationManager.addNotification("New expense added: " +
                    ValidationUtils.formatCurrency(amount) + " for " + category);
            updateNotificationButton();

            Toast.makeText(this, "Expense added successfully!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showExpenseHistory() {
        List<Expense> expenses = expenseManager.getAllExpenses();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Expense History");

        if (expenses.isEmpty()) {
            builder.setMessage("No expenses recorded yet.");
        } else {
            StringBuilder expenseHistory = new StringBuilder("Recent Expenses:\n\n");
            double totalWeek = 0;

            for (int i = 0; i < Math.min(expenses.size(), 5); i++) {
                Expense expense = expenses.get(i);
                expenseHistory.append("• ").append(expense.getCategory())
                        .append(" - ").append(ValidationUtils.formatCurrency(expense.getAmount()))
                        .append(" (").append(expense.getDate()).append(")\n");
                totalWeek += expense.getAmount();
            }

            expenseHistory.append("\nTotal this week: ").append(ValidationUtils.formatCurrency(totalWeek));
            builder.setMessage(expenseHistory.toString());
        }

        builder.setPositiveButton("View Details", (dialog, which) -> {
            // Intent to ExpenseHistoryActivity
            Toast.makeText(this, "Opening detailed expense history...", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void showMonthlyReport() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Monthly Report - August 2024");

        String monthlyReport = "📊 Expense Summary:\n\n" +
                "🍽️ Food & Dining: $245.80\n" +
                "⛽ Transportation: $128.50\n" +
                "🛒 Shopping: $89.30\n" +
                "💡 Utilities: $156.00\n" +
                "🎯 Entertainment: $67.25\n\n" +
                "💰 Total Spent: $686.85\n" +
                "📈 vs Last Month: +12.5%";

        builder.setMessage(monthlyReport);
        builder.setPositiveButton("Export PDF", (dialog, which) -> {
            Toast.makeText(this, "Exporting monthly report...", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    // Contact Functions
    private void showContactInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("📞 Contact Information");

        String[] contactOptions = {
                "📱 My Contacts",
                "➕ Add New Contact",
                "⭐ Favorites",
                "📞 Recent Calls",
                "📧 Emergency Contacts",
                "🔄 Sync Contacts"
        };

        builder.setItems(contactOptions, (dialog, which) -> {
            String selectedOption = contactOptions[which];
            handleContactOption(selectedOption);
        });

        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void showQuickContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("📱 Quick Contact");

        String[] quickContacts = {
                "📞 Call Support: +1-800-HELP",
                "📧 Email: support@taskmanager.com",
                "💬 Live Chat",
                "🌐 Visit Website",
                "📱 SMS Support"
        };

        builder.setItems(quickContacts, (dialog, which) -> {
            handleQuickContactOption(which, quickContacts[which]);
        });

        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void handleContactOption(String option) {
        switch (option) {
            case "📱 My Contacts":
                showMyContacts();
                break;
            case "➕ Add New Contact":
                showAddContactDialog();
                break;
            case "⭐ Favorites":
                showFavoriteContacts();
                break;
            case "📞 Recent Calls":
                showRecentCalls();
                break;
            default:
                Toast.makeText(this, option + " selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleQuickContactOption(int index, String option) {
        switch (index) {
            case 0: // Call Support
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+18004357"));
                startActivity(callIntent);
                break;
            case 1: // Email
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:support@taskmanager.com"));
                startActivity(emailIntent);
                break;
            case 2: // Live Chat
                Toast.makeText(this, "Opening live chat...", Toast.LENGTH_SHORT).show();
                break;
            case 3: // Website
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.taskmanager.com"));
                startActivity(browserIntent);
                break;
            default:
                Toast.makeText(this, option + " selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMyContacts() {
        List<Contact> contacts = contactManager.getAllContacts();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("My Contacts");

        if (contacts.isEmpty()) {
            builder.setMessage("No contacts found. Add some contacts first.");
        } else {
            StringBuilder contactsList = new StringBuilder("👥 Recent Contacts:\n\n");
            for (int i = 0; i < Math.min(contacts.size(), 5); i++) {
                Contact contact = contacts.get(i);
                contactsList.append("📱 ").append(contact.getName())
                        .append(" - ").append(contact.getPhone()).append("\n");
            }
            builder.setMessage(contactsList.toString());
        }

        builder.setPositiveButton("Call Selected", (dialog, which) -> {
            Toast.makeText(this, "Select a contact to call", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Contact");

        // Create a simple text input for demo purposes
        // In a real app, you would create a proper layout with multiple EditText fields
        final EditText input = new EditText(this);
        input.setHint("Enter contact name");
        builder.setView(input);

        builder.setMessage("Contact Name: _____________\n\n" +
                "Phone Number: _____________\n\n" +
                "Email: _____________\n\n" +
                "Category: _____________");

        builder.setPositiveButton("Add Contact", (dialog, which) -> {
            String contactName = input.getText().toString().trim();
            if (!contactName.isEmpty()) {
                // Create a dummy contact for demo
                Contact newContact = new Contact("0", contactName, "555-000-0000", contactName.toLowerCase() + "@email.com");
                contactManager.addContact(newContact);
                Toast.makeText(this, "Contact added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a contact name", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showFavoriteContacts() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("⭐ Favorite Contacts");

        String[] favorites = {
                "⭐ Mom - (555) 111-1111",
                "⭐ Dad - (555) 222-2222",
                "⭐ Best Friend - (555) 333-3333",
                "⭐ Work Partner - (555) 444-4444"
        };

        builder.setItems(favorites, (dialog, which) -> {
            String selectedContact = favorites[which];
            showContactActions(selectedContact);
        });

        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void showRecentCalls() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("📞 Recent Calls");

        String recentCalls = "Recent Call History:\n\n" +
                "📞 John Smith - 2:30 PM (Outgoing)\n" +
                "📞 Sarah Johnson - 1:15 PM (Incoming)\n" +
                "📞 Unknown - 11:45 AM (Missed)\n" +
                "📞 Mike Wilson - 10:20 AM (Outgoing)\n" +
                "📞 Emma Davis - Yesterday (Incoming)";

        builder.setMessage(recentCalls);
        builder.setPositiveButton("Call Back", (dialog, which) -> {
            Toast.makeText(this, "Select a contact to call back", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void showContactActions(String contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact: " + contact);

        String[] actions = {"📞 Call", "💬 Message", "✉️ Email", "🔄 Edit", "❌ Remove from Favorites"};

        builder.setItems(actions, (dialog, which) -> {
            String action = actions[which];
            Toast.makeText(this, action + " selected for " + contact, Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Close", null);
        builder.show();
    }

    // Task Management Functions
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(year, month, dayOfMonth);
                    tvSelectedDate.setText(dateFormat.format(selectedCalendar.getTime()));
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void addNewTask() {
        String title = etTaskTitle.getText().toString().trim();
        String description = etTaskDescription.getText().toString().trim();
        String dueDate = tvSelectedDate.getText().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new task
        Task newTask = new Task(title, description, dueDate);
        taskList.add(newTask);

        // Add task to UI
        addTaskToUI(newTask);

        // Clear input fields
        etTaskTitle.setText("");
        etTaskDescription.setText("");
        tvSelectedDate.setText(dateFormat.format(new Date()));

        // Add notification
        notificationManager.addNotification("New task added: " + title);
        updateNotificationButton();

        Toast.makeText(this, "Task added successfully!", Toast.LENGTH_SHORT).show();
    }

    private void addTaskToUI(Task task) {
        try {
            View taskView = getLayoutInflater().inflate(R.layout.item_task, null);

            TextView tvTaskTitle = taskView.findViewById(R.id.tvTaskItemTitle);
            TextView tvTaskDescription = taskView.findViewById(R.id.tvTaskItemDescription);
            TextView tvTaskDate = taskView.findViewById(R.id.tvTaskItemDate);
            Button btnCompleteTask = taskView.findViewById(R.id.btnCompleteTask);

            tvTaskTitle.setText(task.getTitle());
            tvTaskDescription.setText(task.getDescription());
            tvTaskDate.setText("Due: " + task.getDueDate());

            btnCompleteTask.setOnClickListener(v -> {
                llTaskList.removeView(taskView);
                taskList.remove(task);
                Toast.makeText(this, "Task completed!", Toast.LENGTH_SHORT).show();
            });

            llTaskList.addView(taskView);
        } catch (Exception e) {
            // Fallback if layout doesn't exist - create simple text view
            TextView taskView = new TextView(this);
            taskView.setText(task.getTitle() + " - " + task.getDueDate());
            taskView.setPadding(16, 16, 16, 16);
            taskView.setBackgroundColor(0xFFE0E0E0);

            taskView.setOnClickListener(v -> {
                llTaskList.removeView(taskView);
                taskList.remove(task);
                Toast.makeText(this, "Task completed!", Toast.LENGTH_SHORT).show();
            });

            llTaskList.addView(taskView);
        }
    }

    private void loadExistingTasks() {


    }

    // Logout Function
    private void performLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Logout", (dialog, which) -> {
            // Clear user data/preferences
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

            // Navigate back to login activity
            Intent loginIntent = new Intent(this, SignInActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Task Data Model
    private static class Task {
        private String title;
        private String description;
        private String dueDate;
        private boolean completed;

        public Task(String title, String description, String dueDate) {
            this.title = title;
            this.description = description;
            this.dueDate = dueDate;
            this.completed = false;
        }

        // Getters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getDueDate() { return dueDate; }
        public boolean isCompleted() { return completed; }

        // Setters
        public void setCompleted(boolean completed) { this.completed = completed; }
    }
}