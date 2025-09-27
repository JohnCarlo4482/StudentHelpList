package com.example.studentlist2025;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView loadingIcon = findViewById(R.id.loadingIcon);
        Button btnGetStarted = findViewById(R.id.btnGetStarted);

        if (loadingIcon != null && btnGetStarted != null) {
            // Start the loading animation if resources are available
            loadingIcon.setVisibility(View.VISIBLE);
            try {
                loadingIcon.setImageResource(R.drawable.ic_loading);
            } catch (Exception e) {
                loadingIcon.setVisibility(View.GONE); // Hide if animation fails
            }

            Animation fadeIn = AnimationUtils.loadAnimation(this, R.transition.fade_in);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (loadingIcon != null) loadingIcon.setVisibility(View.GONE);
                if (btnGetStarted != null) {
                    btnGetStarted.setVisibility(View.VISIBLE);
                    btnGetStarted.startAnimation(fadeIn);
                }
            }, 4000);

            btnGetStarted.setOnClickListener(v -> {
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_in_from_right, R.transition.slide_out_to_left);
                finish();
            });
        } else {
            // Fallback if views are not found (log or start next activity directly)
            Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }
    }
}