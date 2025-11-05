package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;
import com.example.cinemabookingsystemfe.data.models.request.AddCombosRequest;
import com.example.cinemabookingsystemfe.data.models.response.AddCombosResponse;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.Combo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ComboRepository - Handles combo-related APIs
 */
public class ComboRepository {
    
    private static ComboRepository instance;
    private final ApiService apiService;
    private final Context context;
    
    private ComboRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = ApiClient.getInstance(context).getApiService();
    }
    
    public static synchronized ComboRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ComboRepository(context);
        }
        return instance;
    }
    
    /**
     * Get all available combos
     * API: GET /api/combos
     * Auth: Not required
     */
    public void getCombos(ApiCallback<ApiResponse<List<Combo>>> callback) {
        Call<ApiResponse<List<Combo>>> call = apiService.getCombos();
        
        call.enqueue(new Callback<ApiResponse<List<Combo>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Combo>>> call, 
                                 Response<ApiResponse<List<Combo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Error " + response.code();
                    android.util.Log.d("ComboRepository", "Get combos error: " + errorMsg);
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<Combo>>> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                android.util.Log.e("ComboRepository", "getCombos failure", t);
                callback.onError(errorMsg);
            }
        });
    }
    
    /**
     * Add combos to booking
     * API: POST /api/bookings/{id}/add-combos
     * Auth: Required
     */
    public void addCombosToBooking(int bookingId, AddCombosRequest request, 
                                  ApiCallback<ApiResponse<AddCombosResponse>> callback) {
        Call<ApiResponse<AddCombosResponse>> call = apiService.addCombosToBooking(bookingId, request);
        
        call.enqueue(new Callback<ApiResponse<AddCombosResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AddCombosResponse>> call, 
                                 Response<ApiResponse<AddCombosResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = String.valueOf(response.code());
                    
                    // Log detailed error
                    String detailedError = "Add combos error: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            detailedError += " - " + response.errorBody().string();
                        } catch (Exception e) {
                            android.util.Log.e("ComboRepository", "Error reading error body", e);
                        }
                    }
                    android.util.Log.d("ComboRepository", detailedError);
                    
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<AddCombosResponse>> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                android.util.Log.e("ComboRepository", "addCombosToBooking failure", t);
                callback.onError(errorMsg);
            }
        });
    }
}
