package com.example.cinemabookingsystemfe.ui.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.adapters.SpinnerAdapter;
import com.example.cinemabookingsystemfe.data.models.response.*;
import com.example.cinemabookingsystemfe.data.repository.MovieRepository;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

/**
 * SelectCinemaActivity - Chọn rạp chiếu và suất chiếu cho phim
 */
public class SelectCinemaActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "movie_id";
    public static final String EXTRA_SHOWTIME_ID = "showtime_id";

    private MaterialToolbar toolbar;
    private Spinner spinnerCity;
    private Spinner spinnerCinema;
    private LinearLayout chipGroupDates;
    private TextView tvSelectedDate;
    private RecyclerView rvCinemas;
    private ProgressBar progressBar;
    private View layoutEmptyState;

    private int movieId;
    private String movieTitle;
    private String movieAgeRating;
    private String moviePosterUrl;
    private String selectedDate;
    private String initialApiDate; // Store initial date to load after API returns
    private SelectCinemaAdapter adapter;
    
    private MovieRepository movieRepository;
    private MovieShowtimesResponse showtimesData;
    private List<ShowtimesByDate.ShowtimesByCinema> allCinemas = new ArrayList<>();
    private Map<String, List<ShowtimesByDate.ShowtimesByCinema>> showtimesByDateMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            android.util.Log.d("SelectCinema", "onCreate started");
            setContentView(R.layout.activity_select_cinema);
            
            // Get movie info from intent
            movieId = getIntent().getIntExtra("movie_id", 0);
            movieTitle = getIntent().getStringExtra("movie_title");
            movieAgeRating = getIntent().getStringExtra("movie_age_rating");
            moviePosterUrl = getIntent().getStringExtra("movie_poster");
            
            if (movieTitle == null) movieTitle = "Phim";
            if (movieAgeRating == null) movieAgeRating = "P";
            
            android.util.Log.d("SelectCinema", "Movie ID: " + movieId + ", Title: " + movieTitle + ", Rating: " + movieAgeRating);
            
            // Initialize repository
            movieRepository = MovieRepository.getInstance(this);
            
            initViews();
            android.util.Log.d("SelectCinema", "Views initialized");
            
            setupToolbar();
            android.util.Log.d("SelectCinema", "Toolbar setup complete");
            
            setupSpinners();
            android.util.Log.d("SelectCinema", "Spinners setup complete");
            
            setupDateChips();
            android.util.Log.d("SelectCinema", "Date chips setup complete");
            
            setupRecyclerView();
            android.util.Log.d("SelectCinema", "RecyclerView setup complete");
            
            // Load showtimes from API
            loadShowtimesFromAPI();
            
        } catch (Exception e) {
            android.util.Log.e("SelectCinema", "ERROR in onCreate: " + e.getMessage(), e);
            e.printStackTrace();
            android.widget.Toast.makeText(this, 
                "Lỗi khởi tạo: " + e.getMessage(), 
                android.widget.Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerCinema = findViewById(R.id.spinnerCinema);
        chipGroupDates = findViewById(R.id.chipGroupDates);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        rvCinemas = findViewById(R.id.rvCinemas);
        progressBar = findViewById(R.id.progressBar);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (layoutEmptyState != null) {
            layoutEmptyState.setVisibility(View.GONE);
        }
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Set movie title as toolbar title
            if (movieTitle != null && !movieTitle.isEmpty()) {
                getSupportActionBar().setTitle(movieTitle);
            }
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    
    private void setupSpinners() {
        try {
            // City spinner - chỉ cho phép TP Hồ Chí Minh
            List<String> cities = Arrays.asList("TP Hồ Chí Minh");
            SpinnerAdapter cityAdapter = new SpinnerAdapter(this, cities);
            spinnerCity.setAdapter(cityAdapter);
            android.util.Log.d("SelectCinema", "City spinner setup OK");
        
        // Cinema spinner will be populated after API call
        // Initially show "Tất cả chi nhánh"
        List<String> initialCinemas = Arrays.asList("Tất cả chi nhánh");
        SpinnerAdapter cinemaAdapter = new SpinnerAdapter(this, initialCinemas);
        spinnerCinema.setAdapter(cinemaAdapter);
        android.util.Log.d("SelectCinema", "Cinema spinner setup OK");
        
        // Add listener to filter cinemas by selected cinema
        spinnerCinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    filterShowtimesByCinema(position);
                } catch (Exception e) {
                    android.util.Log.e("SelectCinema", "Error in filter: " + e.getMessage(), e);
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
            
        } catch (Exception e) {
            android.util.Log.e("SelectCinema", "Error in setupSpinners: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Load showtimes from API for the selected movie
     */
    private void loadShowtimesFromAPI() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        
        movieRepository.getMovieShowtimes(movieId, null, null, new ApiCallback<ApiResponse<MovieShowtimesResponse>>() {
            @Override
            public void onSuccess(ApiResponse<MovieShowtimesResponse> response) {
                runOnUiThread(() -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    
                    if (response != null && response.getData() != null) {
                        showtimesData = response.getData();
                        processShowtimesData();
                        showContent();
                    } else {
                        showEmptyState();
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    
                    // Log for debugging only
                    android.util.Log.d("SelectCinema", "Showtimes not available: " + error);
                    
                    // Always show empty state, no toast
                    showEmptyState();
                });
            }
        });
    }
    
    /**
     * Process showtimes data from API and populate UI
     */
    private void processShowtimesData() {
        if (showtimesData == null || showtimesData.getShowtimesByDate() == null) {
            return;
        }
        
        // Group showtimes by date
        showtimesByDateMap.clear();
        allCinemas.clear();
        List<String> cinemaNames = new ArrayList<>();
        cinemaNames.add("Tất cả chi nhánh");
        
        for (ShowtimesByDate dateGroup : showtimesData.getShowtimesByDate()) {
            String date = dateGroup.getDate();
            List<ShowtimesByDate.ShowtimesByCinema> cinemas = dateGroup.getCinemas();
            
            showtimesByDateMap.put(date, cinemas);
            
            // Collect unique cinema names for spinner
            for (ShowtimesByDate.ShowtimesByCinema cinema : cinemas) {
                if (!cinemaNames.contains(cinema.getName())) {
                    cinemaNames.add(cinema.getName());
                }
            }
        }
        
        // Update cinema spinner with real data
        SpinnerAdapter cinemaAdapter = new SpinnerAdapter(this, cinemaNames);
        spinnerCinema.setAdapter(cinemaAdapter);
        
        // Load showtimes for initially selected date (today)
        if (initialApiDate != null) {
            updateShowtimesForDate(initialApiDate);
        }
    }
    
    /**
     * Update showtimes display for a specific date
     */
    private void updateShowtimesForDate(String date) {
        android.util.Log.d("SelectCinema", "Updating showtimes for date: " + date);
        
        List<ShowtimesByDate.ShowtimesByCinema> cinemas = showtimesByDateMap.get(date);
        
        if (cinemas != null && !cinemas.isEmpty()) {
            // Date has showtimes - show them
            allCinemas.clear();
            allCinemas.addAll(cinemas);
            
            // Apply cinema filter if any cinema is selected
            int selectedCinemaIndex = spinnerCinema.getSelectedItemPosition();
            filterShowtimesByCinema(selectedCinemaIndex);
            
            showContent();
        } else {
            // Date has no showtimes - show empty state
            android.util.Log.d("SelectCinema", "No showtimes for date: " + date);
            allCinemas.clear();
            adapter.updateData(new ArrayList<>());
            showEmptyState();
        }
    }
    
    /**
     * Filter showtimes by selected cinema from spinner
     */
    private void filterShowtimesByCinema(int cinemaIndex) {
        if (allCinemas == null || allCinemas.isEmpty()) {
            return;
        }
        
        List<ShowtimesByDate.ShowtimesByCinema> filtered = new ArrayList<>();
        
        if (cinemaIndex == 0) {
            // "Tất cả chi nhánh" - show all
            filtered.addAll(allCinemas);
        } else {
            // Get selected cinema name
            String selectedCinemaName = (String) spinnerCinema.getItemAtPosition(cinemaIndex);
            
            // Filter by cinema name
            for (ShowtimesByDate.ShowtimesByCinema cinema : allCinemas) {
                if (cinema.getName().equals(selectedCinemaName)) {
                    filtered.add(cinema);
                }
            }
        }
        
        adapter.updateData(filtered);
    }
    
    private void setupDateChips() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEEE dd, 'tháng' MM yyyy", new Locale("vi", "VN"));
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        
        // Generate next 4 days
        for (int i = 0; i < 4; i++) {
            final Calendar chipCalendar = (Calendar) calendar.clone();
            final String chipDate = dateFormat.format(chipCalendar.getTime());
            final String fullDate = fullDateFormat.format(chipCalendar.getTime());
            final String apiDate = apiDateFormat.format(chipCalendar.getTime()); // For API matching
            
            // Inflate custom date item layout
            View dateView = getLayoutInflater().inflate(R.layout.item_date_chip, chipGroupDates, false);
            com.google.android.material.card.MaterialCardView cardDate = dateView.findViewById(R.id.cardDate);
            TextView tvDayName = dateView.findViewById(R.id.tvDayName);
            TextView tvDate = dateView.findViewById(R.id.tvDate);
            
            // Set day name
            String dayName;
            if (i == 0) {
                dayName = "Hôm nay";
            } else {
                dayName = new SimpleDateFormat("EEEE", new Locale("vi", "VN")).format(chipCalendar.getTime());
            }
            tvDayName.setText(dayName);
            tvDate.setText(chipDate);
            
            // Set initial state
            final boolean isFirst = (i == 0);
            if (isFirst) {
                cardDate.setCardBackgroundColor(getColor(android.R.color.holo_blue_dark));
                tvDayName.setTextColor(getColor(R.color.white));
                tvDate.setTextColor(getColor(R.color.white));
                selectedDate = chipDate;
                tvSelectedDate.setText(fullDate);
                initialApiDate = apiDate; // Save for loading after API returns
            }
            
            // Click listener
            dateView.setOnClickListener(v -> {
                // Reset all other date items
                for (int j = 0; j < chipGroupDates.getChildCount(); j++) {
                    View child = chipGroupDates.getChildAt(j);
                    com.google.android.material.card.MaterialCardView card = child.findViewById(R.id.cardDate);
                    TextView dayNameView = child.findViewById(R.id.tvDayName);
                    TextView dateTextView = child.findViewById(R.id.tvDate);
                    
                    card.setCardBackgroundColor(getColor(R.color.white));
                    dayNameView.setTextColor(getColor(R.color.black));
                    dateTextView.setTextColor(getColor(R.color.black));
                }
                
                // Set selected state
                cardDate.setCardBackgroundColor(getColor(android.R.color.holo_blue_dark));
                tvDayName.setTextColor(getColor(R.color.white));
                tvDate.setTextColor(getColor(R.color.white));
                selectedDate = chipDate;
                tvSelectedDate.setText(fullDate);
                
                // Filter showtimes by selected date using API format
                updateShowtimesForDate(apiDate);
            });
            
            chipGroupDates.addView(dateView);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    
    private void setupRecyclerView() {
        adapter = new SelectCinemaAdapter(allCinemas, this::onShowtimeClick);
        rvCinemas.setLayoutManager(new LinearLayoutManager(this));
        rvCinemas.setAdapter(adapter);
    }
    
    /**
     * Handle showtime item click
     */
    private void onShowtimeClick(ShowtimesByDate.ShowtimeItem showtime, ShowtimesByDate.ShowtimesByCinema cinema) {
        // Check if user is logged in before proceeding to seat selection
        com.example.cinemabookingsystemfe.utils.TokenManager tokenManager = 
            com.example.cinemabookingsystemfe.utils.TokenManager.getInstance(this);
        
        String token = tokenManager.getToken();
        if (token == null || token.isEmpty() || tokenManager.isTokenExpired()) {
            // User not logged in -> redirect to LoginActivity
            Toast.makeText(this, "Vui lòng đăng nhập để đặt vé", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, 
                    com.example.cinemabookingsystemfe.ui.auth.LoginActivity.class);
            startActivity(loginIntent);
            return;
        }
        
        // User is logged in -> proceed to seat selection
        Intent intent = new Intent(this, SelectSeatActivity.class);
        intent.putExtra(SelectSeatActivity.EXTRA_SHOWTIME_ID, showtime.getShowtimeId());
        
        // ✅ Use real auditoriumId from API response (Backend now returns this field)
        intent.putExtra(SelectSeatActivity.EXTRA_AUDITORIUM_ID, showtime.getAuditoriumId());
        
        intent.putExtra(SelectSeatActivity.EXTRA_SHOWTIME_PRICE, showtime.getPrice());
        intent.putExtra(SelectSeatActivity.EXTRA_MOVIE_TITLE, movieTitle);
        intent.putExtra(SelectSeatActivity.EXTRA_CINEMA_NAME, cinema.getName());
        intent.putExtra(SelectSeatActivity.EXTRA_SHOWTIME_FORMAT, showtime.getFormat() + " " + showtime.getLanguageType());
        intent.putExtra(SelectSeatActivity.EXTRA_SHOWTIME_TIME, showtime.getStartTime());
        intent.putExtra(SelectSeatActivity.EXTRA_MOVIE_RATING, movieAgeRating);
        intent.putExtra(SelectSeatActivity.EXTRA_MOVIE_POSTER, moviePosterUrl);
        
        // Get selected date from chip
        String selectedDateStr = tvSelectedDate.getText().toString();
        intent.putExtra(SelectSeatActivity.EXTRA_SHOWTIME_DATE, selectedDateStr);
        
        startActivity(intent);
    }
    
    /**
     * Show empty state when no showtimes available
     */
    private void showEmptyState() {
        if (layoutEmptyState != null) {
            layoutEmptyState.setVisibility(View.VISIBLE);
        }
        if (rvCinemas != null) {
            rvCinemas.setVisibility(View.GONE);
        }
    }
    
    /**
     * Show content when showtimes are available
     */
    private void showContent() {
        if (layoutEmptyState != null) {
            layoutEmptyState.setVisibility(View.GONE);
        }
        if (rvCinemas != null) {
            rvCinemas.setVisibility(View.VISIBLE);
        }
    }
}
