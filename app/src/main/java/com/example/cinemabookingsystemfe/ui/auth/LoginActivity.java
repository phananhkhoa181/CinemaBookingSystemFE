package com.example.cinemabookingsystemfe.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.request.LoginRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.LoginResponse;
import com.example.cinemabookingsystemfe.data.repository.AuthRepository;
import com.example.cinemabookingsystemfe.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * LoginActivity - Professional login screen with mock data
 */
public class LoginActivity extends AppCompatActivity {
    
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    
    // Web Client ID from backend (for requestIdToken)
    private static final String WEB_CLIENT_ID = "1005211565279-3ffgkjhh0vd8ans8hv8gt9152gf3pim6.apps.googleusercontent.com";
    
    // TODO: Replace with your Android Client ID from Google Console
    // Create Android OAuth Client ID with package name: com.example.cinemabookingsystemfe
    // and SHA-1: F3:56:49:F8:1A:02:1A:E3:87:B4:39:56:C8:E7:1C:81:8C:CF:AE:81
    private static final String ANDROID_CLIENT_ID = "YOUR_ANDROID_CLIENT_ID.apps.googleusercontent.com";
    
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin, btnGoogleSignIn;
    private ProgressBar progressBar;
    private TextView tvForgotPassword, tvRegister;
    
    private AuthRepository authRepository;
    private GoogleSignInClient mGoogleSignInClient;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initViews();
        setupGoogleSignIn();
        setupListeners();
    }
    
    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        progressBar = findViewById(R.id.progressBar);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
        
        authRepository = AuthRepository.getInstance(this);
    }
    
    /**
     * Setup Google Sign-In configuration
     */
    private void setupGoogleSignIn() {
        // Try to get default_web_client_id from google-services.json
        String serverClientId;
        try {
            serverClientId = getString(R.string.default_web_client_id);
            Log.d(TAG, "Using default_web_client_id from google-services.json");
        } catch (Exception e) {
            // Fallback to hardcoded Web Client ID
            serverClientId = WEB_CLIENT_ID;
            Log.w(TAG, "google-services.json not found, using hardcoded Web Client ID");
        }
        
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(serverClientId)
                .requestEmail()
                .requestProfile()
                .build();
        
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        
        Log.d(TAG, "Google Sign-In configured successfully with prompt=select_account");
    }
    
    /**
     * Sign out from Google to force account picker
     */
    private void signOutGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Log.d(TAG, "Google sign out completed");
        });
    }
    
    private void setupListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
        
        btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle());
        
        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });
        
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
    
    /**
     * Start Google Sign-In flow
     */
    private void signInWithGoogle() {
        // Sign out first to force account picker (prompt=select_account behavior)
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Log.d(TAG, "Signed out from Google, showing account picker");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    
    /**
     * Handle Google Sign-In result
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            
            // Get ID Token
            String idToken = account.getIdToken();
            
            if (idToken != null) {
                Log.d(TAG, "ID Token received from Google");
                // Send token to backend
                loginWithGoogleToken(idToken);
            } else {
                Log.e(TAG, "ID Token is null");
                Toast.makeText(this, "Không thể lấy ID Token từ Google", Toast.LENGTH_SHORT).show();
            }
            
        } catch (ApiException e) {
            Log.e(TAG, "Google Sign-In failed: " + e.getStatusCode(), e);
            Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Send Google ID Token to backend API
     */
    private void loginWithGoogleToken(String idToken) {
        setLoading(true);
        
        authRepository.googleLogin(idToken, new ApiCallback<ApiResponse<LoginResponse>>() {
            @Override
            public void onSuccess(ApiResponse<LoginResponse> response) {
                setLoading(false);
                
                if (response.getData() != null && response.getData().getUser() != null) {
                    String userName = response.getData().getUser().getFullName();
                    Toast.makeText(LoginActivity.this, 
                        "Chào mừng, " + userName + "!", 
                        Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, 
                        "Đăng nhập Google thành công!", 
                        Toast.LENGTH_SHORT).show();
                }
                
                // Navigate to home
                navigateToHome();
            }
            
            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                Log.e(TAG, "Google Login API failed: " + errorMessage);
                Toast.makeText(LoginActivity.this, 
                    "Lỗi: " + errorMessage, 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Navigate to MainActivity
     */
    private void navigateToHome() {
        // Check if this activity was started from MainActivity for auth check
        if (getCallingActivity() != null) {
            // Return success result to MainActivity
            setResult(RESULT_OK);
            finish();
        } else {
            // Navigate to MainActivity (first time login or from splash)
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
    
    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // Validate inputs
        if (!isValidEmail(email)) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }
        
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return;
        }
        
        // Show loading
        setLoading(true);
        
        // Call API with mock data
        LoginRequest request = new LoginRequest(email, password);
        authRepository.login(request, new ApiCallback<ApiResponse<LoginResponse>>() {
            @Override
            public void onSuccess(ApiResponse<LoginResponse> response) {
                setLoading(false);
                
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                
                // Navigate to home
                navigateToHome();
            }
            
            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setLoading(boolean isLoading) {
        btnLogin.setEnabled(!isLoading);
        btnGoogleSignIn.setEnabled(!isLoading);
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        
        if (isLoading) {
            btnLogin.setText("Đang đăng nhập...");
            btnLogin.setAlpha(0.6f);
            btnGoogleSignIn.setAlpha(0.6f);
        } else {
            btnLogin.setText("ĐĂNG NHẬP");
            btnLogin.setAlpha(1.0f);
            btnGoogleSignIn.setAlpha(1.0f);
        }
    }
    
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    @Override
    public void onBackPressed() {
        // If called from MainActivity for auth, return CANCELLED result
        if (getCallingActivity() != null) {
            setResult(RESULT_CANCELED);
        }
        super.onBackPressed();
    }
}
