package com.example.cinemabookingsystemfe.data.api.interceptors;

import android.util.Log;

import com.example.cinemabookingsystemfe.data.models.request.RefreshTokenRequest;
import com.example.cinemabookingsystemfe.data.models.response.RefreshTokenResponse;
import com.example.cinemabookingsystemfe.utils.SharedPrefsManager;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * AuthInterceptor - Thêm JWT token vào header và tự động refresh khi hết hạn
 */
public class AuthInterceptor implements Interceptor {
    
    private static final String TAG = "AuthInterceptor";
    private SharedPrefsManager prefsManager;
    private Gson gson = new Gson();
    
    public AuthInterceptor(SharedPrefsManager prefsManager) {
        this.prefsManager = prefsManager;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        
        // Get token from SharedPreferences
        String token = prefsManager.getToken();
        
        // If token exists, add Authorization header
        Request requestWithAuth;
        if (token != null && !token.isEmpty()) {
            requestWithAuth = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
        } else {
            requestWithAuth = originalRequest;
        }
        
        // Execute request
        Response response = chain.proceed(requestWithAuth);
        
        // If 401 Unauthorized, try to refresh token
        if (response.code() == 401 && token != null) {
            Log.d(TAG, "Token expired (401), attempting refresh...");
            
            synchronized (this) {
                // Close the previous response
                response.close();
                
                // Try to refresh token
                String newToken = refreshToken();
                
                if (newToken != null) {
                    Log.d(TAG, "Token refreshed successfully");
                    
                    // Retry original request with new token
                    Request newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + newToken)
                            .build();
                    return chain.proceed(newRequest);
                } else {
                    Log.e(TAG, "Failed to refresh token");
                    // Return original 401 response to trigger logout
                    return response;
                }
            }
        }
        
        return response;
    }
    
    /**
     * Refresh access token using refresh token
     * @return New access token or null if failed
     */
    private String refreshToken() {
        try {
            String refreshToken = prefsManager.getRefreshToken();
            if (refreshToken == null || refreshToken.isEmpty()) {
                Log.e(TAG, "No refresh token available");
                return null;
            }
            
            // Build refresh token request
            RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
            String jsonBody = gson.toJson(request);
            
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    jsonBody
            );
            
            Request refreshRequest = new Request.Builder()
                    .url(getBaseUrl() + "api/auth/refresh-token")
                    .post(body)
                    .build();
            
            // Execute refresh request synchronously
            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
            Response refreshResponse = client.newCall(refreshRequest).execute();
            
            if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
                String responseBody = refreshResponse.body().string();
                RefreshTokenResponse response = gson.fromJson(responseBody, RefreshTokenResponse.class);
                
                if (response != null && response.getToken() != null) {
                    // Save new tokens
                    prefsManager.saveToken(response.getToken());
                    if (response.getRefreshToken() != null) {
                        prefsManager.saveRefreshToken(response.getRefreshToken());
                    }
                    
                    return response.getToken();
                }
            }
            
            refreshResponse.close();
        } catch (Exception e) {
            Log.e(TAG, "Error refreshing token: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    private String getBaseUrl() {
        // Get base URL from somewhere (you might want to make this configurable)
        return "https://movie88aspnet-app.up.railway.app/";
    }
}
