package com.example.cinemabookingsystemfe.data.api;

import com.example.cinemabookingsystemfe.data.models.request.*;
import com.example.cinemabookingsystemfe.data.models.response.*;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * ApiService - Authentication APIs Only
 * 
 * Currently implementing:
 * - Login
 * - Register
 * - Logout
 * - Forgot Password
 * 
 * Using MockApiService for now (no backend needed)
 * TODO: Implement other modules (Movies, Bookings, etc.) later
 */
public interface ApiService {
    
    // ====================================
    // AUTHENTICATION APIs
    // ====================================
    
    @POST("api/auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);
    
    @POST("api/auth/register")
    Call<ApiResponse<RegisterResponse>> register(@Body RegisterRequest request);
    
    @POST("api/auth/logout")
    Call<ApiResponse<Void>> logout();
    
    @POST("api/auth/forgot-password")
    Call<ApiResponse<Void>> forgotPassword(@Body ForgotPasswordRequest request);
    
    // ====================================
    // TODO: Other modules (Movies, Bookings, Payments, etc.)
    // Will be implemented later when needed
    // ====================================
}
