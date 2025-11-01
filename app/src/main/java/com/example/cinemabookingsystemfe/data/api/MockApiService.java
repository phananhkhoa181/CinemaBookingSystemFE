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
            // Mock success response
            User mockUser = new User(
                1,
                request.getUsername(),
                request.getUsername() + "@gmail.com",
                "Mock User"
            );
            mockUser.setPhoneNumber("0123456789");
            mockUser.setRole("Customer");
            
            LoginResponse loginResponse = new LoginResponse(
                "mock_jwt_token_" + System.currentTimeMillis(),
                "mock_refresh_token_" + System.currentTimeMillis(),
                mockUser
            );
            
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
            // Auto login after register
            User mockUser = new User(2, request.getUsername(), request.getEmail(), request.getFullName());
            mockUser.setPhoneNumber(request.getPhoneNumber());
            mockUser.setRole("Customer");
            
            RegisterResponse registerResponse = new RegisterResponse(
                "mock_jwt_token_" + System.currentTimeMillis(),
                "mock_refresh_token_" + System.currentTimeMillis(),
                mockUser
            );
            
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
