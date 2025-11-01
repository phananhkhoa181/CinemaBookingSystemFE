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
            
            // Check if user is logged in after splash delay
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                try {
                    com.example.cinemabookingsystemfe.utils.SharedPrefsManager prefsManager = 
                        com.example.cinemabookingsystemfe.utils.SharedPrefsManager.getInstance(this);
                    
                    Intent intent;
                    if (prefsManager.isLoggedIn()) {
                        // User already logged in -> go to MainActivity
                        intent = new Intent(SplashActivity.this, com.example.cinemabookingsystemfe.ui.main.MainActivity.class);
                    } else {
                        // Not logged in -> go to LoginActivity
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                    
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    // If error, go to LoginActivity
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
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
