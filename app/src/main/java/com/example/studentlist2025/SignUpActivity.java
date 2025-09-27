package com.example.studentlist2025;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText etEmail, etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvSignIn;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize database
        userDatabase = UserDatabase.getInstance(this);

        // Initialize UI components
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvSignIn = findViewById(R.id.tvSignIn);

        // Set up register button click listener
        btnRegister.setOnClickListener(v -> handleRegistration());

        // Set up sign-in text click listener
        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish(); // Close signup activity
        });
    }

    private void handleRegistration() {
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(email, username, password, confirmPassword)) {
            return;
        }

        // Check if username already exists
        if (userDatabase.userExists(username)) {
            showToast("Username already exists. Please choose a different one.");
            return;
        }

        // Check if email already exists
        if (userDatabase.emailExists(email)) {
            showToast("Email already registered. Please use a different email.");
            return;
        }

        // Attempt to register user
        if (userDatabase.addUser(username, password, email)) {
            showToast("Registration Successful!");
            // Navigate to sign-in activity
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            intent.putExtra("registered_username", username); // Pass username to pre-fill
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        } else {
            showToast("Registration Failed. Please try again.");
        }
    }

    private boolean validateInputs(String email, String username, String password, String confirmPassword) {
        // Check for empty fields
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Please fill in all fields");
            return false;
        }

        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email address");
            etEmail.requestFocus();
            return false;
        }

        // Validate username length
        if (username.length() < 3) {
            showToast("Username must be at least 3 characters long");
            etUsername.requestFocus();
            return false;
        }

        // Validate password strength
        if (password.length() < 6) {
            showToast("Password must be at least 6 characters long");
            etPassword.requestFocus();
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
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