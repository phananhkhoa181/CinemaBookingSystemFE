package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.Cinema;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * CinemaRepository - Handles cinema-related API calls
 */
public class CinemaRepository {
    
    private static final String TAG = "CinemaRepository";
    private static CinemaRepository instance;
    private final ApiService apiService;
    
    private CinemaRepository(Context context) {
        this.apiService = ApiClient.getInstance(context).getApiService();
    }
    
    public static synchronized CinemaRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CinemaRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Get all cinemas
     */
    public void getAllCinemas(ApiCallback<ApiResponse<List<Cinema>>> callback) {
        Log.d(TAG, "Fetching all cinemas...");
        
        apiService.getCinemas().enqueue(new Callback<ApiResponse<List<Cinema>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Cinema>>> call, Response<ApiResponse<List<Cinema>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Cinemas loaded successfully: " + response.body().getData().size() + " cinemas");
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ Failed to load cinemas. Code: " + response.code());
                    callback.onError("Failed to load cinemas");
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<Cinema>>> call, Throwable t) {
                Log.e(TAG, "❌ Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
