package com.example.cinemabookingsystemfe.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.response.SendOtpResponse;
import com.example.cinemabookingsystemfe.data.repository.AuthRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * ForgotPasswordActivity - Password recovery screen
 * Step 1: User enters email → Backend sends OTP email
 * Step 2: Navigate to ResetPasswordActivity to enter OTP + new password
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    
    private TextInputEditText etEmail;
    private MaterialButton btnSend;
    private ProgressBar progressBar;
    private ImageButton btnBack;
    private TextView tvBackToLogin;
    private AuthRepository authRepository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        
        authRepository = AuthRepository.getInstance(this);
        
        initViews();
        setupListeners();
    }
    
    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        btnSend = findViewById(R.id.btnSend);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }
    
    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        tvBackToLogin.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> attemptSendEmail());
    }
    
    private void attemptSendEmail() {
        String email = etEmail.getText().toString().trim();
        
        // Validate email
        if (!isValidEmail(email)) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }
        
        // Show loading
        setLoading(true);
        
        // Call forgot-password API - Backend will send OTP email (type: PasswordReset)
        authRepository.forgotPassword(email, new ApiCallback<SendOtpResponse>() {
            @Override
            public void onSuccess(SendOtpResponse response) {
                setLoading(false);
                
                android.util.Log.d("ForgotPassword", "=== OTP SENT SUCCESSFULLY ===");
                android.util.Log.d("ForgotPassword", "Email: " + email);
                android.util.Log.d("ForgotPassword", "Response email: " + response.getEmail());
                
                Toast.makeText(ForgotPasswordActivity.this, 
                    "✅ Mã OTP đã được gửi đến email: " + response.getEmail() + 
                    "\nVui lòng kiểm tra hộp thư của bạn.", 
                    Toast.LENGTH_LONG).show();
                
                // Navigate to ResetPasswordActivity to enter OTP + new password
                // Delay slightly to ensure toast is visible
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("email", email);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            
                            android.util.Log.d("ForgotPassword", "Starting ResetPasswordActivity with email: " + email);
                            startActivity(intent);
                            
                            // Don't finish immediately - let the new activity start first
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 300);
                            
                        } catch (Exception e) {
                            android.util.Log.e("ForgotPassword", "Error starting ResetPasswordActivity: " + e.getMessage(), e);
                            Toast.makeText(ForgotPasswordActivity.this, 
                                "❌ Lỗi chuyển trang: " + e.getMessage(), 
                                Toast.LENGTH_LONG).show();
                        }
                    }
                }, 500);
            }
            
            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                
                android.util.Log.e("ForgotPassword", "=== ERROR FROM BACKEND ===");
                android.util.Log.e("ForgotPassword", errorMessage);
                android.util.Log.e("ForgotPassword", "==========================");
                
                // Display the exact error message from backend for debugging
                String displayMessage = errorMessage;
                
                // Only translate if it's a generic error
                if (errorMessage == null || errorMessage.isEmpty()) {
                    displayMessage = "Gửi OTP thất bại - Không nhận được thông báo lỗi từ server";
                } else if (errorMessage.equals("Failed to send OTP")) {
                    displayMessage = "Gửi OTP thất bại - Vui lòng thử lại";
                }
                
                Toast.makeText(ForgotPasswordActivity.this, 
                    "❌ Lỗi: " + displayMessage, 
                    Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void setLoading(boolean isLoading) {
        btnSend.setEnabled(!isLoading);
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        
        if (isLoading) {
            btnSend.setText("Đang gửi OTP...");
            btnSend.setAlpha(0.6f);
        } else {
            btnSend.setText("Gửi mã OTP");
            btnSend.setAlpha(1.0f);
        }
    }
    
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
