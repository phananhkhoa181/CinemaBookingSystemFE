package com.example.cinemabookingsystemfe.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.MockApiService;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * ForgotPasswordActivity - Password recovery screen
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    
    private TextInputEditText etEmail;
    private MaterialButton btnSend;
    private ProgressBar progressBar;
    private ImageButton btnBack;
    private TextView tvBackToLogin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        
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
        
        // Call mock API
        MockApiService.forgotPassword(email, new ApiCallback<ApiResponse<Void>>() {
            @Override
            public void onSuccess(ApiResponse<Void> response) {
                setLoading(false);
                
                // Show success dialog
                new androidx.appcompat.app.AlertDialog.Builder(ForgotPasswordActivity.this)
                    .setTitle("Thành công!")
                    .setMessage(response.getMessage())
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setCancelable(false)
                    .show();
            }
            
            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                Toast.makeText(ForgotPasswordActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setLoading(boolean isLoading) {
        btnSend.setEnabled(!isLoading);
        progressBar.setVisibility(View.GONE);
        
        if (isLoading) {
            btnSend.setText("Đang gửi...");
            btnSend.setAlpha(0.6f);
        } else {
            btnSend.setText("GỬI EMAIL");
            btnSend.setAlpha(1.0f);
        }
    }
    
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
