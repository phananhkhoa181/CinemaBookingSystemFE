package com.example.cinemabookingsystemfe.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.response.ResetPasswordResponse;
import com.example.cinemabookingsystemfe.data.models.response.SendOtpResponse;
import com.example.cinemabookingsystemfe.data.repository.AuthRepository;
import com.google.android.material.textfield.TextInputEditText;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextView tvEmail, tvTimer, btnResend, tvBackToLogin;
    private EditText etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6;
    private TextInputEditText etNewPassword, etConfirmPassword;
    private Button btnResetPassword;
    private ImageButton btnBack;

    private String email;
    private CountDownTimer countDownTimer;
    private AuthRepository authRepository;
    private boolean isResendEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        android.util.Log.d("ResetPassword", "=== onCreate START ===");
        
        try {
            android.util.Log.d("ResetPassword", "Setting content view...");
            setContentView(R.layout.activity_reset_password);
            android.util.Log.d("ResetPassword", "✅ Content view set");

            // Get email from Intent
            email = getIntent().getStringExtra("email");
            android.util.Log.d("ResetPassword", "Email from intent: " + email);
            
            if (email == null || email.isEmpty()) {
                Toast.makeText(this, "Email không được cung cấp", Toast.LENGTH_SHORT).show();
                android.util.Log.e("ResetPassword", "❌ Email is null or empty");
                
                // Navigate back to ForgotPasswordActivity instead of finish
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
                return;
            }

            android.util.Log.d("ResetPassword", "Getting AuthRepository instance...");
            authRepository = AuthRepository.getInstance(this);
            android.util.Log.d("ResetPassword", "✅ AuthRepository initialized");

            android.util.Log.d("ResetPassword", "Initializing views...");
            initViews();
            android.util.Log.d("ResetPassword", "✅ Views initialized");
            
            android.util.Log.d("ResetPassword", "Setting up OTP inputs...");
            setupOtpInputs();
            android.util.Log.d("ResetPassword", "✅ OTP inputs setup");
            
            android.util.Log.d("ResetPassword", "Setting up click listeners...");
            setupClickListeners();
            android.util.Log.d("ResetPassword", "✅ Click listeners setup");
            
            android.util.Log.d("ResetPassword", "Starting timer...");
            startTimer();
            android.util.Log.d("ResetPassword", "✅ Timer started");
            
            android.util.Log.d("ResetPassword", "=== ✅ Activity initialized successfully ===");
        } catch (android.content.res.Resources.NotFoundException e) {
            android.util.Log.e("ResetPassword", "❌❌❌ RESOURCE NOT FOUND: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi: Thiếu resource - " + e.getMessage(), Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);
        } catch (Exception e) {
            android.util.Log.e("ResetPassword", "❌❌❌ CRITICAL ERROR in onCreate: " + e.getMessage(), e);
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getClass().getSimpleName() + " - " + e.getMessage(), Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);
        }
    }

    private void initViews() {
        android.util.Log.d("ResetPassword", "Initializing views...");
        
        tvEmail = findViewById(R.id.tvEmail);
        tvTimer = findViewById(R.id.tvTimer);
        btnResend = findViewById(R.id.btnResend);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        btnBack = findViewById(R.id.btnBack);

        etOtp1 = findViewById(R.id.etOtp1);
        etOtp2 = findViewById(R.id.etOtp2);
        etOtp3 = findViewById(R.id.etOtp3);
        etOtp4 = findViewById(R.id.etOtp4);
        etOtp5 = findViewById(R.id.etOtp5);
        etOtp6 = findViewById(R.id.etOtp6);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        // Check for null views and show error if any critical view is missing
        boolean hasNullViews = false;
        StringBuilder nullViews = new StringBuilder("Null views: ");
        
        if (tvEmail == null) { android.util.Log.e("ResetPassword", "❌ tvEmail is null!"); nullViews.append("tvEmail, "); hasNullViews = true; }
        if (tvTimer == null) { android.util.Log.e("ResetPassword", "❌ tvTimer is null!"); nullViews.append("tvTimer, "); hasNullViews = true; }
        if (btnResend == null) { android.util.Log.e("ResetPassword", "❌ btnResend is null!"); nullViews.append("btnResend, "); hasNullViews = true; }
        if (etOtp1 == null) { android.util.Log.e("ResetPassword", "❌ etOtp1 is null!"); nullViews.append("etOtp1, "); hasNullViews = true; }
        if (etNewPassword == null) { android.util.Log.e("ResetPassword", "❌ etNewPassword is null!"); nullViews.append("etNewPassword, "); hasNullViews = true; }
        if (btnResetPassword == null) { android.util.Log.e("ResetPassword", "❌ btnResetPassword is null!"); nullViews.append("btnResetPassword, "); hasNullViews = true; }
        
        if (hasNullViews) {
            String errorMsg = nullViews.toString();
            android.util.Log.e("ResetPassword", "CRITICAL: " + errorMsg);
            Toast.makeText(this, "Lỗi layout: " + errorMsg, Toast.LENGTH_LONG).show();
            // Don't finish - let user see the error
        }

        // Display email
        if (tvEmail != null) {
            tvEmail.setText(email);
            android.util.Log.d("ResetPassword", "✅ Email displayed: " + email);
        } else {
            android.util.Log.e("ResetPassword", "❌ Cannot display email - tvEmail is null");
        }
        
        android.util.Log.d("ResetPassword", "✅ Views initialized successfully");
    }

    private void setupOtpInputs() {
        setupOtpAutoMove(etOtp1, etOtp2);
        setupOtpAutoMove(etOtp2, etOtp3);
        setupOtpAutoMove(etOtp3, etOtp4);
        setupOtpAutoMove(etOtp4, etOtp5);
        setupOtpAutoMove(etOtp5, etOtp6);
        setupOtpAutoMove(etOtp6, null);
    }

    private void setupOtpAutoMove(EditText current, EditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && next != null) {
                    next.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupClickListeners() {
        android.util.Log.d("ResetPassword", "Setting up click listeners...");
        
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (tvBackToLogin != null) {
            tvBackToLogin.setOnClickListener(v -> {
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            });
        }

        if (btnResend != null) {
            btnResend.setOnClickListener(v -> {
                if (isResendEnabled) {
                    resendOtp();
                }
            });
        }

        if (btnResetPassword != null) {
            btnResetPassword.setOnClickListener(v -> resetPassword());
        }
        
        android.util.Log.d("ResetPassword", "✅ Click listeners setup complete");
    }

    private void startTimer() {
        // 10 minutes = 600,000 milliseconds
        countDownTimer = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                tvTimer.setText(String.format("Code expires in: %02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("OTP Expired");
                tvTimer.setTextColor(getColor(R.color.error));
                btnResend.setEnabled(true);
                isResendEnabled = true;
            }
        }.start();
    }

    private void resendOtp() {
        btnResend.setEnabled(false);
        isResendEnabled = false;

        // Use resendOtp API instead of forgotPassword
        com.example.cinemabookingsystemfe.data.models.request.ResendOtpRequest request = 
            new com.example.cinemabookingsystemfe.data.models.request.ResendOtpRequest(email, "PasswordReset");
            
        authRepository.resendOtp(request, new ApiCallback<com.example.cinemabookingsystemfe.data.models.response.ApiResponse<SendOtpResponse>>() {
            @Override
            public void onSuccess(com.example.cinemabookingsystemfe.data.models.response.ApiResponse<SendOtpResponse> response) {
                if (response.isSuccess() && response.getData() != null) {
                    Toast.makeText(ResetPasswordActivity.this, 
                        "Mã OTP mới đã được gửi. Vui lòng kiểm tra email.", 
                        Toast.LENGTH_SHORT).show();
                    
                    // Clear OTP fields
                    etOtp1.setText("");
                    etOtp2.setText("");
                    etOtp3.setText("");
                    etOtp4.setText("");
                    etOtp5.setText("");
                    etOtp6.setText("");
                    etOtp1.requestFocus();

                    // Restart timer
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    startTimer();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, 
                        "Không thể gửi lại OTP: " + (response.getMessage() != null ? response.getMessage() : "Unknown error"), 
                        Toast.LENGTH_SHORT).show();
                    btnResend.setEnabled(true);
                    isResendEnabled = true;
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ResetPasswordActivity.this, 
                    "Không thể gửi lại OTP: " + error, 
                    Toast.LENGTH_SHORT).show();
                btnResend.setEnabled(true);
                isResendEnabled = true;
            }
        });
    }

    private void resetPassword() {
        // Get OTP code
        String otpCode = etOtp1.getText().toString() +
                        etOtp2.getText().toString() +
                        etOtp3.getText().toString() +
                        etOtp4.getText().toString() +
                        etOtp5.getText().toString() +
                        etOtp6.getText().toString();

        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation
        if (otpCode.length() != 6) {
            Toast.makeText(this, "Please enter 6-digit OTP code", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.isEmpty()) {
            etNewPassword.setError("Password is required");
            etNewPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Password must be at least 6 characters");
            etNewPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        // Disable button to prevent double submission
        btnResetPassword.setEnabled(false);
        btnResetPassword.setText("Resetting...");

        // Call API
        authRepository.resetPassword(email, otpCode, newPassword, confirmPassword, 
            new ApiCallback<ResetPasswordResponse>() {
                @Override
                public void onSuccess(ResetPasswordResponse response) {
                    Toast.makeText(ResetPasswordActivity.this, 
                        "Password reset successful! You can now login with your new password.", 
                        Toast.LENGTH_LONG).show();

                    // Navigate to LoginActivity
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(ResetPasswordActivity.this, 
                        "Failed to reset password: " + error, 
                        Toast.LENGTH_SHORT).show();
                    
                    // Re-enable button
                    btnResetPassword.setEnabled(true);
                    btnResetPassword.setText("Reset Password");
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
