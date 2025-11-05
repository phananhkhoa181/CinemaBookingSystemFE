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
import com.example.cinemabookingsystemfe.data.api.MockApiService;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.MovieDetailResponse;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.ShowtimesDate;
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

    private MovieDetailResponse currentMovie;
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

        int movieId = getIntent().getIntExtra("movieId", 1);
        loadMovieDetail(movieId);
        loadShowtimes(movieId);

        btnBookNow.setOnClickListener(v -> doBooking());
        btnWatchTrailer.setOnClickListener(v -> doWatchTrailer());
    }

    private void loadMovieDetail(int movieId) {
        progressBar.setVisibility(View.VISIBLE);
        MockApiService.getMovieDetail(movieId, new ApiCallback<MovieDetailResponse>() {
            @Override
            public void onSuccess(MovieDetailResponse movie) {
                progressBar.setVisibility(View.GONE);
                currentMovie = movie;

                Glide.with(MovieDetailActivity.this).load(movie.getBackdropUrl()).into(ivBackdrop);
                Glide.with(MovieDetailActivity.this).load(movie.getPosterUrl()).into(ivPoster);
                tvTitle.setText(movie.getTitle());
                tvOverview.setText(movie.getOverview());
                tvGenre.setText(movie.getGenre());
                tvRating.setText(String.valueOf(movie.getRating()));
                tvDirector.setText(movie.getDirector());

                String year = new SimpleDateFormat("yyyy", Locale.getDefault())
                        .format(movie.getReleaseDate());
                tvDuration.setText(movie.getDuration() + " min • " + year);
                tvAgeRating.setText(movie.getAgeRating());
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MovieDetailActivity.this,
                        "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadShowtimes(int movieId) {
        progressBar.setVisibility(View.VISIBLE);
        MockApiService.getMovieShowtimes(movieId, new ApiCallback<ApiResponse<List<ShowtimesDate>>>() {
            @Override
            public void onSuccess(ApiResponse<List<ShowtimesDate>> response) {
                progressBar.setVisibility(View.GONE);
                List<ShowtimesDate> showtimes = response.getData();
                showtimeAdapter = new ShowtimeAdapter(showtimes, showtime ->
                        Toast.makeText(MovieDetailActivity.this,
                                "Chọn suất " + showtime.getStartTime() +
                                        " (" + showtime.getLanguageType() + ")",
                                Toast.LENGTH_SHORT).show());
                rvShowtimes.setAdapter(showtimeAdapter);
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MovieDetailActivity.this,
                        "Error loading showtimes: " + errorMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doBooking() {
        Toast.makeText(this, "Booking feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    private void doWatchTrailer() {
        if (currentMovie != null && currentMovie.getTrailerUrl() != null && !currentMovie.getTrailerUrl().isEmpty()) {
            Intent intent = new Intent(MovieDetailActivity.this, MovieTrailerActivity.class);
            intent.putExtra("trailerUrl", currentMovie.getTrailerUrl());
            startActivity(intent);
        } else {
            Toast.makeText(MovieDetailActivity.this, "Trailer not available.", Toast.LENGTH_SHORT).show();
        }
    }
}
