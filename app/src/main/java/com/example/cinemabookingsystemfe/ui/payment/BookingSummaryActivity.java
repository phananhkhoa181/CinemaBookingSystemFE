package com.example.cinemabookingsystemfe.ui.payment;

import android.content.Intent;
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

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.Combo;
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
    private MaterialButton btnApplyVoucher, btnPayment;
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
        seatNames = getIntent().getStringExtra(EXTRA_SEAT_NAMES);
        seatCount = getIntent().getIntExtra(EXTRA_SEAT_COUNT, 0);
        seatPrice = getIntent().getDoubleExtra(EXTRA_SEAT_PRICE, 0);
        comboPrice = getIntent().getDoubleExtra(EXTRA_COMBO_PRICE, 0);
        timerRemaining = getIntent().getLongExtra(EXTRA_TIMER_REMAINING, 15 * 60 * 1000);
        
        // Get combo data (HashMap)
        comboData = (HashMap<String, Integer>) getIntent().getSerializableExtra(EXTRA_COMBO_DATA);
        if (comboData == null) {
            comboData = new HashMap<>();
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
        
        // Poster placeholder (movie details chưa có)
        // TODO: Load poster from movie details API
        ivMoviePoster.setImageResource(0); // Empty for now
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
    }

    private void applyMockVoucher(String voucherCode) {
        // Mock voucher validation
        // TODO: Replace with API call
        
        if (voucherCode.equals("DISCOUNT10")) {
            double subtotal = seatPrice + comboPrice;
            discountAmount = subtotal * 0.1; // 10% discount
            appliedVoucherCode = voucherCode;
            
            tvVoucherName.setText("Giảm 10% tổng đơn");
            layoutAppliedVoucher.setVisibility(View.VISIBLE);
            etVoucherCode.setEnabled(false);
            btnApplyVoucher.setEnabled(false);
            
            calculateTotal();
            Toast.makeText(this, "Áp dụng voucher thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Mã voucher không hợp lệ", Toast.LENGTH_SHORT).show();
        }
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
        
        // TODO: Create booking and payment
        // For now, show toast
        double total = seatPrice + comboPrice - discountAmount;
        DecimalFormat formatter = new DecimalFormat("#,###");
        
        Toast.makeText(this, 
            "Thanh toán: " + formatter.format(total) + "đ\n" +
            "(VNPay WebView chưa được implement)", 
            Toast.LENGTH_LONG).show();
        
        // TODO: Navigate to VNPayWebViewActivity
        // Intent intent = new Intent(this, VNPayWebViewActivity.class);
        // intent.putExtra("PAYMENT_URL", paymentUrl);
        // intent.putExtra("BOOKING_ID", bookingId);
        // startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
