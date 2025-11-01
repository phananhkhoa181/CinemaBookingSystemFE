package com.example.cinemabookingsystemfe.data.api.interceptors;

import com.example.cinemabookingsystemfe.utils.SharedPrefsManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * AuthInterceptor - Thêm JWT token vào header của mọi request
 * 
 * TODO: Inject SharedPrefsManager qua constructor khi có Context
 */
public class AuthInterceptor implements Interceptor {
    
    private SharedPrefsManager prefsManager;
    
    public AuthInterceptor(SharedPrefsManager prefsManager) {
        this.prefsManager = prefsManager;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        
        // Get token from SharedPreferences
        String token = prefsManager.getToken();
        
        // If token exists, add Authorization header
        if (token != null && !token.isEmpty()) {
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }
        
        // No token, proceed with original request
        return chain.proceed(originalRequest);
    }
}
