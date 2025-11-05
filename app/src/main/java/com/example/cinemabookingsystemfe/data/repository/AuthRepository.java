package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;

import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;
import com.example.cinemabookingsystemfe.data.models.request.ForgotPasswordRequest;
import com.example.cinemabookingsystemfe.data.models.request.LoginRequest;
import com.example.cinemabookingsystemfe.data.models.request.RefreshTokenRequest;
import com.example.cinemabookingsystemfe.data.models.request.RegisterRequest;
import com.example.cinemabookingsystemfe.data.models.request.ResendOtpRequest;
import com.example.cinemabookingsystemfe.data.models.request.ResetPasswordRequest;
import com.example.cinemabookingsystemfe.data.models.request.SendOtpRequest;
import com.example.cinemabookingsystemfe.data.models.request.VerifyOtpRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.LoginResponse;
import com.example.cinemabookingsystemfe.data.models.response.RefreshTokenResponse;
import com.example.cinemabookingsystemfe.data.models.response.RegisterResponse;
import com.example.cinemabookingsystemfe.data.models.response.ResetPasswordResponse;
import com.example.cinemabookingsystemfe.data.models.response.SendOtpResponse;
import com.example.cinemabookingsystemfe.data.models.response.VerifyOtpResponse;
import com.example.cinemabookingsystemfe.utils.Constants;
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
        // Call real API via Retrofit - Backend returns LoginResponse directly (not wrapped)
        Call<LoginResponse> call = apiService.login(request);
        
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                android.util.Log.d("AuthRepository", "Login response code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    // Backend returns LoginResponse directly (not wrapped in ApiResponse)
                    LoginResponse loginResponse = response.body();
                    
                    android.util.Log.d("AuthRepository", "Login successful - HTTP " + response.code());
                    android.util.Log.d("AuthRepository", "Token: " + (loginResponse.getToken() != null ? "Present" : "NULL"));
                    android.util.Log.d("AuthRepository", "User: " + (loginResponse.getUser() != null ? loginResponse.getUser().getEmail() : "NULL"));
                    
                    if (loginResponse != null && loginResponse.getToken() != null) {
                        // Save token and user info
                        prefsManager.saveToken(loginResponse.getToken());
                        prefsManager.saveRefreshToken(loginResponse.getRefreshToken());
                        prefsManager.saveUserId(loginResponse.getUserId());
                        prefsManager.saveUserName(loginResponse.getFullname());
                        prefsManager.saveUserEmail(loginResponse.getEmail());
                        prefsManager.setLoggedIn(true); // Set logged in status
                        
                        android.util.Log.d("AuthRepository", "Login successful, tokens saved for: " + loginResponse.getEmail());
                        
                        // Wrap in ApiResponse for compatibility
                        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(true, "Login successful", loginResponse);
                        callback.onSuccess(apiResponse);
                    } else {
                        android.util.Log.e("AuthRepository", "Login response missing token or data");
                        callback.onError("Đăng nhập thất bại: Không nhận được token");
                    }
                } else {
                    // Handle error responses
                    android.util.Log.e("AuthRepository", "Login failed with HTTP code: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        android.util.Log.e("AuthRepository", "Login error body: " + errorBody);
                        
                        // Parse error message from backend
                        if (response.code() == 401) {
                            if (errorBody.contains("not verified") || errorBody.contains("verify")) {
                                callback.onError("Tài khoản chưa được xác thực. Vui lòng kiểm tra email và verify OTP.");
                            } else {
                                callback.onError("Email hoặc mật khẩu không đúng");
                            }
                        } else if (response.code() == 404) {
                            callback.onError("Tài khoản không tồn tại");
                        } else {
                            callback.onError("Đăng nhập thất bại: " + errorBody);
                        }
                    } catch (Exception e) {
                        callback.onError("Đăng nhập thất bại: " + response.message());
                    }
                }
            }
            
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                android.util.Log.e("AuthRepository", "Login network failure", t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Register with mock data (for testing without backend)
     * API: POST /api/auth/register
     */
    public void register(RegisterRequest request, ApiCallback<ApiResponse<RegisterResponse>> callback) {
        // Call real API via Retrofit
        Call<ApiResponse<RegisterResponse>> call = apiService.register(request);
        
        call.enqueue(new Callback<ApiResponse<RegisterResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<RegisterResponse>> call, Response<ApiResponse<RegisterResponse>> response) {
                android.util.Log.d("AuthRepository", "Register response code: " + response.code());
                android.util.Log.d("AuthRepository", "Register response successful: " + response.isSuccessful());
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<RegisterResponse> apiResponse = response.body();
                    android.util.Log.d("AuthRepository", "HTTP Status Code: " + response.code());
                    android.util.Log.d("AuthRepository", "API Response success field: " + apiResponse.isSuccess());
                    android.util.Log.d("AuthRepository", "API Response message: " + apiResponse.getMessage());
                    android.util.Log.d("AuthRepository", "API Response data: " + (apiResponse.getData() != null ? "NOT NULL" : "NULL"));
                    
                    // Registration successful if HTTP status is 2xx (200-299)
                    // Backend returns 201 Created for successful registration
                    // Don't rely on success field, it may be missing or false
                    if (response.code() >= 200 && response.code() < 300) {
                        android.util.Log.d("AuthRepository", "Registration successful based on HTTP status code: " + response.code());
                        
                        // Save user info if data is available
                        if (apiResponse.getData() != null) {
                            RegisterResponse data = apiResponse.getData();
                            prefsManager.saveUserId(data.getUserId());
                            prefsManager.saveUserName(data.getFullname());
                            prefsManager.saveUserEmail(data.getEmail());
                            android.util.Log.d("AuthRepository", "User info saved: " + data.getEmail());
                        } else {
                            android.util.Log.d("AuthRepository", "Data is null but registration successful (backend auto-sends OTP)");
                        }
                        
                        android.util.Log.d("AuthRepository", "Calling callback.onSuccess()");
                        callback.onSuccess(apiResponse);
                    } else {
                        android.util.Log.e("AuthRepository", "Registration failed with status: " + response.code());
                        callback.onError(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Registration failed");
                    }
                } else {
                    android.util.Log.e("AuthRepository", "Response not successful: " + response.code() + " - " + response.message());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        android.util.Log.e("AuthRepository", "Error body: " + errorBody);
                        
                        // Parse backend error messages
                        String userMessage = "Đăng ký thất bại";
                        if (errorBody.contains("Email already exists") || errorBody.contains("email")) {
                            userMessage = "Email already exists";
                        } else if (errorBody.contains("Invalid phone") || errorBody.contains("phone")) {
                            userMessage = "Invalid phone number format";
                        } else if (errorBody.contains("password")) {
                            userMessage = "Password does not meet requirements";
                        } else if (response.code() == 400) {
                            userMessage = errorBody;
                        }
                        
                        callback.onError(userMessage);
                    } catch (Exception e) {
                        callback.onError("Đăng ký thất bại: " + response.message());
                    }
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<RegisterResponse>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
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
    
    /**
     * Send OTP to email
     */
    public void sendOtp(SendOtpRequest request, ApiCallback<ApiResponse<SendOtpResponse>> callback) {
        if (Constants.USE_MOCK_API) {
            // Mock implementation if needed
            return;
        }
        
        apiService.sendOtp(request).enqueue(new Callback<ApiResponse<SendOtpResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<SendOtpResponse>> call, Response<ApiResponse<SendOtpResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Gửi OTP thất bại");
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<SendOtpResponse>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Verify OTP code
     */
    public void verifyOtp(VerifyOtpRequest request, ApiCallback<ApiResponse<VerifyOtpResponse>> callback) {
        if (Constants.USE_MOCK_API) {
            // Mock implementation if needed
            return;
        }
        
        apiService.verifyOtp(request).enqueue(new Callback<ApiResponse<VerifyOtpResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<VerifyOtpResponse>> call, Response<ApiResponse<VerifyOtpResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Xác thực OTP thất bại");
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<VerifyOtpResponse>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Resend OTP code
     */
    public void resendOtp(ResendOtpRequest request, ApiCallback<ApiResponse<SendOtpResponse>> callback) {
        if (Constants.USE_MOCK_API) {
            // Mock implementation if needed
            return;
        }
        
        apiService.resendOtp(request).enqueue(new Callback<ApiResponse<SendOtpResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<SendOtpResponse>> call, Response<ApiResponse<SendOtpResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Gửi lại OTP thất bại");
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<SendOtpResponse>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Refresh JWT token using refresh token
     * Call this when token expires (401) or proactively before expiry
     */
    public void refreshToken(ApiCallback<RefreshTokenResponse> callback) {
        String refreshToken = prefsManager.getRefreshToken();
        
        if (refreshToken == null || refreshToken.isEmpty()) {
            callback.onError("Refresh token not found. Please login again.");
            return;
        }
        
        android.util.Log.d("AuthRepository", "Refreshing token with: " + refreshToken);
        
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        apiService.refreshToken(request).enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                android.util.Log.d("AuthRepository", "Refresh token response code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    RefreshTokenResponse data = response.body();
                    
                    // Save new tokens
                    prefsManager.saveToken(data.getToken());
                    prefsManager.saveRefreshToken(data.getRefreshToken());
                    
                    android.util.Log.d("AuthRepository", "Token refreshed successfully");
                    callback.onSuccess(data);
                } else {
                    android.util.Log.e("AuthRepository", "Refresh token failed: " + response.code());
                    callback.onError("Token refresh failed. Please login again.");
                }
            }
            
            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                android.util.Log.e("AuthRepository", "Refresh token error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    /**
     * Forgot password - Send OTP to email for password reset
     * Backend automatically sends OTP email with 10-minute expiry
     */
    public void forgotPassword(String email, ApiCallback<SendOtpResponse> callback) {
        android.util.Log.d("AuthRepository", "Sending forgot password request for: " + email);
        
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        apiService.forgotPassword(request).enqueue(new Callback<ApiResponse<SendOtpResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<SendOtpResponse>> call, Response<ApiResponse<SendOtpResponse>> response) {
                android.util.Log.d("AuthRepository", "Forgot password response code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<SendOtpResponse> apiResponse = response.body();
                    android.util.Log.d("AuthRepository", "Response body - success: " + apiResponse.isSuccess() + 
                        ", message: " + apiResponse.getMessage() + 
                        ", data: " + (apiResponse.getData() != null ? "not null" : "null"));
                    
                    // Check if response is successful (2xx status code)
                    if (apiResponse.isSuccess() || response.code() >= 200 && response.code() < 300) {
                        // If data is null, create a simple response object for navigation
                        SendOtpResponse data = apiResponse.getData();
                        if (data == null) {
                            data = new SendOtpResponse();
                            data.setEmail(email);
                            data.setMessage(apiResponse.getMessage() != null ? apiResponse.getMessage() : "OTP sent successfully");
                            android.util.Log.d("AuthRepository", "Data is null, created default response");
                        }
                        
                        android.util.Log.d("AuthRepository", "OTP sent successfully to email: " + data.getEmail());
                        callback.onSuccess(data);
                    } else {
                        String errorMsg = apiResponse.getMessage() != null 
                            ? apiResponse.getMessage() 
                            : "Failed to send OTP";
                        android.util.Log.e("AuthRepository", "Forgot password failed: " + errorMsg);
                        callback.onError(errorMsg);
                    }
                } else {
                    android.util.Log.e("AuthRepository", "Forgot password failed with code: " + response.code());
                    
                    // Parse error body for detailed message from backend
                    String errorMsg = "Failed to send OTP";
                    String fullErrorBody = "";
                    
                    try {
                        if (response.errorBody() != null) {
                            fullErrorBody = response.errorBody().string();
                            android.util.Log.e("AuthRepository", "=== ERROR RESPONSE BODY ===");
                            android.util.Log.e("AuthRepository", fullErrorBody);
                            android.util.Log.e("AuthRepository", "===========================");
                            
                            // Try to parse as JSON to extract message
                            try {
                                com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
                                com.google.gson.JsonObject jsonError = parser.parse(fullErrorBody).getAsJsonObject();
                                
                                // Try different message fields
                                if (jsonError.has("message")) {
                                    errorMsg = jsonError.get("message").getAsString();
                                    android.util.Log.d("AuthRepository", "Extracted message: " + errorMsg);
                                } else if (jsonError.has("error")) {
                                    errorMsg = jsonError.get("error").getAsString();
                                    android.util.Log.d("AuthRepository", "Extracted error: " + errorMsg);
                                } else if (jsonError.has("data") && jsonError.get("data").isJsonObject()) {
                                    com.google.gson.JsonObject dataObj = jsonError.getAsJsonObject("data");
                                    if (dataObj.has("message")) {
                                        errorMsg = dataObj.get("message").getAsString();
                                        android.util.Log.d("AuthRepository", "Extracted data.message: " + errorMsg);
                                    }
                                }
                            } catch (Exception jsonEx) {
                                android.util.Log.e("AuthRepository", "JSON parse failed: " + jsonEx.getMessage());
                                // If not JSON or parse fails, use plain text if short enough
                                if (fullErrorBody.length() < 300 && !fullErrorBody.contains("<!DOCTYPE")) {
                                    errorMsg = fullErrorBody;
                                }
                            }
                        }
                    } catch (Exception e) {
                        android.util.Log.e("AuthRepository", "Error parsing error body: " + e.getMessage(), e);
                    }
                    
                    // Show full error body in debug message
                    android.util.Log.e("AuthRepository", "Final error message to user: " + errorMsg);
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<SendOtpResponse>> call, Throwable t) {
                android.util.Log.e("AuthRepository", "Forgot password error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    /**
     * Reset password with OTP code
     * This endpoint verifies OTP internally - do NOT call verify-otp separately!
     * Tracks IP address and User-Agent for security audit
     */
    public void resetPassword(String email, String otpCode, String newPassword, 
                             String confirmPassword, ApiCallback<ResetPasswordResponse> callback) {
        android.util.Log.d("AuthRepository", "Resetting password for: " + email);
        
        ResetPasswordRequest request = new ResetPasswordRequest(email, otpCode, newPassword, confirmPassword);
        apiService.resetPassword(request).enqueue(new Callback<ApiResponse<ResetPasswordResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResetPasswordResponse>> call, Response<ApiResponse<ResetPasswordResponse>> response) {
                android.util.Log.d("AuthRepository", "Reset password response code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ResetPasswordResponse> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        ResetPasswordResponse data = apiResponse.getData();
                        android.util.Log.d("AuthRepository", "Password reset successful for: " + data.getEmail());
                        callback.onSuccess(data);
                    } else {
                        String errorMsg = apiResponse.getMessage() != null 
                            ? apiResponse.getMessage() 
                            : "Failed to reset password";
                        android.util.Log.e("AuthRepository", "Reset password failed: " + errorMsg);
                        callback.onError(errorMsg);
                    }
                } else {
                    android.util.Log.e("AuthRepository", "Reset password failed: " + response.code());
                    
                    // Parse error message from response body
                    String errorMsg = "Failed to reset password. Please check your OTP code.";
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            android.util.Log.e("AuthRepository", "Error body: " + errorBody);
                            // Try to extract message from error body if needed
                        }
                    } catch (Exception e) {
                        android.util.Log.e("AuthRepository", "Error parsing error body: " + e.getMessage());
                    }
                    
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<ResetPasswordResponse>> call, Throwable t) {
                android.util.Log.e("AuthRepository", "Reset password error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
