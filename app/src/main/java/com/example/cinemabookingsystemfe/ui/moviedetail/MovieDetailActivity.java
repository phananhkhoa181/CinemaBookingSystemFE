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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.MovieDetail;
import com.example.cinemabookingsystemfe.data.models.response.MovieShowtimesResponse;
import com.example.cinemabookingsystemfe.data.models.response.ReviewResponse;
import com.example.cinemabookingsystemfe.data.models.response.ShowtimesByDate;
import com.example.cinemabookingsystemfe.data.repository.MovieRepository;
import com.example.cinemabookingsystemfe.ui.moviedetail.adapter.ShowtimeAdapter;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView ivBackdrop, ivPoster;
    private TextView tvTitle, tvOverview, tvGenre, tvRating, tvDuration, tvAgeRating;
    private TextView tvDirector;
    private MaterialButton btnBookNow, btnWatchTrailer;
    private RecyclerView rvShowtimes;

    private MovieRepository movieRepository;
    private MovieDetail currentMovie;
    private ShowtimeAdapter showtimeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Mapping UI
        progressBar = findViewById(R.id.progressBar);
        ivBackdrop = findViewById(R.id.ivBackdrop);
        ivPoster = findViewById(R.id.ivPoster);
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        tvGenre = findViewById(R.id.tvGenre);
        tvRating = findViewById(R.id.tvRating);
        tvDuration = findViewById(R.id.tvDuration);
        tvAgeRating = findViewById(R.id.tvAgeRating);
        tvDirector = findViewById(R.id.tvDirector);
        btnBookNow = findViewById(R.id.btnBookNow);
        btnWatchTrailer = findViewById(R.id.btnWatchTrailer);
        rvShowtimes = findViewById(R.id.rvShowtimes);

        rvShowtimes.setLayoutManager(new LinearLayoutManager(this));

        // Initialize MovieRepository
        movieRepository = MovieRepository.getInstance(this);

        int movieId = getIntent().getIntExtra("movieId", 1);
        loadMovieDetail(movieId);
        loadShowtimes(movieId);
        loadMovieRating(movieId); // Load rating từ Reviews API

        btnBookNow.setOnClickListener(v -> doBooking());
        btnWatchTrailer.setOnClickListener(v -> doWatchTrailer());
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

                    // Note: API doesn't have backdropUrl, using posterUrl for both
                    Glide.with(MovieDetailActivity.this).load(movie.getPosterUrl()).into(ivBackdrop);
                    Glide.with(MovieDetailActivity.this).load(movie.getPosterUrl()).into(ivPoster);
                    tvTitle.setText(movie.getTitle());
                    tvOverview.setText(movie.getDescription());
                    tvGenre.setText(movie.getGenre());
                    // averageRating is the review score (1-5), display it
                    if (movie.getAverageRating() != null) {
                        tvRating.setText(String.format("%.1f/5 ⭐ (%d đánh giá)", 
                            movie.getAverageRating(), movie.getTotalReviews()));
                    } else {
                        tvRating.setText("Chưa có đánh giá");
                    }
                    tvDirector.setText(movie.getDirector());

                    // Extract year from releaseDate (yyyy-MM-dd)
                    String year = movie.getReleaseDate() != null && movie.getReleaseDate().length() >= 4
                            ? movie.getReleaseDate().substring(0, 4) : "";
                    tvDuration.setText(movie.getDurationMinutes() + " phút • " + year);
                    tvAgeRating.setText(movie.getRating()); // Age rating like "P", "T13", "T16"
                    
                    // Enable booking if movie has showtimes
                    if (movie.getTotalShowtimes() > 0) {
                        btnBookNow.setEnabled(true);
                        btnBookNow.setText("Đặt vé");
                        btnBookNow.setAlpha(1.0f);
                    } else {
                        btnBookNow.setEnabled(false);
                        btnBookNow.setText("Chưa có suất chiếu");
                        btnBookNow.setAlpha(0.5f);
                    }
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

    private void loadMovieRating(int movieId) {
        // Load rating from Reviews API
        movieRepository.getMovieReviews(movieId, 1, 10, "latest", new ApiCallback<ApiResponse<ReviewResponse>>() {
            @Override
            public void onSuccess(ApiResponse<ReviewResponse> response) {
                if (response != null && response.getData() != null) {
                    ReviewResponse reviewData = response.getData();
                    double avgRating = reviewData.getAverageRating();
                    int totalReviews = reviewData.getTotalCount();
                    
                    // Update rating display
                    if (totalReviews > 0) {
                        tvRating.setText(String.format("%.1f/5 ⭐ (%d đánh giá)", avgRating, totalReviews));
                    } else {
                        tvRating.setText("Chưa có đánh giá");
                    }
                }
            }
            
            @Override
            public void onError(String errorMessage) {
                android.util.Log.e("MovieDetail", "Error loading reviews: " + errorMessage);
                // Keep existing rating from MovieDetail API or show default
                tvRating.setText("Chưa có đánh giá");
            }
        });
    }
    
    private void loadShowtimes(int movieId) {
        progressBar.setVisibility(View.VISIBLE);
        // Pass null for date and cinemaId to get all showtimes
        movieRepository.getMovieShowtimes(movieId, null, null, new ApiCallback<ApiResponse<MovieShowtimesResponse>>() {
            @Override
            public void onSuccess(ApiResponse<MovieShowtimesResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response != null && response.getData() != null) {
                    MovieShowtimesResponse data = response.getData();
                    List<ShowtimesByDate> showtimes = data.getShowtimesByDate();
                    
                    // TODO: Update ShowtimeAdapter to display showtimes
                    // For now, just show a message
                    if (showtimes == null || showtimes.isEmpty()) {
                        Toast.makeText(MovieDetailActivity.this,
                                "Chưa có suất chiếu", Toast.LENGTH_SHORT).show();
                    } else {
                        // Count total showtimes
                        int totalShowtimes = 0;
                        for (ShowtimesByDate dateGroup : showtimes) {
                            if (dateGroup.getCinemas() != null) {
                                for (ShowtimesByDate.ShowtimesByCinema cinema : dateGroup.getCinemas()) {
                                    if (cinema.getShowtimes() != null) {
                                        totalShowtimes += cinema.getShowtimes().size();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                // Log only, no toast
                android.util.Log.d("MovieDetail", "Showtimes error: " + errorMessage);
            }
        });
    }

    private void doBooking() {
        if (currentMovie == null) {
            Toast.makeText(this, "Vui lòng đợi thông tin phim được tải...", Toast.LENGTH_SHORT).show();
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
