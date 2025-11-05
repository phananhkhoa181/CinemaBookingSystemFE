package com.example.cinemabookingsystemfe.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.ui.adapters.MainPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity - Màn hình chính với 3 tabs: Home, Booking History, Profile
 */
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setupViews();
    }
    
    private void setupViews() {
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        
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
                        bottomNav.setSelectedItemId(R.id.nav_bookings);
                        break;
                    case 2:
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
            } else if (itemId == R.id.nav_bookings) {
                viewPager.setCurrentItem(1, false);
                return true;
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(2, false);
                return true;
            }
            return false;
        });
    }
}
