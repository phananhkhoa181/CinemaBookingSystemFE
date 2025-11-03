package com.example.cinemabookingsystemfe.ui.booking;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.Combo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SelectComboActivity - Chọn combo bắp nước
 * Features:
 * - Display combo list with images
 * - Quantity selector (+/-)
 * - Countdown timer (continues from SelectSeatActivity)
 * - Calculate total combo price
 * - Bottom popup showing selected combo
 * - Skip or Continue to BookingSummaryActivity
 */
public class SelectComboActivity extends AppCompatActivity {

    public static final String EXTRA_BOOKING_ID = "booking_id";
    public static final String EXTRA_SHOWTIME_ID = "showtime_id";
    public static final String EXTRA_SEAT_IDS = "seat_ids";
    public static final String EXTRA_SEAT_NAMES = "seat_names";
    public static final String EXTRA_SEAT_TOTAL_PRICE = "seat_total_price";
    public static final String EXTRA_MOVIE_TITLE = "movie_title";
    public static final String EXTRA_CINEMA_NAME = "cinema_name";
    public static final String EXTRA_SHOWTIME_INFO = "showtime_info";
    public static final String EXTRA_FORMAT = "format";
    public static final String EXTRA_MOVIE_RATING = "movie_rating";
    public static final String EXTRA_TIMER_REMAINING = "timer_remaining";

    private MaterialToolbar toolbar;
    private TextView tvTimeRemaining, tvTotalAmount, tvSelectedSeats;
    private RecyclerView rvCombos;
    private MaterialButton btnSkip, btnContinue;
    private LinearLayout layoutComboSummary, layoutComboList, btnToggleComboList;
    private ImageView ivToggleIcon;
    
    private boolean isComboListExpanded = true; // Start expanded

    private ComboAdapter adapter;
    private CountDownTimer countDownTimer;

    private int bookingId;
    private int showtimeId;
    private ArrayList<Integer> seatIds;
    private String selectedSeatsText;
    private String movieTitle;
    private String cinemaName;
    private String showtimeInfo;
    private String format;
    private String movieRating;
    private double seatTotalPrice; // Total price of selected seats
    private long timerRemaining; // in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_combo);

        // Get data from intent
        bookingId = getIntent().getIntExtra(EXTRA_BOOKING_ID, 0);
        showtimeId = getIntent().getIntExtra(EXTRA_SHOWTIME_ID, 0);
        seatIds = getIntent().getIntegerArrayListExtra(EXTRA_SEAT_IDS);
        selectedSeatsText = getIntent().getStringExtra(EXTRA_SEAT_NAMES);
        seatTotalPrice = getIntent().getDoubleExtra(EXTRA_SEAT_TOTAL_PRICE, 0);
        movieTitle = getIntent().getStringExtra(EXTRA_MOVIE_TITLE);
        cinemaName = getIntent().getStringExtra(EXTRA_CINEMA_NAME);
        showtimeInfo = getIntent().getStringExtra(EXTRA_SHOWTIME_INFO);
        format = getIntent().getStringExtra(EXTRA_FORMAT);
        movieRating = getIntent().getStringExtra(EXTRA_MOVIE_RATING);
        timerRemaining = getIntent().getLongExtra(EXTRA_TIMER_REMAINING, 15 * 60 * 1000);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupListeners();
        loadMockCombos();
        displaySelectedSeats();
        updateTotalAmount(); // Initialize with seat price
        startCountdownTimer();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTimeRemaining = findViewById(R.id.tvTimeRemaining);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvSelectedSeats = findViewById(R.id.tvSelectedSeats);
        rvCombos = findViewById(R.id.rvCombos);
        btnSkip = findViewById(R.id.btnSkip);
        btnContinue = findViewById(R.id.btnContinue);
        layoutComboSummary = findViewById(R.id.layoutComboSummary);
        layoutComboList = findViewById(R.id.layoutComboList);
        btnToggleComboList = findViewById(R.id.btnToggleComboList);
        ivToggleIcon = findViewById(R.id.ivToggleIcon);
    }

    private void displaySelectedSeats() {
        if (selectedSeatsText != null && !selectedSeatsText.isEmpty()) {
            tvSelectedSeats.setText(selectedSeatsText);
        } else {
            tvSelectedSeats.setText("Không có ghế");
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        adapter = new ComboAdapter();
        adapter.setOnQuantityChangeListener((combo, quantity) -> {
            updateTotalAmount();
            showComboPopup(combo, quantity);
        });

        rvCombos.setLayoutManager(new LinearLayoutManager(this));
        rvCombos.setAdapter(adapter);
    }

    private void setupListeners() {
        // Skip button - go directly to BookingSummary without combos
        btnSkip.setOnClickListener(v -> {
            navigateToBookingSummary(new ArrayList<>(), 0);
        });

        // Continue button - save combos and go to BookingSummary
        btnContinue.setOnClickListener(v -> {
            List<Combo> selectedCombos = getSelectedCombos();
            double totalComboPrice = calculateTotalComboPrice();
            navigateToBookingSummary(selectedCombos, totalComboPrice);
        });

        // Toggle combo list expand/collapse
        btnToggleComboList.setOnClickListener(v -> {
            toggleComboList();
        });
    }

    private void toggleComboList() {
        isComboListExpanded = !isComboListExpanded;
        
        if (isComboListExpanded) {
            // Expand - show list, arrow points down
            layoutComboList.setVisibility(View.VISIBLE);
            ivToggleIcon.animate().rotation(0f).setDuration(200).start();
        } else {
            // Collapse - hide list, arrow points up
            layoutComboList.setVisibility(View.GONE);
            ivToggleIcon.animate().rotation(180f).setDuration(200).start();
        }
    }

    private void loadMockCombos() {
        // Mock data - Replace with API call later
        List<Combo> combos = new ArrayList<>();
        
        combos.add(new Combo(1, "Combo 2 Big", 
            "Nhân đôi sự sảng khoái! Combo gồm 1 bắp rang bơ lớn, 2 Pepsi cỡ lớn – tiết kiệm hơn 28,000!", 
            109000, "combo1bap2nuoc"));
        
        combos.add(new Combo(2, "Combo 1 Big", 
            "Combo tiết kiệm! Gồm 1 bắp rang bơ lớn, 1 Pepsi cỡ lớn", 
            89000, "combo1bap1nuoc"));
        
        combos.add(new Combo(3, "Pepsi", 
            "Nước ngọt Pepsi cỡ lớn", 
            35000, "pepsi"));
        
        combos.add(new Combo(4, "Sprite", 
            "Nước ngọt Sprite cỡ lớn", 
            35000, "sprite"));
        
        combos.add(new Combo(5, "Nước suối Aqua", 
            "Nước khoáng Aqua 500ml", 
            15000, "aqua"));
        
        combos.add(new Combo(6, "Bắp rang bơ", 
            "Bắp rang bơ thơm ngon cỡ lớn", 
            60000, "baprang"));

        adapter.setCombos(combos);
    }

    private void updateTotalAmount() {
        double comboPrice = calculateTotalComboPrice();
        double total = seatTotalPrice + comboPrice; // Seat price + Combo price
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvTotalAmount.setText(formatter.format(total) + "đ");
    }

    private double calculateTotalComboPrice() {
        double total = 0;
        Map<Integer, Integer> quantities = adapter.getQuantities();
        
        for (Map.Entry<Integer, Integer> entry : quantities.entrySet()) {
            int comboId = entry.getKey();
            int quantity = entry.getValue();
            
            // Find combo by ID and calculate price
            Combo combo = findComboById(comboId);
            if (combo != null && quantity > 0) {
                total += combo.getPrice() * quantity;
            }
        }
        
        return total;
    }

    private List<Combo> getSelectedCombos() {
        List<Combo> selectedCombos = new ArrayList<>();
        Map<Integer, Integer> quantities = adapter.getQuantities();
        
        for (Map.Entry<Integer, Integer> entry : quantities.entrySet()) {
            int comboId = entry.getKey();
            int quantity = entry.getValue();
            
            if (quantity > 0) {
                Combo combo = findComboById(comboId);
                if (combo != null) {
                    // Add combo multiple times based on quantity
                    for (int i = 0; i < quantity; i++) {
                        selectedCombos.add(combo);
                    }
                }
            }
        }
        
        return selectedCombos;
    }

    private Combo findComboById(int comboId) {
        // Search through adapter's combo list
        for (int i = 0; i < adapter.getItemCount(); i++) {
            // We need to get combo from adapter - using a workaround
            // In production, consider using a HashMap in adapter
        }
        
        // Fallback: recreate combo list (not efficient, but works for mock data)
        List<Combo> combos = getMockCombosList();
        for (Combo combo : combos) {
            if (combo.getComboId() == comboId) {
                return combo;
            }
        }
        
        return null;
    }

    private List<Combo> getMockCombosList() {
        List<Combo> combos = new ArrayList<>();
        combos.add(new Combo(1, "Combo 2 Big", 
            "Nhân đôi sự sảng khoái! Combo gồm 1 bắp rang bơ lớn, 2 Pepsi cỡ lớn – tiết kiệm hơn 28,000!", 
            109000, "combo1bap2nuoc"));
        combos.add(new Combo(2, "Combo 1 Big", 
            "Combo tiết kiệm! Gồm 1 bắp rang bơ lớn, 1 Pepsi cỡ lớn", 
            89000, "combo1bap1nuoc"));
        combos.add(new Combo(3, "Pepsi", 
            "Nước ngọt Pepsi cỡ lớn", 
            35000, "pepsi"));
        combos.add(new Combo(4, "Sprite", 
            "Nước ngọt Sprite cỡ lớn", 
            35000, "sprite"));
        combos.add(new Combo(5, "Nước suối Aqua", 
            "Nước khoáng Aqua 500ml", 
            15000, "aqua"));
        combos.add(new Combo(6, "Bắp rang bơ", 
            "Bắp rang bơ thơm ngon cỡ lớn", 
            60000, "baprang"));
        return combos;
    }

    private void showComboPopup(Combo combo, int quantity) {
        // Update the combo list display
        updateComboSummaryList();
    }

    private void updateComboSummaryList() {
        // Clear existing views
        layoutComboList.removeAllViews();
        
        // Check if any combo is selected
        Map<Integer, Integer> quantities = adapter.getQuantities();
        boolean hasSelection = false;
        
        for (Map.Entry<Integer, Integer> entry : quantities.entrySet()) {
            int comboId = entry.getKey();
            int quantity = entry.getValue();
            
            if (quantity > 0) {
                hasSelection = true;
                Combo combo = findComboById(comboId);
                if (combo != null) {
                    // Inflate custom layout for each combo
                    View comboItemView = LayoutInflater.from(this)
                        .inflate(R.layout.item_combo_summary, layoutComboList, false);
                    
                    ImageView ivComboIcon = comboItemView.findViewById(R.id.ivComboIcon);
                    TextView tvComboName = comboItemView.findViewById(R.id.tvComboName);
                    ImageView btnRemoveComboItem = comboItemView.findViewById(R.id.btnRemoveComboItem);
                    
                    // Set combo icon
                    int imageResource = getImageResource(combo.getImageUrl());
                    if (imageResource != 0) {
                        ivComboIcon.setImageResource(imageResource);
                    }
                    
                    // Set combo name with quantity
                    tvComboName.setText(quantity + "x " + combo.getName());
                    
                    // Handle remove button click
                    final int finalComboId = comboId;
                    btnRemoveComboItem.setOnClickListener(v -> {
                        // Reset quantity to 0 in adapter
                        adapter.resetComboQuantity(finalComboId);
                        // Update UI
                        updateTotalAmount();
                        updateComboSummaryList();
                    });
                    
                    layoutComboList.addView(comboItemView);
                }
            }
        }
        
        // Show or hide the summary based on selection
        if (hasSelection) {
            if (layoutComboSummary.getVisibility() != View.VISIBLE) {
                layoutComboSummary.setVisibility(View.VISIBLE);
                layoutComboSummary.setAlpha(0f);
                layoutComboSummary.setTranslationY(50f);
                layoutComboSummary.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(300)
                    .setListener(null);
            }
        } else {
            layoutComboSummary.animate()
                .alpha(0f)
                .translationY(50f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        layoutComboSummary.setVisibility(View.GONE);
                    }
                });
        }
    }

    private int getImageResource(String imageUrl) {
        // Map image URL to drawable resource
        switch (imageUrl) {
            case "combo1bap2nuoc":
                return R.drawable.combo1bap2nuoc;
            case "combo1bap1nuoc":
                return R.drawable.combo1bap1nuoc;
            case "pepsi":
                return R.drawable.pepsi;
            case "sprite":
                return R.drawable.sprite;
            case "aqua":
                return R.drawable.aqua;
            case "baprang":
                return R.drawable.baprang;
            default:
                return R.drawable.combo1bap2nuoc;
        }
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(timerRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerRemaining = millisUntilFinished;
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                tvTimeRemaining.setText(String.format("Thời gian giữ ghế: %02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                Toast.makeText(SelectComboActivity.this, 
                    "Hết thời gian giữ ghế! Vui lòng chọn lại.", 
                    Toast.LENGTH_LONG).show();
                finish();
            }
        };
        countDownTimer.start();
    }

    private void navigateToBookingSummary(List<Combo> selectedCombos, double totalComboPrice) {
        // Stop timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Build combo data HashMap
        HashMap<String, Integer> comboData = new HashMap<>();
        Map<Integer, Integer> quantities = adapter.getQuantities();
        
        for (Combo combo : selectedCombos) {
            int quantity = quantities.getOrDefault(combo.getComboId(), 0);
            if (quantity > 0) {
                comboData.put(combo.getName(), quantity);
            }
        }

        // Navigate to BookingSummaryActivity
        Intent intent = new Intent(this, com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.class);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_BOOKING_ID, bookingId);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_SHOWTIME_ID, showtimeId);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_MOVIE_TITLE, movieTitle);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_CINEMA_NAME, cinemaName);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_SHOWTIME, showtimeInfo);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_FORMAT, format);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_RATING, movieRating);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_SEAT_NAMES, selectedSeatsText);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_SEAT_COUNT, seatIds != null ? seatIds.size() : 0);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_SEAT_PRICE, seatTotalPrice);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_COMBO_DATA, comboData);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_COMBO_PRICE, totalComboPrice);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_TIMER_REMAINING, timerRemaining);
        
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
