package com.example.cinemabookingsystemfe.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.cinemabookingsystemfe.R;

/**
 * ProfileFragment - Thông tin cá nhân & menu
 * 
 * TODO:
 * - Create layout: fragment_profile.xml
 * - User info card (avatar, name, email)
 * - Menu items: Edit Profile, Change Password, Settings, Logout
 * - Handle logout (clear SharedPrefs, navigate to Login)
 * 
 * ASSIGNED TO: Developer 5
 * PRIORITY: LOW
 * REFER TO: docs-FE/03-Screens/06-Profile.md
 */
public class ProfileFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        // TODO: Implement
        
        return view;
    }
}
