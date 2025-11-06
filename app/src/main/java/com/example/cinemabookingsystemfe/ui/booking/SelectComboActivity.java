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
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.request.AddCombosRequest;
import com.example.cinemabookingsystemfe.data.models.response.AddCombosResponse;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.AppliedPromotion;
import com.example.cinemabookingsystemfe.data.models.response.Combo;
import com.example.cinemabookingsystemfe.data.repository.ComboRepository;
import com.example.cinemabookingsystemfe.data.repository.VoucherRepository;
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
    public static final String EXTRA_MOVIE_POSTER = "movie_poster";
    public static final String EXTRA_TIMER_REMAINING = "timer_remaining";
    public static final String EXTRA_APPLIED_PROMOTIONS = "applied_promotions";

    private MaterialToolbar toolbar;
    private TextView tvTimeRemaining, tvTotalAmount, tvSelectedSeats;
    private RecyclerView rvCombos;
    private MaterialButton btnSkip, btnContinue;
    private LinearLayout layoutComboSummary, layoutComboList, btnToggleComboList;
    private ImageView ivToggleIcon;
    
    private boolean isComboListExpanded = true; // Start expanded

    private ComboAdapter adapter;
    private CountDownTimer countDownTimer;
    private ComboRepository comboRepository;

    private int bookingId;
    private int showtimeId;
    private ArrayList<Integer> seatIds;
    private String selectedSeatsText;
    private String movieTitle;
    private String cinemaName;
    private String showtimeInfo;
    private String format;
    private String movieRating;
    private String moviePosterUrl;
    private double seatTotalPrice; // Total price of selected seats (already discounted by promotion)
    private long timerRemaining; // in milliseconds
    private ArrayList<AppliedPromotion> appliedPromotions; // Promotions applied to seats

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
        moviePosterUrl = getIntent().getStringExtra(EXTRA_MOVIE_POSTER);
        timerRemaining = getIntent().getLongExtra(EXTRA_TIMER_REMAINING, 15 * 60 * 1000);
        appliedPromotions = (ArrayList<AppliedPromotion>) getIntent().getSerializableExtra(EXTRA_APPLIED_PROMOTIONS);

        // Initialize repository
        comboRepository = ComboRepository.getInstance(this);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupListeners();
        
        // Load combos from API
        loadCombosFromAPI();
        
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
        // Cancel booking button - cancel the booking and go back to home
        btnSkip.setOnClickListener(v -> {
            showCancelConfirmationDialog();
        });

        // Continue button - save combos via API and go to BookingSummary
        btnContinue.setOnClickListener(v -> {
            android.util.Log.d("SelectCombo", "Continue button clicked");
            
            // Check if any combo has quantity > 0
            Map<Integer, Integer> quantities = adapter.getQuantities();
            boolean hasCombo = false;
            
            for (Integer quantity : quantities.values()) {
                if (quantity > 0) {
                    hasCombo = true;
                    break;
                }
            }
            
            android.util.Log.d("SelectCombo", "Has combo with quantity > 0: " + hasCombo);
            android.util.Log.d("SelectCombo", "Total quantities entries: " + quantities.size());
            
            if (!hasCombo) {
                // No combo selected - just navigate without calling API
                android.util.Log.d("SelectCombo", "No combos selected - navigating without API call");
                navigateToBookingSummary(new ArrayList<>(), 0);
            } else {
                // Add combos to booking via API
                android.util.Log.d("SelectCombo", "Combos selected - calling API");
                addCombosToBookingAPI();
            }
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

    /**
     * Load combos from API: GET /api/combos
     */
    private void loadCombosFromAPI() {
        // Show loading state
        btnContinue.setEnabled(false);
        btnContinue.setText("Đang tải...");
        
        android.util.Log.d("SelectCombo", "Loading combos from API");
        
        comboRepository.getCombos(new ApiCallback<ApiResponse<List<Combo>>>() {
            @Override
            public void onSuccess(ApiResponse<List<Combo>> response) {
                runOnUiThread(() -> {
                    android.util.Log.d("SelectCombo", "API Response received: " + (response != null));
                    
                    if (response != null && response.getData() != null) {
                        List<Combo> combos = response.getData();
                        android.util.Log.d("SelectCombo", "Loaded " + combos.size() + " combos");
                        
                        // Update adapter with real data
                        adapter.setCombos(combos);
                        
                        // Enable button
                        btnContinue.setEnabled(true);
                        btnContinue.setText("Tiếp tục");
                    } else {
                        android.util.Log.e("SelectCombo", "Response or data is null");
                        Toast.makeText(SelectComboActivity.this, 
                            "Không thể tải danh sách combo", Toast.LENGTH_SHORT).show();
                        
                        // Still allow to continue without combos
                        btnContinue.setEnabled(true);
                        btnContinue.setText("Tiếp tục");
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    android.util.Log.e("SelectCombo", "Error loading combos: " + error);
                    Toast.makeText(SelectComboActivity.this, 
                        "Không thể tải combo. Bạn có thể bỏ qua bước này.", Toast.LENGTH_SHORT).show();
                    
                    // Still allow to continue without combos
                    btnContinue.setEnabled(true);
                    btnContinue.setText("Tiếp tục");
                });
            }
        });
    }
    
    /**
     * Add selected combos to booking via API: POST /api/bookings/{id}/add-combos
     */
    private void addCombosToBookingAPI() {
        // Disable button
        btnContinue.setEnabled(false);
        btnContinue.setText("Đang lưu combo...");
        
        // Build request
        List<AddCombosRequest.ComboItem> comboItems = new ArrayList<>();
        Map<Integer, Integer> quantities = adapter.getQuantities();
        
        for (Map.Entry<Integer, Integer> entry : quantities.entrySet()) {
            int comboId = entry.getKey();
            int quantity = entry.getValue();
            
            if (quantity > 0) {
                comboItems.add(new AddCombosRequest.ComboItem(comboId, quantity));
            }
        }
        
        if (comboItems.isEmpty()) {
            // No combo selected - just navigate
            navigateToBookingSummary(new ArrayList<>(), 0);
            return;
        }
        
        AddCombosRequest request = new AddCombosRequest(comboItems);
        
        android.util.Log.d("SelectCombo", "Adding " + comboItems.size() + " combos to booking " + bookingId);
        
        comboRepository.addCombosToBooking(bookingId, request, 
            new ApiCallback<ApiResponse<AddCombosResponse>>() {
            @Override
            public void onSuccess(ApiResponse<AddCombosResponse> response) {
                runOnUiThread(() -> {
                    if (response != null && response.getData() != null) {
                        AddCombosResponse data = response.getData();
                        
                        android.util.Log.d("SelectCombo", "API Response - Combo total: " + data.getComboTotal());
                        android.util.Log.d("SelectCombo", "API Response - Seats total: " + data.getSeatsTotal());
                        android.util.Log.d("SelectCombo", "API Response - Total amount: " + data.getTotalAmount());
                        android.util.Log.d("SelectCombo", "API Response - Combos count: " + (data.getCombos() != null ? data.getCombos().size() : 0));
                        
                        // Build combo data from API response instead of selectedCombos
                        // This ensures we use the actual data saved in booking
                        navigateToBookingSummaryWithApiData(data);
                    } else {
                        btnContinue.setEnabled(true);
                        btnContinue.setText("Tiếp tục");
                        Toast.makeText(SelectComboActivity.this, 
                            "Không thể lưu combo", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    btnContinue.setEnabled(true);
                    btnContinue.setText("Tiếp tục");
                    
                    android.util.Log.e("SelectCombo", "Add combos error: " + error);
                    
                    // User-friendly error messages
                    String message = "Không thể lưu combo";
                    if (error.contains("404")) {
                        message = "Không tìm thấy booking. Vui lòng thử lại.";
                    } else if (error.contains("401")) {
                        message = "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.";
                    } else if (error.contains("409")) {
                        message = "Booking đã bị hủy hoặc đã xác nhận.";
                    }
                    
                    Toast.makeText(SelectComboActivity.this, message, Toast.LENGTH_LONG).show();
                });
            }
        });
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
        
        android.util.Log.d("SelectCombo", "getSelectedCombos - Quantities map size: " + quantities.size());
        
        for (Map.Entry<Integer, Integer> entry : quantities.entrySet()) {
            int comboId = entry.getKey();
            int quantity = entry.getValue();
            
            android.util.Log.d("SelectCombo", "Combo ID: " + comboId + ", Quantity: " + quantity);
            
            if (quantity > 0) {
                Combo combo = findComboById(comboId);
                android.util.Log.d("SelectCombo", "Found combo: " + (combo != null ? combo.getName() : "NULL"));
                
                if (combo != null) {
                    // Add combo multiple times based on quantity
                    for (int i = 0; i < quantity; i++) {
                        selectedCombos.add(combo);
                    }
                }
            }
        }
        
        android.util.Log.d("SelectCombo", "Total selected combos: " + selectedCombos.size());
        
        return selectedCombos;
    }

    private Combo findComboById(int comboId) {
        // Get combo from adapter's list
        return adapter.getComboById(comboId);
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

        android.util.Log.d("SelectCombo", "Navigating to summary. Selected combos: " + selectedCombos.size() + ", Total price: " + totalComboPrice);

        // Build combo data HashMap
        HashMap<String, Integer> comboData = new HashMap<>();
        HashMap<String, String> comboDescriptions = new HashMap<>();
        Map<Integer, Integer> quantities = adapter.getQuantities();
        
        for (Combo combo : selectedCombos) {
            int quantity = quantities.getOrDefault(combo.getComboId(), 0);
            if (quantity > 0) {
                comboData.put(combo.getName(), quantity);
                if (combo.getDescription() != null) {
                    comboDescriptions.put(combo.getName(), combo.getDescription());
                }
                android.util.Log.d("SelectCombo", "Adding combo: " + combo.getName() + " x " + quantity);
            }
        }
        
        android.util.Log.d("SelectCombo", "Combo data size: " + comboData.size() + ", Total combo price: " + totalComboPrice);

        navigateToBookingSummaryIntent(comboData, comboDescriptions, totalComboPrice);
    }
    
    /**
     * Navigate to BookingSummary using data from API response (more reliable)
     */
    private void navigateToBookingSummaryWithApiData(AddCombosResponse apiData) {
        // Stop timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Calculate combo total from combos array since backend doesn't return comboTotal field
        // Backend returns unit price, so we need to multiply by quantity
        double calculatedComboTotal = 0;
        if (apiData.getCombos() != null) {
            for (AddCombosResponse.ComboDetail comboDetail : apiData.getCombos()) {
                double comboTotal = comboDetail.getPrice() * comboDetail.getQuantity();
                calculatedComboTotal += comboTotal;
                android.util.Log.d("SelectCombo", "Combo: " + comboDetail.getName() + 
                    " - Unit: " + comboDetail.getPrice() + " × " + comboDetail.getQuantity() + 
                    " = " + comboTotal + "đ");
            }
        }
        
        android.util.Log.d("SelectCombo", "Navigating with API data. Calculated combo total: " + calculatedComboTotal);
        android.util.Log.d("SelectCombo", "API total amount: " + apiData.getTotalAmount());

        // Build combo data from API response
        HashMap<String, Integer> comboData = new HashMap<>();
        HashMap<String, String> comboDescriptions = new HashMap<>();
        
        if (apiData.getCombos() != null) {
            for (AddCombosResponse.ComboDetail comboDetail : apiData.getCombos()) {
                comboData.put(comboDetail.getName(), comboDetail.getQuantity());
                
                // Get description from adapter's combo list
                Combo combo = adapter.getComboByName(comboDetail.getName());
                if (combo != null && combo.getDescription() != null) {
                    comboDescriptions.put(comboDetail.getName(), combo.getDescription());
                }
                
                android.util.Log.d("SelectCombo", "API Combo: " + comboDetail.getName() + " x " + comboDetail.getQuantity() + " = " + comboDetail.getPrice() + "đ");
            }
        }
        
        android.util.Log.d("SelectCombo", "Combo data from API size: " + comboData.size() + ", Total: " + calculatedComboTotal);

        navigateToBookingSummaryIntent(comboData, comboDescriptions, calculatedComboTotal);
    }
    
    /**
     * Common method to create intent and navigate
     */
    private void navigateToBookingSummaryIntent(HashMap<String, Integer> comboData, HashMap<String, String> comboDescriptions, double totalComboPrice) {
        // Navigate to BookingSummaryActivity
        Intent intent = new Intent(this, com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.class);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_BOOKING_ID, bookingId);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_SHOWTIME_ID, showtimeId);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_MOVIE_TITLE, movieTitle);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_CINEMA_NAME, cinemaName);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_SHOWTIME, showtimeInfo);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_FORMAT, format);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_RATING, movieRating);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_MOVIE_POSTER, moviePosterUrl);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_SEAT_NAMES, selectedSeatsText);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_SEAT_COUNT, seatIds != null ? seatIds.size() : 0);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_SEAT_PRICE, seatTotalPrice);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_COMBO_DATA, comboData);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_COMBO_DESCRIPTIONS, comboDescriptions);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_COMBO_PRICE, totalComboPrice);
        intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_TIMER_REMAINING, timerRemaining);
        
        // Pass applied promotions
        if (appliedPromotions != null && !appliedPromotions.isEmpty()) {
            intent.putExtra(com.example.cinemabookingsystemfe.ui.payment.BookingSummaryActivity.EXTRA_APPLIED_PROMOTIONS, 
                appliedPromotions);
        }
        
        startActivity(intent);
        // Don't finish() - allow user to go back to combo selection
    }

    @Override
    protected void onResume() {
        super.onResume();
        // When user comes back from BookingSummary, timer continues
        // No need to restart timer - it's already running
        android.util.Log.d("SelectCombo", "onResume - Timer remaining: " + timerRemaining);
    }

    @Override
    public void onBackPressed() {
        // Show confirmation dialog before cancelling booking
        showCancelConfirmationDialog();
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
                
                Toast.makeText(SelectComboActivity.this, 
                    "Đã hủy đặt vé thành công", 
                    Toast.LENGTH_SHORT).show();
                
                // Navigate to home screen and clear back stack
                Intent intent = new Intent(SelectComboActivity.this, com.example.cinemabookingsystemfe.ui.main.MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            
            @Override
            public void onError(String error) {
                Toast.makeText(SelectComboActivity.this, 
                    "Lỗi hủy đặt vé: " + error, 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
