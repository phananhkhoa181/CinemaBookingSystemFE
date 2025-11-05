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
    Call<LoginResponse> login(@Body LoginRequest request);  // Backend returns LoginResponse directly (not wrapped)
    
    @POST("api/auth/register")
    Call<ApiResponse<RegisterResponse>> register(@Body RegisterRequest request);
    
    @POST("api/auth/logout")
    Call<ApiResponse<Void>> logout();
    
    @POST("api/auth/forgot-password")
    Call<ApiResponse<SendOtpResponse>> forgotPassword(@Body ForgotPasswordRequest request);
    
    // OTP Verification APIs
    @POST("api/auth/send-otp")
    Call<ApiResponse<SendOtpResponse>> sendOtp(@Body SendOtpRequest request);
    
    @POST("api/auth/verify-otp")
    Call<ApiResponse<VerifyOtpResponse>> verifyOtp(@Body VerifyOtpRequest request);
    
    @POST("api/auth/resend-otp")
    Call<ApiResponse<SendOtpResponse>> resendOtp(@Body ResendOtpRequest request);
    
    // Password Reset API
    @POST("api/auth/reset-password")
    Call<ApiResponse<ResetPasswordResponse>> resetPassword(@Body ResetPasswordRequest request);
    
    // Token Refresh API
    @POST("api/auth/refresh-token")
    Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest request);
    
    // ====================================
    // TODO: Other modules (Movies, Bookings, Payments, etc.)
    // Will be implemented later when needed
    // ====================================
}
