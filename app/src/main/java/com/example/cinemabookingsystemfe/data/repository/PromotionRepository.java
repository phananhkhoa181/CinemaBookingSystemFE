package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.Promotion;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * PromotionRepository - Handle promotion-related API calls
 */
public class PromotionRepository {
    
    private static final String TAG = "PromotionRepository";
    private static PromotionRepository instance;
    private ApiService apiService;
    
    private PromotionRepository(Context context) {
        apiService = ApiClient.getInstance(context).getApiService();
    }
    
    public static synchronized PromotionRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PromotionRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Get active promotions
     */
    public void getActivePromotions(ApiCallback<ApiResponse<List<Promotion>>> callback) {
        Call<ApiResponse<List<Promotion>>> call = apiService.getActivePromotions();
        
        call.enqueue(new Callback<ApiResponse<List<Promotion>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Promotion>>> call, 
                                 Response<ApiResponse<List<Promotion>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Promotion>> apiResponse = response.body();
                    Log.d(TAG, "Response - success field: " + apiResponse.isSuccess() + 
                          ", message: " + apiResponse.getMessage() + 
                          ", status: " + apiResponse.getStatus() +
                          ", data is null: " + (apiResponse.getData() == null));
                    
                    // Prioritize checking data availability over success flag
                    // Some backends return success=false even when data is available
                    if (apiResponse.getData() != null || apiResponse.isSuccess()) {
                        Log.d(TAG, "getActivePromotions success: " + 
                              (apiResponse.getData() != null ? apiResponse.getData().size() : 0) + " promotions");
                        callback.onSuccess(apiResponse);
                    } else {
                        Log.e(TAG, "getActivePromotions failed: " + apiResponse.getMessage());
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "getActivePromotions response error: " + response.code());
                    callback.onError("Lỗi tải khuyến mãi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<Promotion>>> call, Throwable t) {
                Log.e(TAG, "getActivePromotions failure: " + t.getMessage(), t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
