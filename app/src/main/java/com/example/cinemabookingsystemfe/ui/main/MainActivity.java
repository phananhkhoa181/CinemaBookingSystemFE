package com.example.cinemabookingsystemfe.ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.ui.auth.LoginActivity;
import com.example.cinemabookingsystemfe.ui.booking.SelectCinemaActivity;
import com.example.cinemabookingsystemfe.utils.SharedPrefsManager;
import com.google.android.material.button.MaterialButton;

/**
 * MainActivity - Màn hình chính sau khi đăng nhập
 * 
 * TODO:
 * - Create layout: activity_main.xml (ViewPager2 + BottomNavigationView)
 * - Setup 3 fragments: HomeFragment, BookingHistoryFragment, ProfileFragment
 * - Disable swipe trên ViewPager2
 * - Sync BottomNav với ViewPager
 * 
 * ASSIGNED TO: Developer 2
 * PRIORITY: HIGH
 * REFER TO: docs-FE/03-Screens/02-Main.md (Line 1-150)
 */
public class MainActivity extends AppCompatActivity {
    
    // TODO: TEMPORARY - Remove btnTestBooking when real HomeFragment is implemented
    private MaterialButton btnTestBooking;
    private MaterialButton btnLogout;
    private SharedPrefsManager prefsManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        prefsManager = SharedPrefsManager.getInstance(this);
        
        // TODO: TEMPORARY - Remove this section when real HomeFragment is implemented
        btnTestBooking = findViewById(R.id.btnTestBooking);
        btnTestBooking.setOnClickListener(v -> testBookingFlow());
        
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logout());
    }
    
    // TODO: TEMPORARY - Remove this method when real HomeFragment is implemented
    private void testBookingFlow() {
        Intent intent = new Intent(this, SelectCinemaActivity.class);
        intent.putExtra(SelectCinemaActivity.EXTRA_MOVIE_ID, 1); // Mock movie ID for testing
        startActivity(intent);
    }
    
    private void logout() {
        // Clear all user data
        prefsManager.clearAll();
        
        // Navigate to LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
