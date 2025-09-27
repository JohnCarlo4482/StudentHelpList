package com.example.studentlist2025;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize database
        userDatabase = UserDatabase.getInstance(this);

        // Initialize UI components
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        // Set up login button click listener
        btnLogin.setOnClickListener(v -> handleLogin());

        // Set up sign-up text click listener - now navigates to SignUpActivity
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
            // Use standard Android transitions or remove if animations aren't needed
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            showToast("Please fill in all fields");
            return;
        }

        // Check credentials against database
        if (validateCredentials(username, password)) {
            Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
            intent.putExtra("username", username); // Pass username to dashboard
            startActivity(intent);
            // Use standard Android transitions
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish(); // Close SignInActivity to prevent back navigation
        } else {
            showToast("Invalid username or password");
            // Clear password field for security
            etPassword.setText("");
        }
    }

    private boolean validateCredentials(String username, String password) {
        // Use database to check credentials instead of hardcoded values
        return userDatabase.checkUser(username, password);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close database connection if needed
        if (userDatabase != null) {
            userDatabase.close();
        }
    }
}