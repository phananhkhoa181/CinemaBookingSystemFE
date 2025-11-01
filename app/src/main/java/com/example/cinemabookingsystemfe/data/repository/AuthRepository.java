package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;

import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;
import com.example.cinemabookingsystemfe.data.models.request.LoginRequest;
import com.example.cinemabookingsystemfe.data.models.request.RegisterRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.LoginResponse;
import com.example.cinemabookingsystemfe.data.models.response.RegisterResponse;
import com.example.cinemabookingsystemfe.utils.SharedPrefsManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AuthRepository - Xử lý authentication logic
 * TODO: Implement login, register, refresh token
 * 
 * ASSIGNED TO: Developer 1
 * PRIORITY: HIGH
 * DEADLINE: Week 2
 */
public class AuthRepository {
    
    private static AuthRepository instance;
    private final ApiService apiService;
    private final SharedPrefsManager prefsManager;
    
    private AuthRepository(Context context) {
        apiService = ApiClient.getInstance(context).getApiService();
        prefsManager = SharedPrefsManager.getInstance(context);
    }
    
    public static synchronized AuthRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AuthRepository(context);
        }
        return instance;
    }
    
    /**
     * Login with mock data (for testing without backend)
     * 
     * Steps:
     * 1. Call Mock API với LoginRequest
     * 2. Nhận LoginResponse (token, refreshToken, user)
     * 3. Save token vào SharedPreferences
     * 4. Save user info
     * 5. Set isLoggedIn = true
     * 6. Callback onSuccess hoặc onError
     */
    public void login(LoginRequest request, ApiCallback<ApiResponse<LoginResponse>> callback) {
        // Using MockApiService for now - will switch to real API later
        com.example.cinemabookingsystemfe.data.api.MockApiService.login(request, new ApiCallback<ApiResponse<LoginResponse>>() {
            @Override
            public void onSuccess(ApiResponse<LoginResponse> response) {
                if (response.isSuccess() && response.getData() != null) {
                    LoginResponse data = response.getData();
                    
                    // Save token and user info
                    prefsManager.saveToken(data.getToken());
                    prefsManager.saveRefreshToken(data.getRefreshToken());
                    prefsManager.saveUserId(data.getUser().getUserId());
                    prefsManager.saveUserName(data.getUser().getUsername());
                    prefsManager.saveUserEmail(data.getUser().getEmail());
                    
                    callback.onSuccess(response);
                } else {
                    callback.onError(response.getMessage());
                }
            }
            
            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
    
    /**
     * Register with mock data (for testing without backend)
     * API: POST /api/auth/register
     */
    public void register(RegisterRequest request, ApiCallback<ApiResponse<RegisterResponse>> callback) {
        // Using MockApiService for now - will switch to real API later
        com.example.cinemabookingsystemfe.data.api.MockApiService.register(request, new ApiCallback<ApiResponse<RegisterResponse>>() {
            @Override
            public void onSuccess(ApiResponse<RegisterResponse> response) {
                if (response.isSuccess() && response.getData() != null) {
                    RegisterResponse data = response.getData();
                    
                    // Save token and user info
                    prefsManager.saveToken(data.getToken());
                    prefsManager.saveRefreshToken(data.getRefreshToken());
                    prefsManager.saveUserId(data.getUser().getUserId());
                    prefsManager.saveUserName(data.getUser().getUsername());
                    prefsManager.saveUserEmail(data.getUser().getEmail());
                    
                    callback.onSuccess(response);
                } else {
                    callback.onError(response.getMessage());
                }
            }
            
            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
    
    /**
     * TODO: Implement logout
     * Clear SharedPreferences and navigate to Login
     */
    public void logout() {
        prefsManager.clearAll();
    }
}
