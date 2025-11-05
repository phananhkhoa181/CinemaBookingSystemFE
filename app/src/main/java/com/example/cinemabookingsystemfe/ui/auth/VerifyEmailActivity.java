package com.example.cinemabookingsystemfe.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.request.ResendOtpRequest;
import com.example.cinemabookingsystemfe.data.models.request.SendOtpRequest;
import com.example.cinemabookingsystemfe.data.models.request.VerifyOtpRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.SendOtpResponse;
import com.example.cinemabookingsystemfe.data.models.response.VerifyOtpResponse;
import com.example.cinemabookingsystemfe.data.repository.AuthRepository;
import com.example.cinemabookingsystemfe.ui.main.MainActivity;
import com.google.android.material.button.MaterialButton;

/**
 * VerifyEmailActivity - OTP Email Verification
 * 
 * Features:
 * - 6-digit OTP input with auto-focus
 * - 10-minute countdown timer
 * - Auto-enable Resend button when timer expires
 * - Navigate to MainActivity after successful verification
 */
public class VerifyEmailActivity extends AppCompatActivity {

    // UI Components
    private ImageButton btnBack;
    private TextView tvEmail, tvTimer, btnResend;
    private EditText etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6;
    private MaterialButton btnVerify;
    private ProgressBar progressBar;
    
    // Data
    private String email;
    private String otpType;
    private AuthRepository authRepository;
    private CountDownTimer countDownTimer;
    private static final long OTP_EXPIRY_TIME = 10 * 60 * 1000; // 10 minutes in milliseconds
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        
        // Initialize Repository
        authRepository = AuthRepository.getInstance(this);
        
        // Get data from Intent
        email = getIntent().getStringExtra("email");
        otpType = getIntent().getStringExtra("otpType");
        
        if (email == null || otpType == null) {
            Toast.makeText(this, "Lỗi: Không nhận được thông tin email", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize Views
        initViews();
        
        // Setup OTP inputs
        setupOtpInputs();
        
        // Start countdown timer
        startCountdownTimer();
        
        // Set click listeners
        btnBack.setOnClickListener(v -> onBackPressed());
        btnVerify.setOnClickListener(v -> verifyOtp());
        btnResend.setOnClickListener(v -> resendOtp());
    }
    
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvEmail = findViewById(R.id.tvEmail);
        tvTimer = findViewById(R.id.tvTimer);
        btnResend = findViewById(R.id.btnResend);
        etOtp1 = findViewById(R.id.etOtp1);
        etOtp2 = findViewById(R.id.etOtp2);
        etOtp3 = findViewById(R.id.etOtp3);
        etOtp4 = findViewById(R.id.etOtp4);
        etOtp5 = findViewById(R.id.etOtp5);
        etOtp6 = findViewById(R.id.etOtp6);
        btnVerify = findViewById(R.id.btnVerify);
        progressBar = findViewById(R.id.progressBar);
        
        // Display email
        tvEmail.setText(email);
        
        // Disable Resend button initially
        btnResend.setEnabled(false);
        btnResend.setAlpha(0.5f);
    }
    
    /**
     * Setup OTP inputs with auto-focus and backspace handling
     */
    private void setupOtpInputs() {
        EditText[] otpFields = {etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6};
        
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        // Move to next field
                        otpFields[index + 1].requestFocus();
                    }
                }
                
                @Override
                public void afterTextChanged(Editable s) {}
            });
            
            // Handle backspace
            otpFields[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (otpFields[index].getText().toString().isEmpty() && index > 0) {
                        otpFields[index - 1].requestFocus();
                        otpFields[index - 1].setText("");
                    }
                }
                return false;
            });
        }
        
        // Focus first field
        etOtp1.requestFocus();
    }
    
    /**
     * Start 10-minute countdown timer
     */
    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(OTP_EXPIRY_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                tvTimer.setText(String.format("OTP hết hạn sau: %02d:%02d", minutes, seconds));
            }
            
            @Override
            public void onFinish() {
                tvTimer.setText("OTP đã hết hạn");
                tvTimer.setTextColor(getResources().getColor(R.color.error, null));
                
                // Enable Resend button
                btnResend.setEnabled(true);
                btnResend.setAlpha(1.0f);
                
                Toast.makeText(VerifyEmailActivity.this, 
                    "OTP đã hết hạn. Vui lòng gửi lại mã mới.", 
                    Toast.LENGTH_LONG).show();
            }
        }.start();
    }
    
    /**
     * Get OTP code from all 6 fields
     */
    private String getOtpCode() {
        return etOtp1.getText().toString() +
               etOtp2.getText().toString() +
               etOtp3.getText().toString() +
               etOtp4.getText().toString() +
               etOtp5.getText().toString() +
               etOtp6.getText().toString();
    }
    
    /**
     * Verify OTP code
     */
    private void verifyOtp() {
        String otpCode = getOtpCode();
        
        if (otpCode.length() != 6) {
            Toast.makeText(this, "Vui lòng nhập đủ 6 số", Toast.LENGTH_SHORT).show();
            return;
        }
        
        setLoading(true);
        
        VerifyOtpRequest request = new VerifyOtpRequest(email, otpCode, otpType);
        
        authRepository.verifyOtp(request, new ApiCallback<ApiResponse<VerifyOtpResponse>>() {
            @Override
            public void onSuccess(ApiResponse<VerifyOtpResponse> response) {
                setLoading(false);
                
                if (response.isSuccess() && response.getData() != null) {
                    VerifyOtpResponse data = response.getData();
                    
                    // Cancel timer
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    
                    Toast.makeText(VerifyEmailActivity.this, 
                        "Xác thực thành công! Vui lòng đăng nhập.", 
                        Toast.LENGTH_LONG).show();
                    
                    // Navigate to LoginActivity after successful verification
                    Intent intent = new Intent(VerifyEmailActivity.this, LoginActivity.class);
                    intent.putExtra("verified_email", email);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(VerifyEmailActivity.this, 
                        response.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                Toast.makeText(VerifyEmailActivity.this, 
                    "Lỗi: " + errorMessage, 
                    Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Resend OTP code
     */
    private void resendOtp() {
        setLoading(true);
        
        ResendOtpRequest request = new ResendOtpRequest(email, otpType);
        
        authRepository.resendOtp(request, new ApiCallback<ApiResponse<SendOtpResponse>>() {
            @Override
            public void onSuccess(ApiResponse<SendOtpResponse> response) {
                setLoading(false);
                
                if (response.isSuccess() && response.getData() != null) {
                    SendOtpResponse data = response.getData();
                    
                    Toast.makeText(VerifyEmailActivity.this, 
                        "Đã gửi lại mã OTP. Vui lòng kiểm tra email.", 
                        Toast.LENGTH_SHORT).show();
                    
                    // Clear OTP fields
                    clearOtpFields();
                    
                    // Restart countdown timer
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    startCountdownTimer();
                    
                    // Disable Resend button again
                    btnResend.setEnabled(false);
                    btnResend.setAlpha(0.5f);
                    
                    // Reset timer color
                    tvTimer.setTextColor(getResources().getColor(R.color.primary, null));
                } else {
                    Toast.makeText(VerifyEmailActivity.this, 
                        response.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                Toast.makeText(VerifyEmailActivity.this, 
                    "Lỗi: " + errorMessage, 
                    Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Clear all OTP input fields
     */
    private void clearOtpFields() {
        etOtp1.setText("");
        etOtp2.setText("");
        etOtp3.setText("");
        etOtp4.setText("");
        etOtp5.setText("");
        etOtp6.setText("");
        etOtp1.requestFocus();
    }
    
    /**
     * Show/hide loading state
     */
    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnVerify.setEnabled(!isLoading);
        btnBack.setEnabled(!isLoading);
        
        if (isLoading) {
            btnVerify.setAlpha(0.5f);
        } else {
            btnVerify.setAlpha(1.0f);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
