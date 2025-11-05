package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;
import com.example.cinemabookingsystemfe.data.models.request.ApplyVoucherRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.ApplyVoucherResponse;
import com.example.cinemabookingsystemfe.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * VoucherRepository - Handle voucher-related API calls
 */
public class VoucherRepository {
    
    private static final String TAG = "VoucherRepository";
    private static VoucherRepository instance;
    private final ApiService apiService;
    private final TokenManager tokenManager;
    
    private VoucherRepository(Context context) {
        this.apiService = ApiClient.getInstance(context).getApiService();
        this.tokenManager = TokenManager.getInstance(context);
    }
    
    public static synchronized VoucherRepository getInstance(Context context) {
        if (instance == null) {
            instance = new VoucherRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Apply voucher to booking
     * POST /api/bookings/{id}/apply-voucher
     */
    public void applyVoucher(int bookingId, String voucherCode, ApiCallback<ApiResponse<ApplyVoucherResponse>> callback) {
        String token = tokenManager.getToken();
        if (token == null) {
            callback.onError("Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.");
            return;
        }
        
        ApplyVoucherRequest request = new ApplyVoucherRequest(voucherCode);
        
        Call<ApiResponse<ApplyVoucherResponse>> call = apiService.applyVoucher(bookingId, request);
        
        call.enqueue(new Callback<ApiResponse<ApplyVoucherResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ApplyVoucherResponse>> call, Response<ApiResponse<ApplyVoucherResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Apply voucher successful: " + response.body().getMessage());
                    callback.onSuccess(response.body());
                } else {
                    String error = "Mã khuyến mãi không hợp lệ";
                    if (response.code() == 400) {
                        error = "Mã khuyến mãi đã hết hạn hoặc không đủ điều kiện áp dụng";
                    } else if (response.code() == 404) {
                        error = "Mã khuyến mãi không tồn tại";
                    } else if (response.code() == 409) {
                        error = "Booking đã áp dụng voucher khác";
                    }
                    Log.e(TAG, "Apply voucher failed: " + response.code());
                    callback.onError(error);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<ApplyVoucherResponse>> call, Throwable t) {
                Log.e(TAG, "Apply voucher network error: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Cancel booking
     * POST /api/bookings/{id}/cancel
     */
    public void cancelBooking(int bookingId, ApiCallback<ApiResponse<Void>> callback) {
        String token = tokenManager.getToken();
        if (token == null) {
            callback.onError("Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.");
            return;
        }
        
        Call<ApiResponse<Void>> call = apiService.cancelBooking(bookingId);
        
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Cancel booking successful");
                    callback.onSuccess(response.body());
                } else {
                    String error = "Không thể hủy đơn đặt vé";
                    if (response.code() == 404) {
                        error = "Không tìm thấy đơn đặt vé";
                    } else if (response.code() == 409) {
                        error = "Không thể hủy đơn đã thanh toán";
                    }
                    Log.e(TAG, "Cancel booking failed: " + response.code());
                    callback.onError(error);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Log.e(TAG, "Cancel booking network error: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
