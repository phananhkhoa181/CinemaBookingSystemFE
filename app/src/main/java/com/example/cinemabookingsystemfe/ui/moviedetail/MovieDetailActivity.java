package com.example.cinemabookingsystemfe.ui.moviedetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.MovieDetail;
import com.example.cinemabookingsystemfe.data.repository.MovieRepository;
import com.example.cinemabookingsystemfe.ui.moviedetail.adapter.MovieDetailPagerAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView ivBackdrop, ivPoster, ivRatingStar;
    private TextView tvTitle, tvRating, tvDuration, tvAgeRating, tvReleaseDate;
    private MaterialButton btnBookNow;
    private MaterialCardView btnPlayTrailer;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private MovieRepository movieRepository;
    private MovieDetail currentMovie;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Mapping UI
        progressBar = findViewById(R.id.progressBar);
        ivBackdrop = findViewById(R.id.ivBackdrop);
        ivPoster = findViewById(R.id.ivPoster);
        ivRatingStar = findViewById(R.id.ivRatingStar);
        tvTitle = findViewById(R.id.tvTitle);
        tvRating = findViewById(R.id.tvRating);
        tvDuration = findViewById(R.id.tvDuration);
        tvAgeRating = findViewById(R.id.tvAgeRating);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        btnBookNow = findViewById(R.id.btnBookNow);
        btnPlayTrailer = findViewById(R.id.btnPlayTrailer);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Initialize MovieRepository
        movieRepository = MovieRepository.getInstance(this);

        movieId = getIntent().getIntExtra("movieId", 1);
        loadMovieDetail(movieId);

        btnBookNow.setOnClickListener(v -> doBooking());
        btnPlayTrailer.setOnClickListener(v -> doWatchTrailer());
    }

    private void loadMovieDetail(int movieId) {
        progressBar.setVisibility(View.VISIBLE);
        movieRepository.getMovieDetail(movieId, new ApiCallback<ApiResponse<MovieDetail>>() {
            @Override
            public void onSuccess(ApiResponse<MovieDetail> response) {
                progressBar.setVisibility(View.GONE);
                if (response != null && response.getData() != null) {
                    MovieDetail movie = response.getData();
                    currentMovie = movie;

                    // Load poster and backdrop
                    Glide.with(MovieDetailActivity.this).load(movie.getPosterUrl()).into(ivBackdrop);
                    Glide.with(MovieDetailActivity.this).load(movie.getPosterUrl()).into(ivPoster);
                    
                    tvTitle.setText(movie.getTitle());
                    
                    // Display rating from API
                    if (movie.getAverageRating() != null && movie.getTotalReviews() > 0) {
                        tvRating.setText(String.format(Locale.getDefault(), "%.1f", movie.getAverageRating()));
                        tvRating.setTextColor(getResources().getColor(R.color.textPrimary));
                        ivRatingStar.setVisibility(View.VISIBLE);
                    } else {
                        tvRating.setText("Chưa có đánh giá");
                        tvRating.setTextColor(getResources().getColor(R.color.textSecondary));
                        ivRatingStar.setVisibility(View.GONE);
                    }
                    
                    // Extract year from releaseDate (yyyy-MM-dd)
                    String releaseDate = movie.getReleaseDate() != null && movie.getReleaseDate().length() >= 10
                            ? formatReleaseDate(movie.getReleaseDate()) : "";
                    tvReleaseDate.setText(releaseDate);
                    
                    tvDuration.setText(movie.getDurationMinutes() + " Phút");
                    tvAgeRating.setText(movie.getRating()); // Age rating like "P", "T13", "T16", "T18"
                    
                    // Check if movie is coming soon based on release date
                    boolean isComingSoon = isMovieComingSoon(movie.getReleaseDate());
                    
                    if (isComingSoon) {
                        // Coming soon - disable booking
                        btnBookNow.setEnabled(false);
                        btnBookNow.setText("Sắp chiếu");
                        btnBookNow.setAlpha(0.6f);
                    } else if (movie.getTotalShowtimes() > 0) {
                        // Now showing with showtimes - enable booking
                        btnBookNow.setEnabled(true);
                        btnBookNow.setText("Đặt vé ngay");
                        btnBookNow.setAlpha(1.0f);
                    } else {
                        // Now showing but no showtimes - disable booking
                        btnBookNow.setEnabled(false);
                        btnBookNow.setText("Chưa có suất chiếu");
                        btnBookNow.setAlpha(0.5f);
                    }
                    
                    // Setup ViewPager with tabs (pass isComingSoon flag)
                    setupViewPager(movie, movieId, isComingSoon);
                } else {
                    Toast.makeText(MovieDetailActivity.this,
                            "Không thể tải thông tin phim", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MovieDetailActivity.this,
                        "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Check if movie is coming soon based on release date
     * @param releaseDate Release date in format "yyyy-MM-dd"
     * @return true if movie is coming soon (release date is in the future)
     */
    private boolean isMovieComingSoon(String releaseDate) {
        if (releaseDate == null || releaseDate.isEmpty()) {
            return false; // If no release date, assume it's available
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date movieReleaseDate = sdf.parse(releaseDate);
            
            // Get current date (set time to 00:00:00 for date-only comparison)
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            
            // Compare dates
            return movieReleaseDate != null && movieReleaseDate.after(today.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return false; // If parsing fails, assume it's available
        }
    }
    
    private String formatReleaseDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return outputFormat.format(inputFormat.parse(dateStr));
        } catch (Exception e) {
            return dateStr;
        }
    }
    
    private void setupViewPager(MovieDetail movie, int movieId, boolean isComingSoon) {
        MovieDetailPagerAdapter adapter = new MovieDetailPagerAdapter(this, movie, movieId, isComingSoon);
        viewPager.setAdapter(adapter);
        
        // Disable swipe to improve scrolling
        viewPager.setUserInputEnabled(false);
        
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Thông tin");
                    break;
                case 1:
                    tab.setText("Đánh giá");
                    break;
            }
        }).attach();
    }

    private void doBooking() {
        if (currentMovie == null) {
            Toast.makeText(this, "Vui lòng đợi thông tin phim được tải...", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check if movie is coming soon based on release date
        if (isMovieComingSoon(currentMovie.getReleaseDate())) {
            Toast.makeText(this, "Phim sắp chiếu, chưa thể đặt vé", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Allow user to browse showtimes without login
        // Login check will be done when user selects a specific showtime
        Intent intent = new Intent(MovieDetailActivity.this, 
                com.example.cinemabookingsystemfe.ui.booking.SelectCinemaActivity.class);
        intent.putExtra("movie_id", currentMovie.getMovieId());
        intent.putExtra("movie_title", currentMovie.getTitle());
        intent.putExtra("movie_poster", currentMovie.getPosterUrl());
        intent.putExtra("movie_duration", currentMovie.getDurationMinutes());
        intent.putExtra("movie_genre", currentMovie.getGenre());
        intent.putExtra("movie_age_rating", currentMovie.getRating());
        startActivity(intent);
    }

    private void doWatchTrailer() {
        if (currentMovie != null && currentMovie.getTrailerUrl() != null && !currentMovie.getTrailerUrl().isEmpty()) {
            Intent intent = new Intent(MovieDetailActivity.this, MovieTrailerActivity.class);
            intent.putExtra("trailerUrl", currentMovie.getTrailerUrl());
            startActivity(intent);
        } else {
            Toast.makeText(MovieDetailActivity.this, "Trailer không khả dụng.", Toast.LENGTH_SHORT).show();
        }
    }
}
