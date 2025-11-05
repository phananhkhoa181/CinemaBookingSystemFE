package com.example.cinemabookingsystemfe.ui.payment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.PaymentDetailResponse;
import com.example.cinemabookingsystemfe.data.repository.PaymentRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * PaymentResultActivity - Màn hình kết quả thanh toán
 * Nhận deep link từ VNPay callback: movieapp://payment/result?bookingId=156&status=success
 */
public class PaymentResultActivity extends AppCompatActivity {
    
    private ImageView ivResultIcon;
    private TextView tvResultTitle, tvResultMessage, tvBookingCode, tvTransactionCode;
    private TextView tvAmount, tvPaymentMethod, tvPaymentTime;
    private MaterialCardView cardBookingCode;
    private MaterialButton btnViewBooking, btnBackHome;
    
    private PaymentRepository paymentRepository;
    private int paymentId;
    private int bookingId;
    private String status;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);
        
        initViews();
        paymentRepository = PaymentRepository.getInstance(this);
        
        handleDeepLink();
        
        setupListeners();
    }
    
    private void initViews() {
        ivResultIcon = findViewById(R.id.ivResultIcon);
        tvResultTitle = findViewById(R.id.tvResultTitle);
        tvResultMessage = findViewById(R.id.tvResultMessage);
        tvBookingCode = findViewById(R.id.tvBookingCode);
        tvTransactionCode = findViewById(R.id.tvTransactionCode);
        tvAmount = findViewById(R.id.tvAmount);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvPaymentTime = findViewById(R.id.tvPaymentTime);
        cardBookingCode = findViewById(R.id.cardBookingCode);
        btnViewBooking = findViewById(R.id.btnViewBooking);
        btnBackHome = findViewById(R.id.btnBackHome);
    }
    
    private void handleDeepLink() {
        Intent intent = getIntent();
        
        // Check if coming from polling (direct intent)
        boolean fromPolling = intent.getBooleanExtra("FROM_POLLING", false);
        if (fromPolling) {
            android.util.Log.d("PaymentResult", "Received from polling");
            paymentId = intent.getIntExtra("PAYMENT_ID", 0);
            bookingId = intent.getIntExtra("BOOKING_ID", 0);
            status = intent.getStringExtra("STATUS");
            
            if ("success".equals(status)) {
                showSuccessUI();
                if (paymentId > 0) {
                    verifyPaymentStatus();
                }
            } else {
                showFailureUI();
            }
            return;
        }
        
        // Handle deep link from browser
        Uri data = intent.getData();
        
        android.util.Log.d("PaymentResult", "Intent action: " + intent.getAction());
        android.util.Log.d("PaymentResult", "Intent data URI: " + (data != null ? data.toString() : "null"));
        
        if (data != null && "movieapp".equals(data.getScheme())) {
            // Deep link format: movieapp://payment/result?bookingId=156&status=success
            String bookingIdParam = data.getQueryParameter("bookingId");
            status = data.getQueryParameter("status");
            
            android.util.Log.d("PaymentResult", "Scheme: " + data.getScheme());
            android.util.Log.d("PaymentResult", "Host: " + data.getHost());
            android.util.Log.d("PaymentResult", "Path: " + data.getPath());
            android.util.Log.d("PaymentResult", "Query: " + data.getQuery());
            android.util.Log.d("PaymentResult", "Deep link received - bookingId: " + bookingIdParam + ", status: " + status);
            
            // Get stored payment ID
            SharedPreferences prefs = getSharedPreferences("PaymentPrefs", MODE_PRIVATE);
            paymentId = prefs.getInt("PENDING_PAYMENT_ID", 0);
            
            android.util.Log.d("PaymentResult", "Payment ID from prefs: " + paymentId);
            
            // If bookingId is provided, use it
            if (bookingIdParam != null) {
                bookingId = Integer.parseInt(bookingIdParam);
            }
            
            // Check if we have either paymentId or status to continue
            if ((paymentId > 0 || bookingId > 0) && status != null) {
                if ("success".equals(status)) {
                    // Payment successful
                    showSuccessUI();
                    // Verify and get booking code from API
                    if (paymentId > 0) {
                        verifyPaymentStatus();
                    }
                } else {
                    // Payment failed
                    showFailureUI();
                }
                return;
            }
        }
        
        // No valid deep link - show error
        android.util.Log.e("PaymentResult", "Invalid deep link - paymentId: " + paymentId + ", bookingId: " + bookingId + ", status: " + status);
        Toast.makeText(this, "Truy cập không hợp lệ", Toast.LENGTH_SHORT).show();
        finish();
    }
    
    private void showSuccessUI() {
        ivResultIcon.setImageResource(R.drawable.ic_check_circle);
        tvResultTitle.setText("Thanh toán thành công!");
        tvResultMessage.setText("Đặt vé của bạn đã được xác nhận");
    }
    
    private void showFailureUI() {
        ivResultIcon.setImageResource(R.drawable.ic_error);
        tvResultTitle.setText("Thanh toán thất bại");
        tvResultMessage.setText("Đã có lỗi xảy ra trong quá trình thanh toán");
        cardBookingCode.setVisibility(android.view.View.GONE);
        btnViewBooking.setVisibility(android.view.View.GONE);
    }
    
    private void verifyPaymentStatus() {
        paymentRepository.getPaymentDetails(paymentId, new ApiCallback<ApiResponse<PaymentDetailResponse>>() {
            @Override
            public void onSuccess(ApiResponse<PaymentDetailResponse> response) {
                if (response != null && response.getData() != null) {
                    PaymentDetailResponse payment = response.getData();
                    
                    android.util.Log.d("PaymentResult", "Payment details received");
                    android.util.Log.d("PaymentResult", "Status: " + payment.getStatus());
                    android.util.Log.d("PaymentResult", "Amount: " + payment.getAmount());
                    
                    // Update UI with payment details
                    updatePaymentDetails(payment);
                }
            }
            
            @Override
            public void onError(String error) {
                android.util.Log.e("PaymentResult", "Error getting payment details: " + error);
                // Show basic info without API details
            }
        });
    }
    
    private void updatePaymentDetails(PaymentDetailResponse payment) {
        // Format amount
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvAmount.setText(formatter.format(payment.getAmount()) + "đ");
        
        // Payment method
        if (payment.getMethod() != null) {
            tvPaymentMethod.setText(payment.getMethod().getName());
        }
        
        // Payment time
        if (payment.getPaymentTime() != null) {
            tvPaymentTime.setText(formatDateTime(payment.getPaymentTime()));
        }
        
        // Booking code
        if (payment.getBooking() != null && payment.getBooking().getBookingCode() != null) {
            tvBookingCode.setText(payment.getBooking().getBookingCode());
            android.util.Log.d("PaymentResult", "Booking code: " + payment.getBooking().getBookingCode());
        }
        
        // Transaction code
        if (payment.getTransactionCode() != null) {
            String shortCode = payment.getTransactionCode();
            if (shortCode.length() > 20) {
                shortCode = shortCode.substring(0, 20) + "...";
            }
            tvTransactionCode.setText("Giao dịch: " + shortCode);
        }
    }
    
    private String formatDateTime(String dateTimeStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(dateTimeStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateTimeStr;
        }
    }
    
    private void setupListeners() {
        btnViewBooking.setOnClickListener(v -> {
            // Navigate to booking history tab
            Intent intent = new Intent(this, com.example.cinemabookingsystemfe.ui.main.MainActivity.class);
            intent.putExtra("NAVIGATE_TO_TAB", "BOOKINGS"); // Navigate to bookings tab
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        
        btnBackHome.setOnClickListener(v -> {
            // Navigate to home
            Intent intent = new Intent(this, com.example.cinemabookingsystemfe.ui.main.MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
    
    @Override
    public void onBackPressed() {
        // Prevent back, must use buttons
        // Or go to home
        Intent intent = new Intent(this, com.example.cinemabookingsystemfe.ui.main.MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
