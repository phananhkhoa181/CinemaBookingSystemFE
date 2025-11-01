package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;

import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;

/**
 * UserRepository - Xử lý user profile logic
 * TODO: Implement getCurrentUser, updateProfile, uploadAvatar
 * 
 * ASSIGNED TO: Developer 5
 * PRIORITY: LOW
 * DEADLINE: Week 8
 * 
 * Refer to: docs-FE/03-Screens/06-Profile.md
 */
public class UserRepository {
    
    private static UserRepository instance;
    private final ApiService apiService;
    
    private UserRepository(Context context) {
        apiService = ApiClient.getInstance(context).getApiService();
    }
    
    public static synchronized UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context);
        }
        return instance;
    }
    
    // TODO: Implement methods
    // - getCurrentUser(callback)
    // - updateProfile(request, callback)
    // - uploadAvatar(base64Image, callback)
}
