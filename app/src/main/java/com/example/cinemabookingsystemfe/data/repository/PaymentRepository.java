package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;

import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;

/**
 * PaymentRepository - Xử lý payment logic (VNPay)
 * TODO: Implement createVNPayPayment, getPaymentById
 * 
 * ASSIGNED TO: Developer 4
 * PRIORITY: MEDIUM
 * DEADLINE: Week 7
 * 
 * Refer to: docs-FE/03-Screens/05-Payment.md
 */
public class PaymentRepository {
    
    private static PaymentRepository instance;
    private final ApiService apiService;
    
    private PaymentRepository(Context context) {
        apiService = ApiClient.getInstance(context).getApiService();
    }
    
    public static synchronized PaymentRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PaymentRepository(context);
        }
        return instance;
    }
    
    // TODO: Implement methods
    // - createVNPayPayment(bookingId, returnUrl, callback)
    // - getPaymentById(paymentId, callback)
}
