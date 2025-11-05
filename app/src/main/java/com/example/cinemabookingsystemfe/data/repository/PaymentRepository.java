package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;
import com.example.cinemabookingsystemfe.data.models.request.CreatePaymentRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.CreatePaymentResponse;
import com.example.cinemabookingsystemfe.data.models.response.PaymentDetailResponse;
import com.example.cinemabookingsystemfe.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * PaymentRepository - Handle VNPay payment API calls
 */
public class PaymentRepository {
    
    private static final String TAG = "PaymentRepository";
    private static PaymentRepository instance;
    private final ApiService apiService;
    private final TokenManager tokenManager;
    
    private PaymentRepository(Context context) {
        this.apiService = ApiClient.getInstance(context).getApiService();
        this.tokenManager = TokenManager.getInstance(context);
    }
    
    public static synchronized PaymentRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PaymentRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Create VNPay payment
     * POST /api/payments/vnpay/create
     */
    public void createVNPayPayment(int bookingId, String returnUrl, 
                                    ApiCallback<ApiResponse<CreatePaymentResponse>> callback) {
        String token = tokenManager.getToken();
        if (token == null) {
            callback.onError("Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.");
            return;
        }
        
        CreatePaymentRequest request = new CreatePaymentRequest(bookingId, returnUrl);
        
        Call<ApiResponse<CreatePaymentResponse>> call = apiService.createVNPayPayment(request);
        
        call.enqueue(new Callback<ApiResponse<CreatePaymentResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CreatePaymentResponse>> call, 
                                 Response<ApiResponse<CreatePaymentResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Create VNPay payment successful");
                    callback.onSuccess(response.body());
                } else {
                    String error = "Không thể tạo thanh toán";
                    if (response.code() == 403) {
                        error = "Không có quyền truy cập booking này";
                    } else if (response.code() == 400) {
                        error = "Trạng thái booking không hợp lệ";
                    } else if (response.code() == 409) {
                        error = "Thanh toán đã tồn tại cho booking này";
                    }
                    Log.e(TAG, "Create payment failed: " + response.code());
                    callback.onError(error);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<CreatePaymentResponse>> call, Throwable t) {
                Log.e(TAG, "Create payment network error: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Get payment details
     * GET /api/payments/{id}
     */
    public void getPaymentDetails(int paymentId, 
                                   ApiCallback<ApiResponse<PaymentDetailResponse>> callback) {
        String token = tokenManager.getToken();
        if (token == null) {
            callback.onError("Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.");
            return;
        }
        
        Call<ApiResponse<PaymentDetailResponse>> call = apiService.getPaymentDetails(paymentId);
        
        call.enqueue(new Callback<ApiResponse<PaymentDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PaymentDetailResponse>> call, 
                                 Response<ApiResponse<PaymentDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Get payment details successful");
                    callback.onSuccess(response.body());
                } else {
                    String error = "Không thể lấy thông tin thanh toán";
                    if (response.code() == 403) {
                        error = "Không có quyền truy cập thanh toán này";
                    } else if (response.code() == 404) {
                        error = "Không tìm thấy thanh toán";
                    }
                    Log.e(TAG, "Get payment details failed: " + response.code());
                    callback.onError(error);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<PaymentDetailResponse>> call, Throwable t) {
                Log.e(TAG, "Get payment details network error: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
