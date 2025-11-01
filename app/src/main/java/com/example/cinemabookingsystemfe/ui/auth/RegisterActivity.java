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
        
        // Call API with mock data - using email as username
        RegisterRequest request = new RegisterRequest(email, password, email, fullName);
        request.setPhoneNumber(phone);
        
        authRepository.register(request, new ApiCallback<ApiResponse<RegisterResponse>>() {
            @Override
            public void onSuccess(ApiResponse<RegisterResponse> response) {
                setLoading(false);
                
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                
                // Navigate to MainActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            
            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                Toast.makeText(RegisterActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
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
