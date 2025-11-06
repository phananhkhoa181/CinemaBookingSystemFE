package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;
import com.example.cinemabookingsystemfe.data.models.request.UpdateCustomerRequest;
import com.example.cinemabookingsystemfe.data.models.request.UpdateUserRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.CustomerProfile;
import com.example.cinemabookingsystemfe.data.models.response.UpdateUserResponse;
import com.example.cinemabookingsystemfe.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * UserRepository - Xử lý user profile logic
 * Hỗ trợ: Get profile, Update profile (Users + Customers)
 */
public class UserRepository {
    
    private static final String TAG = "UserRepository";
    private static UserRepository instance;
    private final ApiService apiService;
    private final TokenManager tokenManager;
    
    private UserRepository(Context context) {
        apiService = ApiClient.getInstance(context).getApiService();
        tokenManager = TokenManager.getInstance(context);
    }
    
    public static synchronized UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context);
        }
        return instance;
    }
    
    /**
     * Lấy thông tin profile từ API
     */
    public void getCustomerProfile(ProfileCallback callback) {
        apiService.getCustomerProfile().enqueue(new Callback<ApiResponse<CustomerProfile>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<CustomerProfile>> call, 
                                 @NonNull Response<ApiResponse<CustomerProfile>> response) {
                Log.d(TAG, "getCustomerProfile - Response code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<CustomerProfile> apiResponse = response.body();
                    Log.d(TAG, "getCustomerProfile - isSuccess: " + apiResponse.isSuccess());
                    Log.d(TAG, "getCustomerProfile - message: " + apiResponse.getMessage());
                    Log.d(TAG, "getCustomerProfile - data is null: " + (apiResponse.getData() == null));
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        CustomerProfile profile = apiResponse.getData();
                        Log.d(TAG, "✅ Profile loaded - Name: " + profile.getFullName());
                        callback.onSuccess(profile);
                    } else {
                        Log.e(TAG, "❌ API returned success but data is null!");
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "❌ Response unsuccessful or body is null");
                    callback.onError("Không thể tải thông tin profile");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<CustomerProfile>> call, @NonNull Throwable t) {
                Log.e(TAG, "getCustomerProfile failed", t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Cập nhật profile - Gọi 2 API tuần tự:
     * 1. PUT /api/users/{id} - Cập nhật fullname, phone
     * 2. PUT /api/customers/profile - Cập nhật address, dateOfBirth, gender
     */
    public void updateProfile(int userId, String fullName, String phone, 
                            String address, String dateOfBirth, String gender,
                            UpdateProfileCallback callback) {
        
        // Bước 1: Cập nhật Users table
        UpdateUserRequest userRequest = new UpdateUserRequest(fullName, phone);
        
        apiService.updateUser(userId, userRequest).enqueue(new Callback<ApiResponse<UpdateUserResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<UpdateUserResponse>> call,
                                 @NonNull Response<ApiResponse<UpdateUserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UpdateUserResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        // Bước 2: Cập nhật Customers table
                        updateCustomerProfile(address, dateOfBirth, gender, callback);
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Không thể cập nhật thông tin user");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<UpdateUserResponse>> call, @NonNull Throwable t) {
                Log.e(TAG, "updateUser failed", t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Cập nhật thông tin customer (bước 2 của updateProfile)
     */
    private void updateCustomerProfile(String address, String dateOfBirth, String gender,
                                     UpdateProfileCallback callback) {
        
        UpdateCustomerRequest customerRequest = new UpdateCustomerRequest(address, dateOfBirth, gender);
        
        apiService.updateCustomerProfile(customerRequest).enqueue(new Callback<ApiResponse<CustomerProfile>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<CustomerProfile>> call,
                                 @NonNull Response<ApiResponse<CustomerProfile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<CustomerProfile> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Không thể cập nhật thông tin customer");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<CustomerProfile>> call, @NonNull Throwable t) {
                Log.e(TAG, "updateCustomerProfile failed", t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    // Callback interfaces
    public interface ProfileCallback {
        void onSuccess(CustomerProfile profile);
        void onError(String error);
    }
    
    public interface UpdateProfileCallback {
        void onSuccess(CustomerProfile updatedProfile);
        void onError(String error);
    }
}
