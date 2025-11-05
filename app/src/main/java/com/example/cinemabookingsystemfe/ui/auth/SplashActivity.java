package com.example.cinemabookingsystemfe.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cinemabookingsystemfe.R;
// MainActivity import - uses ui.main package

/**
 * SplashActivity - Màn hình chào khi khởi động app
 * 
 * TODO: 
 * - Check JWT token expiry
 * - Auto-login nếu token còn valid
 * - Navigate to LoginActivity nếu token expired
 * 
 * ASSIGNED TO: Developer 1
 * PRIORITY: HIGH
 * REFER TO: docs-FE/03-Screens/01-Auth.md (Line 1-100)
 */
public class SplashActivity extends AppCompatActivity {
    
    private static final int SPLASH_DURATION = 2000; // 2 seconds
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_splash);
            
            // Always go to MainActivity after splash delay
            // Login check will be done when user tries to book ticket
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                try {
                    Intent intent = new Intent(SplashActivity.this, com.example.cinemabookingsystemfe.ui.main.MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    // If error, still try to go to MainActivity
                    Intent intent = new Intent(SplashActivity.this, com.example.cinemabookingsystemfe.ui.main.MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_DURATION);
        } catch (Exception e) {
            e.printStackTrace();
            // If layout fails, go directly to LoginActivity
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
