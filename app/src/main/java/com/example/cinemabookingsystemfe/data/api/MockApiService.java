package com.example.cinemabookingsystemfe.data.api;

import android.os.Handler;
import android.os.Looper;

import com.example.cinemabookingsystemfe.data.models.request.LoginRequest;
import com.example.cinemabookingsystemfe.data.models.request.RegisterRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.LoginResponse;
import com.example.cinemabookingsystemfe.data.models.response.RegisterResponse;
import com.example.cinemabookingsystemfe.data.models.response.User;

/**
 * Mock API Service for testing without backend
 * TODO: Remove this when real backend is ready
 */
public class MockApiService {
    
    private static final int MOCK_DELAY_MS = 1000; // Simulate network delay
    
    /**
     * Mock login - accepts any username/password
     */
    public static void login(LoginRequest request, ApiCallback<ApiResponse<LoginResponse>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // NOTE: Mock disabled - using real API now
            // Mock success response with nested user structure (matching backend format)
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken("mock_jwt_token_" + System.currentTimeMillis());
            loginResponse.setRefreshToken("mock_refresh_token_" + System.currentTimeMillis());
            loginResponse.setExpiresAt("2025-12-31T23:59:59");
            
            // Create nested user data (matching backend format)
            LoginResponse.UserData userData = new LoginResponse.UserData();
            userData.setUserId(1);
            userData.setFullName("Mock User");
            userData.setEmail(request.getEmail());
            userData.setPhoneNumber("0123456789");
            userData.setRoleId(1);
            userData.setRoleName("Customer");
            loginResponse.setUser(userData);
            
            ApiResponse<LoginResponse> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Login successful");
            response.setData(loginResponse);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }
    
    /**
     * Mock register
     */
    public static void register(RegisterRequest request, ApiCallback<ApiResponse<RegisterResponse>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // NOTE: Mock disabled - using real API now
            // Mock success response with updated request fields
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setUserId(2);
            registerResponse.setFullname(request.getFullname());
            registerResponse.setEmail(request.getEmail());
            registerResponse.setPhone(request.getPhone());
            registerResponse.setRoleName("Customer");
            
            ApiResponse<RegisterResponse> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Registration successful");
            response.setData(registerResponse);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }
    
    /**
     * Mock forgot password - always success
     */
    public static void forgotPassword(String email, ApiCallback<ApiResponse<Void>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Đã gửi email hướng dẫn đặt lại mật khẩu đến " + email);
            response.setData(null);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }
    
    /**
     * Mock logout - always success
     */
    public static void logout(ApiCallback<ApiResponse<Void>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Đăng xuất thành công");
            response.setData(null);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }
}
