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
import com.example.cinemabookingsystemfe.data.models.request.RegisterRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.RegisterResponse;
import com.example.cinemabookingsystemfe.data.repository.AuthRepository;
import com.example.cinemabookingsystemfe.ui.main.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * RegisterActivity - Professional registration screen
 */
public class RegisterActivity extends AppCompatActivity {
    
    private TextInputEditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;
    private ImageButton btnBack;
    private TextView tvLogin;
    
    private AuthRepository authRepository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        initViews();
        setupListeners();
    }
    
    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        tvLogin = findViewById(R.id.tvLogin);
        
        authRepository = AuthRepository.getInstance(this);
    }
    
    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnRegister.setOnClickListener(v -> attemptRegister());
        
        tvLogin.setOnClickListener(v -> finish());
    }
    
    private void attemptRegister() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        
        // Validate inputs
        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return;
        }
        
        if (!isValidEmail(email)) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }
        
        if (phone.isEmpty() || phone.length() < 10) {
            etPhone.setError("Số điện thoại không hợp lệ");
            etPhone.requestFocus();
            return;
        }
        
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return;
        }
         
        // Show loading
        setLoading(true);
        
        // Call real API - backend expects: fullname, email, password, confirmPassword, phone
        RegisterRequest request = new RegisterRequest(fullName, email, password, confirmPassword, phone);
        
        // Debug: Log request data
        android.util.Log.d("RegisterActivity", "=== REGISTER REQUEST ===");
        android.util.Log.d("RegisterActivity", "Request Object: " + request.toString());
        android.util.Log.d("RegisterActivity", "Full Name: " + fullName);
        android.util.Log.d("RegisterActivity", "Email: " + email);
        android.util.Log.d("RegisterActivity", "Phone: " + phone);
        android.util.Log.d("RegisterActivity", "Phone from request: " + request.getPhone());
        
        // Verify Gson serialization
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String jsonRequest = gson.toJson(request);
        android.util.Log.d("RegisterActivity", "JSON Request: " + jsonRequest);
        
        authRepository.register(request, new ApiCallback<ApiResponse<RegisterResponse>>() {
            @Override
            public void onSuccess(ApiResponse<RegisterResponse> response) {
                setLoading(false);
                
                // Log response for debugging
                android.util.Log.d("RegisterActivity", "onSuccess called - ALWAYS navigate to VerifyEmailActivity");
                android.util.Log.d("RegisterActivity", "Response: " + (response != null ? response.getMessage() : "null"));
                
                // Backend auto-sends OTP email after successful registration
                // Always navigate to VerifyEmailActivity when onSuccess is called
                Toast.makeText(RegisterActivity.this, 
                    "Đăng ký thành công! Vui lòng kiểm tra email và nhập mã OTP.", 
                    Toast.LENGTH_LONG).show();
                
                // Navigate to VerifyEmailActivity for OTP verification
                android.util.Log.d("RegisterActivity", "Navigating to VerifyEmailActivity with email: " + email);
                
                try {
                    Intent intent = new Intent(RegisterActivity.this, VerifyEmailActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("otpType", "EmailVerification");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    android.util.Log.d("RegisterActivity", "Successfully navigated to VerifyEmailActivity");
                } catch (Exception e) {
                    android.util.Log.e("RegisterActivity", "Failed to navigate: " + e.getMessage());
                    Toast.makeText(RegisterActivity.this, 
                        "Lỗi chuyển màn hình. Email: " + email, 
                        Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                android.util.Log.e("RegisterActivity", "onError called: " + errorMessage);
                
                // Parse and show user-friendly error messages
                String userMessage = errorMessage;
                if (errorMessage.contains("Email already exists") || errorMessage.contains("email already")) {
                    userMessage = "Email đã tồn tại. Vui lòng sử dụng email khác hoặc đăng nhập.";
                } else if (errorMessage.contains("Invalid phone") || errorMessage.contains("phone")) {
                    userMessage = "Số điện thoại không hợp lệ. Vui lòng kiểm tra lại.";
                } else if (errorMessage.contains("Invalid email") || errorMessage.contains("email format")) {
                    userMessage = "Email không hợp lệ. Vui lòng kiểm tra lại.";
                } else if (errorMessage.contains("Password")) {
                    userMessage = "Mật khẩu không đủ mạnh. Yêu cầu ít nhất 6 ký tự, có chữ hoa, chữ thường và số.";
                }
                
                Toast.makeText(RegisterActivity.this, userMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void setLoading(boolean isLoading) {
        btnRegister.setEnabled(!isLoading);
        progressBar.setVisibility(View.GONE);
        
        if (isLoading) {
            btnRegister.setText("Đang đăng ký...");
            btnRegister.setAlpha(0.6f);
        } else {
            btnRegister.setText("ĐĂNG KÝ");
            btnRegister.setAlpha(1.0f);
        }
    }
    
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
