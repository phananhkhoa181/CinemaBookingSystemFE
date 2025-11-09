package com.example.cinemabookingsystemfe.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.ui.adapters.MainPagerAdapter;
import com.example.cinemabookingsystemfe.ui.auth.LoginActivity;
import com.example.cinemabookingsystemfe.utils.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity - Màn hình chính với 3 tabs: Home, Booking History, Profile
 */
public class MainActivity extends AppCompatActivity {
    
    private TokenManager tokenManager;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tokenManager = TokenManager.getInstance(this);
        setupViews();
        
        // Check for navigation intent
        handleNavigationIntent();
    }
    
    private void handleNavigationIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("NAVIGATE_TO_TAB")) {
            String tab = intent.getStringExtra("NAVIGATE_TO_TAB");
            if ("BOOKINGS".equals(tab)) {
                // Navigate to bookings tab after a short delay to ensure views are ready
                viewPager.postDelayed(() -> {
                    viewPager.setCurrentItem(1, false);
                    bottomNav.setSelectedItemId(R.id.nav_bookings);
                }, 100);
            }
        }
    }
    
    private void setupViews() {
        viewPager = findViewById(R.id.viewPager);
        bottomNav = findViewById(R.id.bottomNavigation);
        
        // Setup ViewPager with fragments
        MainPagerAdapter adapter = new MainPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false); // Disable swipe
        
        // Sync ViewPager with BottomNavigation
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNav.setSelectedItemId(R.id.nav_home);
                        break;
                    case 1:
                        bottomNav.setSelectedItemId(R.id.nav_cinemas);
                        break;
                    case 2:
                        bottomNav.setSelectedItemId(R.id.nav_bookings);
                        break;
                    case 3:
                        bottomNav.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }
        });
        
        // Handle bottom navigation item selection
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                viewPager.setCurrentItem(0, false);
                return true;
            } else if (itemId == R.id.nav_cinemas) {
                viewPager.setCurrentItem(1, false);
                return true;
            } else if (itemId == R.id.nav_bookings) {
                // Check login before accessing Bookings
                if (checkLoginRequired("Lịch sử")) {
                    viewPager.setCurrentItem(2, false);
                    return true;
                }
                return false;
            } else if (itemId == R.id.nav_profile) {
                // Check login before accessing Profile
                if (checkLoginRequired("Tài khoản")) {
                    viewPager.setCurrentItem(3, false);
                    return true;
                }
                return false;
            }
            return false;
        });
    }
    
    /**
     * Check if user is logged in
     * If not, redirect to Login screen
     * @param featureName Name of the feature being accessed
     * @return true if logged in, false otherwise
     */
    private boolean checkLoginRequired(String featureName) {
        String token = tokenManager.getToken();
        
        if (token == null || token.isEmpty() || tokenManager.isTokenExpired()) {
            // Show message
            Toast.makeText(this, "Vui lòng đăng nhập để truy cập " + featureName, Toast.LENGTH_SHORT).show();
            
            // Redirect to Login (don't finish MainActivity)
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 100); // Use startActivityForResult to come back
            
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // After login, check if successful and navigate to requested tab
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // User logged in successfully, fragments will reload automatically
        } else {
            // Login cancelled, stay on Home tab
            viewPager.setCurrentItem(0, false);
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }
    
    /**
     * Navigate to Home tab (called after logout)
     */
    public void navigateToHome() {
        viewPager.setCurrentItem(0, true);
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}
