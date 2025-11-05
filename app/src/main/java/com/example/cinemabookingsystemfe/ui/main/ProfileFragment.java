package com.example.cinemabookingsystemfe.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.ui.auth.LoginActivity;
import com.example.cinemabookingsystemfe.utils.TokenManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileFragment extends Fragment {
    
    private TextView tvAvatarInitial, tvFullName, tvEmail, tvPhone, tvAddress;
    private TextView tvDateOfBirth, tvGender, tvRole, tvCreatedAt;
    private MaterialButton btnLogout;
    
    private TokenManager tokenManager;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                            @Nullable ViewGroup container, 
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        initViews(view);
        tokenManager = TokenManager.getInstance(requireContext());
        
        loadMockProfile();
        setupListeners();
        
        return view;
    }
    
    private void initViews(View view) {
        tvAvatarInitial = view.findViewById(R.id.tvAvatarInitial);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvDateOfBirth = view.findViewById(R.id.tvDateOfBirth);
        tvGender = view.findViewById(R.id.tvGender);
        tvRole = view.findViewById(R.id.tvRole);
        tvCreatedAt = view.findViewById(R.id.tvCreatedAt);
        btnLogout = view.findViewById(R.id.btnLogout);
    }
    
    private void loadMockProfile() {
        // Mock data based on API documentation
        // GET /api/customers/profile response
        String fullName = "Nguyễn Văn A";
        
        tvFullName.setText(fullName);
        tvEmail.setText("customer@example.com");
        tvPhone.setText("0901234567");
        tvAddress.setText("123 Nguyễn Huệ, Quận 1, TP.HCM");
        tvDateOfBirth.setText("15/05/1995");
        tvGender.setText("Nam");
        tvRole.setText("Khách hàng");
        tvCreatedAt.setText("01/10/2025");
        
        // Set avatar initial (first letters of first and last name)
        setAvatarInitial(fullName);
    }
    
    private void setAvatarInitial(String fullName) {
        if (fullName != null && !fullName.isEmpty()) {
            String[] nameParts = fullName.trim().split("\\s+");
            String initial;
            
            if (nameParts.length >= 2) {
                // Take first letter of first word and last word
                initial = nameParts[0].substring(0, 1).toUpperCase() + 
                         nameParts[nameParts.length - 1].substring(0, 1).toUpperCase();
            } else {
                // Take first two letters or just first letter
                initial = nameParts[0].substring(0, Math.min(2, nameParts[0].length())).toUpperCase();
            }
            
            tvAvatarInitial.setText(initial);
        }
    }
    
    private void setupListeners() {
        btnLogout.setOnClickListener(v -> showLogoutConfirmation());
    }
    
    private void showLogoutConfirmation() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Đăng Xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng Xuất", (dialog, which) -> performLogout())
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    private void performLogout() {
        // Clear tokens
        tokenManager.clearTokens();
        
        // Show success message
        Toast.makeText(requireContext(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
        
        // Navigate back to Home tab (tab 0)
        // MainActivity will handle switching to home tab
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToHome();
        }
    }
}
