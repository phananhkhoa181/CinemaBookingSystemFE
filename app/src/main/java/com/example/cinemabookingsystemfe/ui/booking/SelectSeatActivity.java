package com.example.cinemabookingsystemfe.ui.booking;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.request.CreateBookingRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.AvailableSeatsResponse;
import com.example.cinemabookingsystemfe.data.models.response.CreateBookingResponse;
import com.example.cinemabookingsystemfe.data.models.response.Seat;
import com.example.cinemabookingsystemfe.data.repository.ShowtimeRepository;
import com.example.cinemabookingsystemfe.data.repository.VoucherRepository;
import com.example.cinemabookingsystemfe.utils.TokenManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * SelectSeatActivity - Chọn ghế ngồi trong phòng chiếu
 * Grid: 10 columns x 8 rows (A-H) = 80 seats
 * Seat types: Standard, VIP, Couple
 * Statuses: Available, Occupied, Selected
 */
public class SelectSeatActivity extends AppCompatActivity {

    public static final String EXTRA_SHOWTIME_ID = "showtime_id";
    public static final String EXTRA_AUDITORIUM_ID = "auditorium_id";
    public static final String EXTRA_SHOWTIME_PRICE = "showtime_price";
    public static final String EXTRA_MOVIE_TITLE = "movie_title";
    public static final String EXTRA_CINEMA_NAME = "cinema_name";
    public static final String EXTRA_SHOWTIME_FORMAT = "showtime_format";
    public static final String EXTRA_SHOWTIME_TIME = "showtime_time";
    public static final String EXTRA_SHOWTIME_DATE = "showtime_date";
    public static final String EXTRA_MOVIE_RATING = "movie_rating";
    public static final String EXTRA_MOVIE_POSTER = "movie_poster";
    public static final String EXTRA_BOOKING_ID = "booking_id";

    private MaterialToolbar toolbar;
    private TextView tvMovieTitle, tvShowtimeInfo, tvShowtimeDateTime, tvRating;
    private TextView tvSeatCount, tvTotalAmount;
    private RecyclerView rvSeatMap;
    private MaterialButton btnContinue;

    private static final int REQUEST_CODE_LOGIN = 1001;
    
    private SeatRowAdapter seatRowAdapter;
    private List<Seat> allSeats = new ArrayList<>();
    private List<Seat> selectedSeats = new ArrayList<>();

    private int showtimeId;
    private int auditoriumId;
    private double showtimePrice = 100000; // Default price, will be updated from API
    private String movieTitle, cinemaName, showtimeFormat, showtimeTime, showtimeDate, movieRating, moviePosterUrl;

    private ShowtimeRepository showtimeRepository;
    private TokenManager tokenManager;
    
    private CountDownTimer countDownTimer;
    private static final long COUNTDOWN_DURATION = 15 * 60 * 1000; // 15 minutes
    private long timerRemaining = COUNTDOWN_DURATION;
    
    // Seat selection limits
    private static final int MAX_SEATS_TO_DISPLAY = 500; // Max seats per auditorium (safety limit)
    private static final int MAX_SEATS_TO_SELECT = 10;   // Max seats user can select at once
    
    private boolean isWaitingForLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);

        // Get data from intent
        showtimeId = getIntent().getIntExtra(EXTRA_SHOWTIME_ID, 0);
        auditoriumId = getIntent().getIntExtra(EXTRA_AUDITORIUM_ID, 0); // From API response
        showtimePrice = getIntent().getDoubleExtra(EXTRA_SHOWTIME_PRICE, 100000); // Get real price from intent
        movieTitle = getIntent().getStringExtra(EXTRA_MOVIE_TITLE);
        cinemaName = getIntent().getStringExtra(EXTRA_CINEMA_NAME);
        showtimeFormat = getIntent().getStringExtra(EXTRA_SHOWTIME_FORMAT);
        showtimeTime = getIntent().getStringExtra(EXTRA_SHOWTIME_TIME);
        showtimeDate = getIntent().getStringExtra(EXTRA_SHOWTIME_DATE);
        movieRating = getIntent().getStringExtra(EXTRA_MOVIE_RATING);
        moviePosterUrl = getIntent().getStringExtra(EXTRA_MOVIE_POSTER);
        if (movieRating == null || movieRating.isEmpty()) {
            movieRating = "P"; // Default rating
        }

        // Clear any previous state
        selectedSeats.clear();
        allSeats.clear();

        // Initialize repositories
        showtimeRepository = ShowtimeRepository.getInstance(this);
        tokenManager = TokenManager.getInstance(this);

        initViews();
        setupToolbar();
        displayMovieInfo();
        setupSeatMap();
        
        // Load seats from API
        loadSeatsFromAPI();
        
        setupListeners();
        startCountdownTimer();
        
        // Initialize button state
        updateContinueButton();
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(COUNTDOWN_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerRemaining = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                Toast.makeText(SelectSeatActivity.this, 
                    "Hết thời gian giữ ghế! Vui lòng chọn lại.", 
                    Toast.LENGTH_LONG).show();
                finish();
            }
        };
        countDownTimer.start();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvShowtimeInfo = findViewById(R.id.tvShowtimeInfo);
        tvShowtimeDateTime = findViewById(R.id.tvShowtimeDateTime);
        tvRating = findViewById(R.id.tvRating);
        tvSeatCount = findViewById(R.id.tvSeatCount);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rvSeatMap = findViewById(R.id.rvSeatMap);
        btnContinue = findViewById(R.id.btnContinue);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(cinemaName != null ? cinemaName : "Chọn ghế");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void displayMovieInfo() {
        if (movieTitle != null) {
            tvMovieTitle.setText(movieTitle);
        }

        if (showtimeFormat != null) {
            tvShowtimeInfo.setText(showtimeFormat);
        }

        if (showtimeDate != null && showtimeTime != null) {
            tvShowtimeDateTime.setText(showtimeDate + " - " + showtimeTime);
        }

        // Display rating badge with appropriate color
        displayRatingBadge();
    }

    private void displayRatingBadge() {
        tvRating.setText(movieRating);
        
        // Set background color based on rating
        int backgroundRes;
        switch (movieRating.toUpperCase()) {
            case "P":
                backgroundRes = R.drawable.bg_rating_p; // Green
                break;
            case "C13":
                backgroundRes = R.drawable.bg_rating_c13; // Orange
                break;
            case "C16":
                backgroundRes = R.drawable.bg_rating_c16; // Deep Orange
                break;
            case "C18":
                backgroundRes = R.drawable.bg_rating_c18; // Red
                break;
            default:
                backgroundRes = R.drawable.bg_rating_badge; // Default Orange
                break;
        }
        tvRating.setBackgroundResource(backgroundRes);
    }

    private void setupSeatMap() {
        seatRowAdapter = new SeatRowAdapter((seat, position) -> handleSeatClick(seat, position));

        // Vertical layout - mỗi row là 1 item
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvSeatMap.setLayoutManager(layoutManager);
        rvSeatMap.setAdapter(seatRowAdapter);
    }

    /**
     * Load seats from API: GET /api/auditoriums/{id}/seats?showtimeId={showtimeId}
     */
    private void loadSeatsFromAPI() {
        // Show loading
        btnContinue.setEnabled(false);
        btnContinue.setText("Đang tải ghế...");
        
        android.util.Log.d("SelectSeat", "Loading seats for auditorium: " + auditoriumId + ", showtime: " + showtimeId);
        
        showtimeRepository.getAuditoriumSeats(auditoriumId, showtimeId, 
            new ApiCallback<ApiResponse<com.example.cinemabookingsystemfe.data.models.response.AuditoriumSeatsResponse>>() {
            @Override
            public void onSuccess(ApiResponse<com.example.cinemabookingsystemfe.data.models.response.AuditoriumSeatsResponse> response) {
                runOnUiThread(() -> {
                    android.util.Log.d("SelectSeat", "API Response received: " + (response != null));
                    
                    if (response != null && response.getData() != null) {
                        com.example.cinemabookingsystemfe.data.models.response.AuditoriumSeatsResponse data = response.getData();
                        auditoriumId = data.getAuditoriumId(); // Update with real auditorium ID
                        
                        android.util.Log.d("SelectSeat", "Auditorium: " + data.getName() 
                            + ", Total seats: " + data.getSeatsCount() 
                            + ", Seats list size: " + (data.getSeats() != null ? data.getSeats().size() : "null"));
                        
                        // Process seats from API
                        processSeatsFromAPI(data.getSeats());
                    } else {
                        android.util.Log.e("SelectSeat", "Response or data is null");
                        Toast.makeText(SelectSeatActivity.this, 
                            "Không có thông tin ghế", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    android.util.Log.e("SelectSeat", "Error loading seats: " + error);
                    Toast.makeText(SelectSeatActivity.this, 
                        "Không thể tải thông tin ghế. Lỗi: " + error, Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        });
    }
    
    /**
     * Process seats from API and organize into grid
     */
    private void processSeatsFromAPI(List<Seat> seats) {
        if (seats == null || seats.isEmpty()) {
            Toast.makeText(this, "Phòng chiếu chưa có ghế", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check seat count limit
        if (seats.size() > MAX_SEATS_TO_DISPLAY) {
            Toast.makeText(this, 
                "Phòng chiếu có quá nhiều ghế (" + seats.size() + "). Giới hạn: " + MAX_SEATS_TO_DISPLAY, 
                Toast.LENGTH_LONG).show();
            android.util.Log.e("SelectSeat", "Seat count exceeds limit: " + seats.size() + " > " + MAX_SEATS_TO_DISPLAY);
            finish();
            return;
        }
        
        allSeats.clear();
        
        // Initialize computed fields for each seat
        for (Seat seat : seats) {
            seat.initializeComputedFields();
            seat.setPrice(showtimePrice); // All seats same price from showtime
            allSeats.add(seat);
        }
        
        // Group seats by row
        Map<String, List<Seat>> seatsByRow = new LinkedHashMap<>();
        for (Seat seat : allSeats) {
            String row = seat.getRow();
            if (!seatsByRow.containsKey(row)) {
                seatsByRow.put(row, new ArrayList<>());
            }
            seatsByRow.get(row).add(seat);
        }
        
        // Sort seats in each row by number
        for (List<Seat> rowSeats : seatsByRow.values()) {
            rowSeats.sort((s1, s2) -> Integer.compare(s1.getNumber(), s2.getNumber()));
        }
        
        // Convert to list of rows for adapter
        List<List<Seat>> seatRows = new ArrayList<>(seatsByRow.values());
        seatRowAdapter.setSeatRows(seatRows);
        
        // Enable continue button
        btnContinue.setEnabled(false);
        btnContinue.setText("Tiếp Tục");
        
        android.util.Log.d("SelectSeat", "Organized " + seatRows.size() + " rows of seats");
    }

    private void handleSeatClick(Seat seat, int position) {
        if (!seat.isAvailable()) {
            Toast.makeText(this, "Ghế đã được đặt", Toast.LENGTH_SHORT).show();
            return;
        }

        // Toggle selection
        if (seat.isSelected()) {
            seat.setSelected(false);
            selectedSeats.remove(seat);
        } else {
            // Check max selection limit
            if (selectedSeats.size() >= MAX_SEATS_TO_SELECT) {
                Toast.makeText(this, 
                    "Bạn chỉ có thể chọn tối đa " + MAX_SEATS_TO_SELECT + " ghế", 
                    Toast.LENGTH_SHORT).show();
                return;
            }
            
            seat.setSelected(true);
            selectedSeats.add(seat);
        }

        // Notify adapter to refresh
        seatRowAdapter.notifyDataSetChanged();
        updateContinueButton();
    }

    private void updateContinueButton() {
        if (selectedSeats.isEmpty()) {
            btnContinue.setEnabled(false);
            tvSeatCount.setText("0x ghế:");
            tvTotalAmount.setText("Tổng Cộng: 0đ");
        } else {
            btnContinue.setEnabled(true);
            
            // Build seat list string
            StringBuilder seatNumbers = new StringBuilder();
            for (int i = 0; i < selectedSeats.size(); i++) {
                seatNumbers.append(selectedSeats.get(i).getSeatNumber());
                if (i < selectedSeats.size() - 1) {
                    seatNumbers.append(", ");
                }
            }
            
            // Calculate total (all seats same price from showtime)
            double total = 0;
            for (Seat seat : selectedSeats) {
                total += seat.getPrice();
            }
            
            DecimalFormat formatter = new DecimalFormat("#,###");
            tvSeatCount.setText(selectedSeats.size() + "x ghế: " + seatNumbers.toString());
            tvTotalAmount.setText("Tổng Cộng: " + formatter.format(total) + "đ");
        }
    }

    private void setupListeners() {
        btnContinue.setOnClickListener(v -> {
            if (!selectedSeats.isEmpty()) {
                // Check authentication
                String token = tokenManager.getToken();
                if (token == null || token.isEmpty() || tokenManager.isTokenExpired()) {
                    Toast.makeText(this, "Vui lòng đăng nhập để đặt vé", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(this, com.example.cinemabookingsystemfe.ui.auth.LoginActivity.class);
                    isWaitingForLogin = true;
                    startActivityForResult(loginIntent, REQUEST_CODE_LOGIN);
                    return;
                }
                
                // Create booking via API
                createBookingAPI();
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
            android.util.Log.d("SelectSeat", "Login successful, retrying booking");
            // Re-check token and create booking
            String token = tokenManager.getToken();
            if (token != null && !token.isEmpty() && !tokenManager.isTokenExpired()) {
                isWaitingForLogin = false;
                createBookingAPI();
            } else {
                Toast.makeText(this, "Đăng nhập thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_CANCELED) {
            android.util.Log.d("SelectSeat", "Login cancelled by user");
            isWaitingForLogin = false;
            Toast.makeText(this, "Đã hủy đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Create booking via POST /api/bookings/create
     */
    private void createBookingAPI() {
        // Disable button
        btnContinue.setEnabled(false);
        btnContinue.setText("Đang tạo đơn...");
        
        // Collect seat IDs
        List<Integer> seatIds = new ArrayList<>();
        for (Seat seat : selectedSeats) {
            seatIds.add(seat.getSeatId());
        }
        
        // Create request
        CreateBookingRequest request = new CreateBookingRequest(showtimeId, seatIds);
        
        showtimeRepository.createBooking(request, new ApiCallback<ApiResponse<CreateBookingResponse>>() {
            @Override
            public void onSuccess(ApiResponse<CreateBookingResponse> response) {
                runOnUiThread(() -> {
                    if (response != null && response.getData() != null) {
                        CreateBookingResponse booking = response.getData();
                        
                        android.util.Log.d("SelectSeat", "Booking created successfully!");
                        android.util.Log.d("SelectSeat", "Booking ID: " + booking.getBookingId());
                        android.util.Log.d("SelectSeat", "Status: " + booking.getStatus());
                        android.util.Log.d("SelectSeat", "Total Amount: " + booking.getTotalAmount());
                        android.util.Log.d("SelectSeat", "Seats count: " + (booking.getSeats() != null ? booking.getSeats().size() : 0));
                        
                        // Stop timer
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        
                        // Navigate to SelectComboActivity
                        navigateToComboSelection(booking);
                    } else {
                        btnContinue.setEnabled(true);
                        btnContinue.setText("Tiếp Tục");
                        Toast.makeText(SelectSeatActivity.this, 
                            "Không thể tạo đơn đặt vé", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    btnContinue.setEnabled(true);
                    btnContinue.setText("Tiếp Tục");
                    
                    android.util.Log.e("SelectSeat", "Create booking error: " + error);
                    
                    // User-friendly error messages
                    String message = "Không thể đặt ghế";
                    if (error.contains("409")) {
                        message = "Ghế đã được người khác đặt. Vui lòng chọn ghế khác.";
                        // Reload seats
                        loadSeatsFromAPI();
                    } else if (error.contains("401")) {
                        message = "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.";
                    }
                    
                    Toast.makeText(SelectSeatActivity.this, message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    /**
     * Navigate to combo selection with booking data
     */
    private void navigateToComboSelection(CreateBookingResponse booking) {
        // Calculate total seat price
        double totalSeatPrice = 0;
        for (Seat seat : selectedSeats) {
            totalSeatPrice += seat.getPrice();
        }
        
        // Build seat names string
        ArrayList<Integer> seatIdList = new ArrayList<>();
        StringBuilder seatNames = new StringBuilder();
        for (int i = 0; i < selectedSeats.size(); i++) {
            Seat seat = selectedSeats.get(i);
            seatIdList.add(seat.getSeatId());
            seatNames.append(seat.getSeatNumber());
            if (i < selectedSeats.size() - 1) {
                seatNames.append(", ");
            }
        }
        
        Intent intent = new Intent(this, SelectComboActivity.class);
        intent.putExtra(SelectComboActivity.EXTRA_SHOWTIME_ID, showtimeId);
        intent.putExtra(SelectComboActivity.EXTRA_BOOKING_ID, booking.getBookingId());
        intent.putExtra(SelectComboActivity.EXTRA_SEAT_IDS, seatIdList);
        intent.putExtra(SelectComboActivity.EXTRA_SEAT_NAMES, seatNames.toString());
        intent.putExtra(SelectComboActivity.EXTRA_SEAT_TOTAL_PRICE, totalSeatPrice);
        intent.putExtra(SelectComboActivity.EXTRA_MOVIE_TITLE, movieTitle);
        intent.putExtra(SelectComboActivity.EXTRA_CINEMA_NAME, cinemaName);
        intent.putExtra(SelectComboActivity.EXTRA_SHOWTIME_INFO, showtimeDate + " - " + showtimeTime);
        intent.putExtra(SelectComboActivity.EXTRA_FORMAT, showtimeFormat);
        intent.putExtra(SelectComboActivity.EXTRA_MOVIE_RATING, movieRating);
        intent.putExtra(SelectComboActivity.EXTRA_MOVIE_POSTER, moviePosterUrl);
        intent.putExtra(SelectComboActivity.EXTRA_TIMER_REMAINING, timerRemaining);
        
        startActivity(intent);
        // Don't finish() - allow user to go back if needed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
