package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.BookingDetail;
import com.example.cinemabookingsystemfe.data.models.response.BookingListResponse;
import com.example.cinemabookingsystemfe.data.models.response.PaginatedBookingResponse;
import com.example.cinemabookingsystemfe.utils.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingRepository {
    private static final String TAG = "BookingRepository";
    private static BookingRepository instance;
    private final ApiService apiService;
    private final TokenManager tokenManager;
    
    private BookingRepository(Context context) {
        this.apiService = ApiClient.getInstance(context).getApiService();
        this.tokenManager = TokenManager.getInstance(context);
    }
    
    public static synchronized BookingRepository getInstance(Context context) {
        if (instance == null) {
            instance = new BookingRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Get my bookings with pagination and optional status filter
     * @param page Page number (1-based)
     * @param pageSize Number of items per page
     * @param status Filter by status: "Pending", "Confirmed", "Cancelled", "Completed", or null for all
     * @param callback Callback with list of bookings
     */
    public void getMyBookings(Integer page, Integer pageSize, String status, 
                              ApiCallback<ApiResponse<PaginatedBookingResponse>> callback) {
        // Check token using TokenManager
        String token = tokenManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Vui lòng đăng nhập lại");
            return;
        }
        
        Log.d(TAG, "Getting my bookings - page: " + page + ", pageSize: " + pageSize + ", status: " + status);
        
        // Use the apiService that already has AuthInterceptor configured
        Call<ApiResponse<PaginatedBookingResponse>> call = apiService.getMyBookings(page, pageSize, status);
        
        call.enqueue(new Callback<ApiResponse<PaginatedBookingResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PaginatedBookingResponse>> call, 
                                 Response<ApiResponse<PaginatedBookingResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PaginatedBookingResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Log.d(TAG, "Bookings loaded successfully");
                        callback.onSuccess(apiResponse);
                    } else {
                        Log.e(TAG, "API error: " + apiResponse.getMessage());
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    String errorMsg = "Lỗi tải danh sách đặt vé";
                    if (response.code() == 401) {
                        errorMsg = "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại";
                    } else if (response.code() == 403) {
                        errorMsg = "Bạn không có quyền truy cập";
                    }
                    Log.e(TAG, "HTTP error: " + response.code() + " - " + response.message());
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<PaginatedBookingResponse>> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Get booking detail by ID
     * @param bookingId Booking ID
     * @param callback Callback with booking detail
     */
    public void getBookingById(int bookingId, ApiCallback<ApiResponse<BookingDetail>> callback) {
        // Check token using TokenManager
        String token = tokenManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Vui lòng đăng nhập lại");
            return;
        }
        
        Log.d(TAG, "Getting booking detail - ID: " + bookingId);
        
        Call<ApiResponse<BookingDetail>> call = apiService.getBookingById(bookingId);
        
        call.enqueue(new Callback<ApiResponse<BookingDetail>>() {
            @Override
            public void onResponse(Call<ApiResponse<BookingDetail>> call, 
                                 Response<ApiResponse<BookingDetail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<BookingDetail> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Log.d(TAG, "Booking detail loaded successfully");
                        callback.onSuccess(apiResponse);
                    } else {
                        Log.e(TAG, "API error: " + apiResponse.getMessage());
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    String errorMsg = "Lỗi tải thông tin đặt vé";
                    if (response.code() == 401) {
                        errorMsg = "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại";
                    } else if (response.code() == 403) {
                        errorMsg = "Bạn không có quyền truy cập";
                    } else if (response.code() == 404) {
                        errorMsg = "Không tìm thấy thông tin đặt vé";
                    }
                    Log.e(TAG, "HTTP error: " + response.code() + " - " + response.message());
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<BookingDetail>> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
