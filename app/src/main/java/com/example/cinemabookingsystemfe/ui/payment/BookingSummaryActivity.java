package com.example.cinemabookingsystemfe.ui.payment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.ApplyVoucherResponse;
import com.example.cinemabookingsystemfe.data.models.response.Combo;
import com.example.cinemabookingsystemfe.data.models.response.CreatePaymentResponse;
import com.example.cinemabookingsystemfe.data.models.response.PaymentDetailResponse;
import com.example.cinemabookingsystemfe.data.repository.PaymentRepository;
import com.example.cinemabookingsystemfe.data.repository.VoucherRepository;
import com.example.cinemabookingsystemfe.ui.booking.SelectComboActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BookingSummaryActivity - Trang thanh toán
 * Hiển thị: movie info, seats, combos, voucher, total price
 * Chuyển sang VNPay payment
 */
public class BookingSummaryActivity extends AppCompatActivity {

    public static final String EXTRA_BOOKING_ID = "booking_id";
    public static final String EXTRA_SHOWTIME_ID = "showtime_id";
    public static final String EXTRA_MOVIE_TITLE = "movie_title";
    public static final String EXTRA_CINEMA_NAME = "cinema_name";
    public static final String EXTRA_SHOWTIME = "showtime";
    public static final String EXTRA_FORMAT = "format";
    public static final String EXTRA_RATING = "rating";
    public static final String EXTRA_MOVIE_POSTER = "movie_poster";
    public static final String EXTRA_SEAT_NAMES = "seat_names";
    public static final String EXTRA_SEAT_COUNT = "seat_count";
    public static final String EXTRA_SEAT_PRICE = "seat_price";
    public static final String EXTRA_COMBO_DATA = "combo_data";
    public static final String EXTRA_COMBO_PRICE = "combo_price";
    public static final String EXTRA_TIMER_REMAINING = "timer_remaining";

    private MaterialToolbar toolbar;
    private ImageView ivMoviePoster;
    private TextView tvMovieTitle, tvFormat, tvRating, tvCinemaName, tvShowtime;
    private TextView tvSeats, tvSeatCount, tvTicketPrice;
    private TextView tvComboSectionHeader;
    private MaterialCardView cardCombos;
    private LinearLayout layoutComboList;
    private TextView tvComboPrice;
    private EditText etVoucherCode;
    private MaterialButton btnApplyVoucher, btnPayment, btnCancelBooking;
    private LinearLayout layoutAppliedVoucher, layoutDiscount;
    private TextView tvVoucherName, tvSubtotal, tvDiscount, tvTotal;
    private ImageView btnRemoveVoucher;

    private CountDownTimer countDownTimer;

    private int bookingId;
    private int showtimeId;
    private String movieTitle;
    private String cinemaName;
    private String showtimeInfo;
    private String format;
    private String rating;
    private String moviePosterUrl;
    private String seatNames;
    private int seatCount;
    private double seatPrice;
    private double comboPrice;
    private long timerRemaining;
    
    private HashMap<String, Integer> comboData; // comboName -> quantity
    
    private double discountAmount = 0;
    private String appliedVoucherCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        // Get data from intent
        getIntentData();

        initViews();
        setupToolbar();
        displayMovieInfo();
        displaySeats();
        displayCombos();
        calculateTotal();
        setupListeners();
        startCountdownTimer();
    }

    private void getIntentData() {
        bookingId = getIntent().getIntExtra(EXTRA_BOOKING_ID, 0);
        showtimeId = getIntent().getIntExtra(EXTRA_SHOWTIME_ID, 0);
        movieTitle = getIntent().getStringExtra(EXTRA_MOVIE_TITLE);
        cinemaName = getIntent().getStringExtra(EXTRA_CINEMA_NAME);
        showtimeInfo = getIntent().getStringExtra(EXTRA_SHOWTIME);
        format = getIntent().getStringExtra(EXTRA_FORMAT);
        rating = getIntent().getStringExtra(EXTRA_RATING);
        moviePosterUrl = getIntent().getStringExtra(EXTRA_MOVIE_POSTER);
        seatNames = getIntent().getStringExtra(EXTRA_SEAT_NAMES);
        seatCount = getIntent().getIntExtra(EXTRA_SEAT_COUNT, 0);
        seatPrice = getIntent().getDoubleExtra(EXTRA_SEAT_PRICE, 0);
        comboPrice = getIntent().getDoubleExtra(EXTRA_COMBO_PRICE, 0);
        timerRemaining = getIntent().getLongExtra(EXTRA_TIMER_REMAINING, 15 * 60 * 1000);
        
        android.util.Log.d("BookingSummary", "=== RECEIVED DATA ===");
        android.util.Log.d("BookingSummary", "Booking ID: " + bookingId);
        android.util.Log.d("BookingSummary", "Seat Price: " + seatPrice);
        android.util.Log.d("BookingSummary", "Combo Price: " + comboPrice);
        
        // Get combo data (HashMap)
        comboData = (HashMap<String, Integer>) getIntent().getSerializableExtra(EXTRA_COMBO_DATA);
        if (comboData == null) {
            comboData = new HashMap<>();
        }
        
        android.util.Log.d("BookingSummary", "Combo Data size: " + comboData.size());
        for (Map.Entry<String, Integer> entry : comboData.entrySet()) {
            android.util.Log.d("BookingSummary", "  - " + entry.getKey() + " x " + entry.getValue());
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        ivMoviePoster = findViewById(R.id.ivMoviePoster);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvFormat = findViewById(R.id.tvFormat);
        tvRating = findViewById(R.id.tvRating);
        tvCinemaName = findViewById(R.id.tvCinemaName);
        tvShowtime = findViewById(R.id.tvShowtime);
        tvSeats = findViewById(R.id.tvSeats);
        tvSeatCount = findViewById(R.id.tvSeatCount);
        tvTicketPrice = findViewById(R.id.tvTicketPrice);
        tvComboSectionHeader = findViewById(R.id.tvComboSectionHeader);
        cardCombos = findViewById(R.id.cardCombos);
        layoutComboList = findViewById(R.id.layoutComboList);
        tvComboPrice = findViewById(R.id.tvComboPrice);
        etVoucherCode = findViewById(R.id.etVoucherCode);
        btnApplyVoucher = findViewById(R.id.btnApplyVoucher);
        btnPayment = findViewById(R.id.btnPayment);
        btnCancelBooking = findViewById(R.id.btnCancelBooking);
        layoutAppliedVoucher = findViewById(R.id.layoutAppliedVoucher);
        layoutDiscount = findViewById(R.id.layoutDiscount);
        tvVoucherName = findViewById(R.id.tvVoucherName);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTotal = findViewById(R.id.tvTotal);
        btnRemoveVoucher = findViewById(R.id.btnRemoveVoucher);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void displayMovieInfo() {
        if (movieTitle != null) {
            tvMovieTitle.setText(movieTitle);
        }
        
        if (format != null) {
            tvFormat.setText(format);
        }
        
        if (rating != null) {
            tvRating.setText(rating);
            setRatingBackground(rating);
        }
        
        if (cinemaName != null) {
            tvCinemaName.setText(cinemaName);
        }
        
        if (showtimeInfo != null) {
            tvShowtime.setText(showtimeInfo);
        }
        
        // Load movie poster using Glide
        if (moviePosterUrl != null && !moviePosterUrl.isEmpty()) {
            com.bumptech.glide.Glide.with(this)
                .load(moviePosterUrl)
                .placeholder(R.drawable.bg_movie_placeholder)
                .error(R.drawable.bg_movie_placeholder)
                .centerCrop()
                .into(ivMoviePoster);
        } else {
            ivMoviePoster.setImageResource(R.drawable.bg_movie_placeholder);
        }
    }

    private void setRatingBackground(String rating) {
        int backgroundRes;
        switch (rating.toUpperCase()) {
            case "P":
                backgroundRes = R.drawable.bg_rating_p;
                break;
            case "C13":
            case "T13":
                backgroundRes = R.drawable.bg_rating_c13;
                break;
            case "C16":
            case "T16":
                backgroundRes = R.drawable.bg_rating_c16;
                break;
            case "C18":
            case "T18":
                backgroundRes = R.drawable.bg_rating_c18;
                break;
            default:
                backgroundRes = R.drawable.bg_rating_badge;
                break;
        }
        tvRating.setBackgroundResource(backgroundRes);
    }

    private void displaySeats() {
        if (seatNames != null) {
            tvSeats.setText(seatNames);
        }
        
        tvSeatCount.setText(String.valueOf(seatCount));
        
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvTicketPrice.setText(formatter.format(seatPrice) + " đ");
    }

    private void displayCombos() {
        if (comboData == null || comboData.isEmpty()) {
            tvComboSectionHeader.setVisibility(View.GONE);
            cardCombos.setVisibility(View.GONE);
            return;
        }
        
        tvComboSectionHeader.setVisibility(View.VISIBLE);
        cardCombos.setVisibility(View.VISIBLE);
        layoutComboList.removeAllViews();
        
        DecimalFormat formatter = new DecimalFormat("#,###");
        
        for (Map.Entry<String, Integer> entry : comboData.entrySet()) {
            String comboName = entry.getKey();
            int quantity = entry.getValue();
            
            if (quantity > 0) {
                View comboItemView = LayoutInflater.from(this)
                    .inflate(R.layout.item_booking_combo, layoutComboList, false);
                
                ImageView ivComboIcon = comboItemView.findViewById(R.id.ivComboIcon);
                TextView tvComboItem = comboItemView.findViewById(R.id.tvComboItem);
                
                // Set combo image based on combo name
                int imageRes = getComboImageResource(comboName);
                ivComboIcon.setImageResource(imageRes);
                
                tvComboItem.setText(quantity + "x " + comboName);
                
                layoutComboList.addView(comboItemView);
            }
        }
        
        tvComboPrice.setText(formatter.format(comboPrice) + " đ");
    }

    private void calculateTotal() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        
        double subtotal = seatPrice + comboPrice;
        tvSubtotal.setText(formatter.format(subtotal) + " đ");
        
        double total = subtotal - discountAmount;
        tvTotal.setText(formatter.format(total) + " đ");
        
        // Always show discount line
        layoutDiscount.setVisibility(View.VISIBLE);
        if (discountAmount > 0) {
            tvDiscount.setText(formatter.format(discountAmount) + " đ");
            tvDiscount.setTextColor(getResources().getColor(R.color.colorPrimary));
            // Also update label color
            TextView tvDiscountLabel = (TextView) layoutDiscount.getChildAt(0);
            tvDiscountLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            tvDiscount.setText("0 đ");
            tvDiscount.setTextColor(getResources().getColor(R.color.textSecondary));
            // Also update label color
            TextView tvDiscountLabel = (TextView) layoutDiscount.getChildAt(0);
            tvDiscountLabel.setTextColor(getResources().getColor(R.color.textSecondary));
        }
    }

    private void setupListeners() {
        // Apply voucher
        btnApplyVoucher.setOnClickListener(v -> {
            String voucherCode = etVoucherCode.getText().toString().trim().toUpperCase();
            if (voucherCode.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã voucher", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // TODO: Call API to validate voucher
            // For now, mock validation
            applyMockVoucher(voucherCode);
        });
        
        // Remove voucher
        btnRemoveVoucher.setOnClickListener(v -> {
            removeVoucher();
        });
        
        // Payment button
        btnPayment.setOnClickListener(v -> {
            proceedToPayment();
        });
        
        // Cancel booking button
        btnCancelBooking.setOnClickListener(v -> {
            showCancelConfirmationDialog();
        });
    }

    private void applyMockVoucher(String voucherCode) {
        // Call real API to apply voucher
        VoucherRepository voucherRepository = VoucherRepository.getInstance(this);
        
        // Disable button while processing
        btnApplyVoucher.setEnabled(false);
        btnApplyVoucher.setText("Đang xử lý...");
        
        voucherRepository.applyVoucher(bookingId, voucherCode, new ApiCallback<ApiResponse<ApplyVoucherResponse>>() {
            @Override
            public void onSuccess(ApiResponse<ApplyVoucherResponse> response) {
                runOnUiThread(() -> {
                    if (response != null && response.getData() != null) {
                        ApplyVoucherResponse data = response.getData();
                        
                        // Calculate discount
                        double subtotal = seatPrice + comboPrice;
                        double newTotal = data.getTotalAmount();
                        discountAmount = subtotal - newTotal;
                        appliedVoucherCode = voucherCode;
                        
                        android.util.Log.d("BookingSummary", "Voucher applied! Discount: " + discountAmount);
                        
                        tvVoucherName.setText("Mã: " + voucherCode);
                        layoutAppliedVoucher.setVisibility(View.VISIBLE);
                        etVoucherCode.setEnabled(false);
                        btnApplyVoucher.setEnabled(false);
                        btnApplyVoucher.setText("Áp dụng");
                        
                        calculateTotal();
                        Toast.makeText(BookingSummaryActivity.this, 
                            "Áp dụng voucher thành công!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    btnApplyVoucher.setEnabled(true);
                    btnApplyVoucher.setText("Áp dụng");
                    Toast.makeText(BookingSummaryActivity.this, error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void removeVoucher() {
        discountAmount = 0;
        appliedVoucherCode = "";
        
        layoutAppliedVoucher.setVisibility(View.GONE);
        etVoucherCode.setEnabled(true);
        etVoucherCode.setText("");
        btnApplyVoucher.setEnabled(true);
        
        calculateTotal();
        Toast.makeText(this, "Đã xóa voucher", Toast.LENGTH_SHORT).show();
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(timerRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerRemaining = millisUntilFinished;
                // TODO: Display timer in UI if needed
            }

            @Override
            public void onFinish() {
                Toast.makeText(BookingSummaryActivity.this, 
                    "Hết thời gian giữ ghế! Vui lòng chọn lại.", 
                    Toast.LENGTH_LONG).show();
                finish();
            }
        };
        countDownTimer.start();
    }

    private void proceedToPayment() {
        // Stop timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        // Show loading
        btnPayment.setEnabled(false);
        btnPayment.setText("Đang tạo thanh toán...");
        
        // Create VNPay payment
        PaymentRepository paymentRepository = PaymentRepository.getInstance(this);
        
        // Use Railway API callback URL (will redirect to app via deep link)
        String returnUrl = "https://movie88aspnet-app.up.railway.app/api/payments/vnpay/callback";
        
        paymentRepository.createVNPayPayment(bookingId, returnUrl, 
            new ApiCallback<ApiResponse<CreatePaymentResponse>>() {
            @Override
            public void onSuccess(ApiResponse<CreatePaymentResponse> response) {
                if (response != null && response.getData() != null) {
                    CreatePaymentResponse payment = response.getData();
                    
                    android.util.Log.d("BookingSummary", "Payment created: " + payment.getPaymentId());
                    android.util.Log.d("BookingSummary", "VNPay URL: " + payment.getVnpayUrl());
                    
                    // Save payment ID for result verification
                    savePaymentId(payment.getPaymentId());
                    
                    // Open VNPay payment URL in browser/Chrome Custom Tab
                    openVNPayPayment(payment.getVnpayUrl());
                } else {
                    btnPayment.setEnabled(true);
                    btnPayment.setText("THANH TOÁN");
                    Toast.makeText(BookingSummaryActivity.this, 
                        "Không thể tạo thanh toán", 
                        Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onError(String error) {
                btnPayment.setEnabled(true);
                btnPayment.setText("THANH TOÁN");
                Toast.makeText(BookingSummaryActivity.this, 
                    "Lỗi: " + error, 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void savePaymentId(int paymentId) {
        SharedPreferences prefs = getSharedPreferences("PaymentPrefs", MODE_PRIVATE);
        prefs.edit().putInt("PENDING_PAYMENT_ID", paymentId).apply();
        android.util.Log.d("BookingSummary", "Saved payment ID: " + paymentId);
    }
    
    private void openVNPayPayment(String vnpayUrl) {
        try {
            // Use Chrome Custom Tabs for better UX
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            
            // Customize toolbar color
            builder.setToolbarColor(getResources().getColor(R.color.colorPrimary, null));
            builder.setShowTitle(true);
            builder.setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out);
            builder.setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out);
            
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(vnpayUrl));
            
            android.util.Log.d("BookingSummary", "Opened VNPay URL in Chrome Custom Tab");
            
            // Start polling payment status after user goes to VNPay
            startPaymentStatusPolling();
            
        } catch (Exception e) {
            // Fallback to browser if Chrome Custom Tabs not available
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(vnpayUrl));
                startActivity(browserIntent);
                startPaymentStatusPolling();
            } catch (Exception ex) {
                Toast.makeText(this, "Không thể mở trang thanh toán", Toast.LENGTH_SHORT).show();
                android.util.Log.e("BookingSummary", "Error opening VNPay URL: " + ex.getMessage());
            }
        }
    }
    
    private android.os.Handler paymentCheckHandler;
    private Runnable paymentCheckRunnable;
    private int paymentCheckCount = 0;
    private static final int MAX_PAYMENT_CHECK = 60; // Check for 60 times (5 minutes with 5s interval)
    
    private void startPaymentStatusPolling() {
        paymentCheckHandler = new android.os.Handler();
        paymentCheckCount = 0;
        
        paymentCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (paymentCheckCount < MAX_PAYMENT_CHECK) {
                    checkPaymentStatus();
                    paymentCheckCount++;
                    // Check every 5 seconds
                    paymentCheckHandler.postDelayed(this, 5000);
                } else {
                    android.util.Log.d("BookingSummary", "Payment check timeout");
                }
            }
        };
        
        // Start checking after 5 seconds (give user time to complete payment)
        paymentCheckHandler.postDelayed(paymentCheckRunnable, 5000);
    }
    
    private void checkPaymentStatus() {
        SharedPreferences prefs = getSharedPreferences("PaymentPrefs", MODE_PRIVATE);
        int savedPaymentId = prefs.getInt("PENDING_PAYMENT_ID", 0);
        
        if (savedPaymentId <= 0) return;
        
        android.util.Log.d("BookingSummary", "Checking payment status - attempt " + paymentCheckCount);
        
        PaymentRepository paymentRepository = PaymentRepository.getInstance(this);
        paymentRepository.getPaymentDetails(savedPaymentId, new ApiCallback<ApiResponse<PaymentDetailResponse>>() {
            @Override
            public void onSuccess(ApiResponse<PaymentDetailResponse> response) {
                if (response != null && response.getData() != null) {
                    PaymentDetailResponse payment = response.getData();
                    
                    android.util.Log.d("BookingSummary", "Payment status: " + payment.getStatus());
                    
                    if ("Completed".equalsIgnoreCase(payment.getStatus())) {
                        // Payment successful! Stop polling and navigate to result
                        stopPaymentStatusPolling();
                        navigateToPaymentResult(true, payment);
                    } else if ("Failed".equalsIgnoreCase(payment.getStatus()) || 
                               "Cancelled".equalsIgnoreCase(payment.getStatus())) {
                        // Payment failed
                        stopPaymentStatusPolling();
                        navigateToPaymentResult(false, payment);
                    }
                    // If still "Pending", continue polling
                }
            }
            
            @Override
            public void onError(String error) {
                android.util.Log.e("BookingSummary", "Error checking payment: " + error);
            }
        });
    }
    
    private void stopPaymentStatusPolling() {
        if (paymentCheckHandler != null && paymentCheckRunnable != null) {
            paymentCheckHandler.removeCallbacks(paymentCheckRunnable);
            android.util.Log.d("BookingSummary", "Stopped payment status polling");
        }
    }
    
    private void navigateToPaymentResult(boolean success, PaymentDetailResponse payment) {
        Intent intent = new Intent(this, PaymentResultActivity.class);
        intent.putExtra("PAYMENT_ID", payment.getPaymentId());
        intent.putExtra("BOOKING_ID", payment.getBookingId());
        intent.putExtra("STATUS", success ? "success" : "failed");
        intent.putExtra("FROM_POLLING", true);
        startActivity(intent);
        finish();
    }

    /**
     * Get drawable resource ID for combo image based on combo name
     */
    private int getComboImageResource(String comboName) {
        if (comboName == null) {
            return R.drawable.combo1bap2nuoc; // Default
        }
        
        // Match combo name to drawable resource
        if (comboName.toLowerCase().contains("combo 1 big") || 
            comboName.toLowerCase().contains("1 bắp 2 nước")) {
            return R.drawable.combo1bap2nuoc;
        } else if (comboName.toLowerCase().contains("combo 1 small") || 
                   comboName.toLowerCase().contains("1 bắp 1 nước")) {
            return R.drawable.combo1bap1nuoc;
        } else if (comboName.toLowerCase().contains("pepsi")) {
            return R.drawable.pepsi;
        } else if (comboName.toLowerCase().contains("sprite") || 
                   comboName.toLowerCase().contains("7up")) {
            return R.drawable.sprite;
        } else if (comboName.toLowerCase().contains("aqua") || 
                   comboName.toLowerCase().contains("nước suối")) {
            return R.drawable.aqua;
        } else if (comboName.toLowerCase().contains("bắp rang") || 
                   comboName.toLowerCase().contains("popcorn")) {
            return R.drawable.baprang;
        } else {
            // Default fallback
            return R.drawable.combo1bap2nuoc;
        }
    }

    private void showCancelConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Hủy đặt vé")
            .setMessage("Bạn có chắc muốn hủy đặt vé này không?")
            .setPositiveButton("Có", (dialog, which) -> {
                cancelBookingAndFinish();
            })
            .setNegativeButton("Không", null)
            .show();
    }

    private void cancelBookingAndFinish() {
        VoucherRepository voucherRepository = VoucherRepository.getInstance(this);
        
        voucherRepository.cancelBooking(bookingId, new ApiCallback<ApiResponse<Void>>() {
            @Override
            public void onSuccess(ApiResponse<Void> response) {
                // Stop timer
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                
                Toast.makeText(BookingSummaryActivity.this, 
                    "Đã hủy đặt vé thành công", 
                    Toast.LENGTH_SHORT).show();
                
                // Navigate to home screen and clear back stack
                Intent intent = new Intent(BookingSummaryActivity.this, com.example.cinemabookingsystemfe.ui.main.MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            
            @Override
            public void onError(String error) {
                Toast.makeText(BookingSummaryActivity.this, 
                    "Lỗi hủy đặt vé: " + error, 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Show confirmation dialog before cancelling booking
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Thoát thanh toán")
            .setMessage("Nếu bạn thoát, đặt vé sẽ bị hủy. Bạn có chắc muốn thoát không?")
            .setPositiveButton("Có", (dialog, which) -> {
                cancelBookingAndFinish();
            })
            .setNegativeButton("Không", null)
            .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // Stop payment polling if activity is destroyed
        stopPaymentStatusPolling();
    }
}
