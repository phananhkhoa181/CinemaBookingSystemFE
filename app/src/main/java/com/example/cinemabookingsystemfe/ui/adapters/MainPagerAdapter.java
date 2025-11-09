package com.example.cinemabookingsystemfe.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cinemabookingsystemfe.ui.main.HomeFragment;
import com.example.cinemabookingsystemfe.ui.main.CinemasFragment;
import com.example.cinemabookingsystemfe.ui.main.BookingHistoryFragment;
import com.example.cinemabookingsystemfe.ui.profile.ProfileFragment;

public class MainPagerAdapter extends FragmentStateAdapter {
    
    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new CinemasFragment();
            case 2:
                return new BookingHistoryFragment();
            case 3:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }
    
    @Override
    public int getItemCount() {
        return 4; // Changed from 3 to 4
    }
}
