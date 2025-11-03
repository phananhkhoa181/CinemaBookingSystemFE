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
import com.example.cinemabookingsystemfe.data.models.response.Seat;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * SelectSeatActivity - Chọn ghế ngồi trong phòng chiếu
 * Grid: 10 columns x 8 rows (A-H) = 80 seats
 * Seat types: Standard, VIP, Couple
 * Statuses: Available, Occupied, Selected
 */
public class SelectSeatActivity extends AppCompatActivity {

    public static final String EXTRA_SHOWTIME_ID = "showtime_id";
    public static final String EXTRA_MOVIE_TITLE = "movie_title";
    public static final String EXTRA_CINEMA_NAME = "cinema_name";
    public static final String EXTRA_SHOWTIME_FORMAT = "showtime_format";
    public static final String EXTRA_SHOWTIME_TIME = "showtime_time";
    public static final String EXTRA_SHOWTIME_DATE = "showtime_date";
    public static final String EXTRA_MOVIE_RATING = "movie_rating";
    public static final String EXTRA_BOOKING_ID = "booking_id";

    private MaterialToolbar toolbar;
    private TextView tvMovieTitle, tvShowtimeInfo, tvShowtimeDateTime, tvRating;
    private TextView tvSeatCount, tvTotalAmount;
    private RecyclerView rvSeatMap;
    private MaterialButton btnContinue;

    private SeatRowAdapter seatRowAdapter;
    private List<Seat> allSeats = new ArrayList<>();
    private List<Seat> selectedSeats = new ArrayList<>();

    private int showtimeId;
    private String movieTitle, cinemaName, showtimeFormat, showtimeTime, showtimeDate, movieRating;

    private CountDownTimer countDownTimer;
    private static final long COUNTDOWN_DURATION = 15 * 60 * 1000; // 15 minutes
    private long timerRemaining = COUNTDOWN_DURATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);

        // Get data from intent
        showtimeId = getIntent().getIntExtra(EXTRA_SHOWTIME_ID, 0);
        movieTitle = getIntent().getStringExtra(EXTRA_MOVIE_TITLE);
        cinemaName = getIntent().getStringExtra(EXTRA_CINEMA_NAME);
        showtimeFormat = getIntent().getStringExtra(EXTRA_SHOWTIME_FORMAT);
        showtimeTime = getIntent().getStringExtra(EXTRA_SHOWTIME_TIME);
        showtimeDate = getIntent().getStringExtra(EXTRA_SHOWTIME_DATE);
        movieRating = getIntent().getStringExtra(EXTRA_MOVIE_RATING);
        if (movieRating == null || movieRating.isEmpty()) {
            movieRating = "P"; // Default rating
        }

        // Clear any previous state
        selectedSeats.clear();
        allSeats.clear();

        initViews();
        setupToolbar();
        displayMovieInfo();
        setupSeatMap();
        generateMockSeats();
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

    private void generateMockSeats() {
        // Grid: 8 rows (A-H) x 10 columns = 80 seats
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H"};
        int seatIdCounter = 1;
        List<List<Seat>> seatRows = new ArrayList<>();

        for (String row : rows) {
            List<Seat> rowSeats = new ArrayList<>();
            for (int col = 1; col <= 10; col++) {
                String seatNumber = row + col;
                String seatType;
                String status = "AVAILABLE";
                double price;

                // Define seat types
                if (row.equals("D") || row.equals("E")) {
                    // Row D and E are VIP
                    seatType = "VIP";
                    price = 100000;
                } else if (row.equals("H") && (col == 4 || col == 5 || col == 6 || col == 7)) {
                    // Row H columns 4-7 are Couple seats (changed from G to H)
                    seatType = "COUPLE";
                    price = 150000;
                } else {
                    // Standard seats
                    seatType = "STANDARD";
                    price = 70000;
                }

                // Mock some occupied seats
                if ((row.equals("C") && (col == 5 || col == 6)) ||
                    (row.equals("E") && col == 3) ||
                    (row.equals("F") && (col == 7 || col == 8))) {
                    status = "OCCUPIED";
                }

                Seat seat = new Seat(seatIdCounter++, seatNumber, row, col, seatType, status, price);
                allSeats.add(seat);
                rowSeats.add(seat);
            }
            seatRows.add(rowSeats);
        }

        seatRowAdapter.setSeatRows(seatRows);
    }

    private void handleSeatClick(Seat seat, int position) {
        if ("OCCUPIED".equals(seat.getStatus())) {
            Toast.makeText(this, "Ghế đã được đặt", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if it's a couple seat
        if ("COUPLE".equalsIgnoreCase(seat.getType())) {
            // Find the paired seat (4-5 or 6-7)
            Seat pairedSeat = findPairedCoupleSeat(seat);
            
            if (pairedSeat != null && "OCCUPIED".equals(pairedSeat.getStatus())) {
                Toast.makeText(this, "Ghế đôi đã có người đặt", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Toggle both seats together
            if (seat.isSelected()) {
                seat.setSelected(false);
                selectedSeats.remove(seat);
                if (pairedSeat != null) {
                    pairedSeat.setSelected(false);
                    selectedSeats.remove(pairedSeat);
                }
            } else {
                seat.setSelected(true);
                selectedSeats.add(seat);
                if (pairedSeat != null) {
                    pairedSeat.setSelected(true);
                    selectedSeats.add(pairedSeat);
                }
            }
        } else {
            // Normal seat - toggle selection
            if (seat.isSelected()) {
                seat.setSelected(false);
                selectedSeats.remove(seat);
            } else {
                seat.setSelected(true);
                selectedSeats.add(seat);
            }
        }

        // Notify adapter to refresh all seats
        seatRowAdapter.notifyDataSetChanged();
        updateContinueButton();
    }

    private Seat findPairedCoupleSeat(Seat seat) {
        // Couple seats are in pairs: (4,5) and (6,7)
        int targetCol;
        if (seat.getColumn() == 4) targetCol = 5;
        else if (seat.getColumn() == 5) targetCol = 4;
        else if (seat.getColumn() == 6) targetCol = 7;
        else if (seat.getColumn() == 7) targetCol = 6;
        else return null;
        
        // Find seat with same row and targetCol
        for (Seat s : allSeats) {
            if (s.getRow().equals(seat.getRow()) && s.getColumn() == targetCol) {
                return s;
            }
        }
        return null;
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
            
            // Calculate total - for couple seats, only count price once per pair
            double total = 0;
            List<Seat> processedSeats = new ArrayList<>();
            
            for (Seat seat : selectedSeats) {
                if (processedSeats.contains(seat)) continue;
                
                if ("COUPLE".equalsIgnoreCase(seat.getType())) {
                    // For couple seat, find its pair and mark both as processed
                    Seat pairedSeat = findPairedCoupleSeat(seat);
                    total += seat.getPrice(); // Only add price once for the pair
                    processedSeats.add(seat);
                    if (pairedSeat != null) {
                        processedSeats.add(pairedSeat);
                    }
                } else {
                    total += seat.getPrice();
                    processedSeats.add(seat);
                }
            }
            
            DecimalFormat formatter = new DecimalFormat("#,###");
            tvSeatCount.setText(selectedSeats.size() + "x ghế: " + seatNumbers.toString());
            tvTotalAmount.setText("Tổng Cộng: " + formatter.format(total) + "đ");
        }
    }

    private void setupListeners() {
        btnContinue.setOnClickListener(v -> {
            if (!selectedSeats.isEmpty()) {
                // Stop timer before navigating
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                // Calculate total seat price
                double totalSeatPrice = 0;
                List<Seat> processedSeats = new ArrayList<>();
                for (Seat seat : selectedSeats) {
                    if (processedSeats.contains(seat)) continue;
                    
                    if ("COUPLE".equalsIgnoreCase(seat.getType())) {
                        Seat pairedSeat = findPairedCoupleSeat(seat);
                        totalSeatPrice += seat.getPrice();
                        processedSeats.add(seat);
                        if (pairedSeat != null) {
                            processedSeats.add(pairedSeat);
                        }
                    } else {
                        totalSeatPrice += seat.getPrice();
                        processedSeats.add(seat);
                    }
                }

                // Navigate to SelectComboActivity
                Intent intent = new Intent(this, SelectComboActivity.class);
                intent.putExtra(SelectComboActivity.EXTRA_SHOWTIME_ID, showtimeId);
                intent.putExtra(SelectComboActivity.EXTRA_BOOKING_ID, 0); // TODO: Get from booking API
                
                // Pass selected seat IDs
                ArrayList<Integer> seatIdList = new ArrayList<>();
                StringBuilder seatNames = new StringBuilder();
                for (int i = 0; i < selectedSeats.size(); i++) {
                    Seat seat = selectedSeats.get(i);
                    seatIdList.add(seat.getSeatId());
                    
                    // Build seat names string (e.g., "F8, G9, H10")
                    seatNames.append(seat.getRow()).append(seat.getColumn());
                    if (i < selectedSeats.size() - 1) {
                        seatNames.append(", ");
                    }
                }
                intent.putExtra(SelectComboActivity.EXTRA_SEAT_IDS, seatIdList);
                intent.putExtra(SelectComboActivity.EXTRA_SEAT_NAMES, seatNames.toString());
                intent.putExtra(SelectComboActivity.EXTRA_SEAT_TOTAL_PRICE, totalSeatPrice);
                
                // Pass movie and cinema info
                intent.putExtra(SelectComboActivity.EXTRA_MOVIE_TITLE, movieTitle);
                intent.putExtra(SelectComboActivity.EXTRA_CINEMA_NAME, cinemaName);
                intent.putExtra(SelectComboActivity.EXTRA_SHOWTIME_INFO, showtimeDate + " - " + showtimeTime);
                intent.putExtra(SelectComboActivity.EXTRA_FORMAT, showtimeFormat);
                intent.putExtra(SelectComboActivity.EXTRA_MOVIE_RATING, movieRating);
                
                // Pass remaining timer
                intent.putExtra(SelectComboActivity.EXTRA_TIMER_REMAINING, timerRemaining);
                
                startActivity(intent);
                // Don't finish() here - allow user to go back if needed
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
