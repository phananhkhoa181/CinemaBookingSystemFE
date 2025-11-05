package com.example.cinemabookingsystemfe.ui.moviedetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.MockApiService;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.MovieItem;
import com.example.cinemabookingsystemfe.data.models.response.SearchMovieData;
import com.example.cinemabookingsystemfe.ui.moviedetail.adapter.MovieAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class SearchMovieActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private EditText etSearch;
    private ImageView btnClear;
    private ImageView btnSearch;
    private Chip chipGenre;
    private Chip chipSort;




    private List<MovieItem> movieList = new ArrayList<>();
    private List<MovieItem> displayList = new ArrayList<>();
    private String selectedGenre = ""; // lưu thể loại đã chọn
    private String selectedSort = ""; // e.g. "releasedate_desc"



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Views
        rvMovies = findViewById(R.id.rvMovies);
        progressBar = findViewById(R.id.progressBar);
        etSearch = findViewById(R.id.etSearch);
        btnClear = findViewById(R.id.btnClear);
        btnSearch = findViewById(R.id.btnSearch);
        chipGenre = findViewById(R.id.chipGenre);
        chipSort = findViewById(R.id.chipSort);


        // RecyclerView setup
        adapter = new MovieAdapter(displayList);
        rvMovies.setLayoutManager(new GridLayoutManager(this, 5)); // 2 cột
        rvMovies.setAdapter(adapter);

        String movieTitle = getIntent().getStringExtra("movieTitle");
        if (movieTitle != null) {
            etSearch.setText(movieTitle);
        }

        // Load initial data
        loadMovies();

        // Search input listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterMovies(s.toString());
            }
        });

        // Clear button
        btnClear.setOnClickListener(v -> etSearch.setText(""));
        btnSearch.setOnClickListener(v -> searchMovies());

        adapter.setOnItemClickListener(movie -> {
            // Khi click item, mở MovieDetailActivity và truyền movieId
            Intent intent = new Intent(SearchMovieActivity.this, MovieDetailActivity.class);
            intent.putExtra("movieId", movie.getMovieId()); // truyền id
            startActivity(intent);
        });
        chipGenre.setOnClickListener(v -> showGenreDialog());
        chipSort.setOnClickListener(v -> showSortDialog());
    }

    private void loadMovies() {
        progressBar.setVisibility(View.VISIBLE);

        MockApiService.searchMovies(1, 10,"a","a", new ApiCallback<ApiResponse<SearchMovieData>>() {
            @Override
            public void onSuccess(ApiResponse<SearchMovieData> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccess() && response.getData() != null) {
                    movieList.clear();
                    movieList.addAll(response.getData().getItems());
                    displayList.clear();
                    displayList.addAll(movieList);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SearchMovieActivity.this, "Failed to load movies", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SearchMovieActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterMovies(String keyword) {
        displayList.clear();
        if (keyword.isEmpty()) {
            displayList.addAll(movieList);
        } else {
            for (MovieItem movie : movieList) {
                if (movie.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    displayList.add(movie);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    // Gọi API khi nhấn nút Search
    private void searchMovies() {
        String genreToSend = selectedGenre != null && !selectedGenre.isEmpty() ? selectedGenre : null;
        String sortToSend = selectedSort != null && !selectedSort.isEmpty() ? selectedSort : null;
        int page = 1;
        int pageSize = 10;

        progressBar.setVisibility(View.VISIBLE);

        MockApiService.searchMovies(page, pageSize, genreToSend, sortToSend,
                new ApiCallback<ApiResponse<SearchMovieData>>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(ApiResponse<SearchMovieData> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccess() && response.getData() != null) {
                            movieList.clear();
                            movieList.addAll(response.getData().getItems());
                            displayList.clear();
                            displayList.addAll(movieList);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(SearchMovieActivity.this, "No movies found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SearchMovieActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showGenreDialog() {
        String[] genres = {"Kinh dị", "Hành động", "Hoạt hình", "Tình cảm"};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chọn thể loại")
                .setItems(genres, (dialog, which) -> {
                    selectedGenre = genres[which]; // ✅ lưu thể loại
                    Chip chipGenre = findViewById(R.id.chipGenre);
                    chipGenre.setText(selectedGenre);
                })
                .show();
    }

    private void showSortDialog() {
        String[] sorts = {"Mới nhất → Cũ nhất", "Cũ nhất → Mới nhất" , "Tiêu đề = A → Z", "Tiêu đề Z → A"};
        String[] sortValues = {"releasedate_desc", "releasedate_asc","title_asc","title_desc"};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chọn sắp xếp")
                .setItems(sorts, (dialog, which) -> {
                    selectedSort = sortValues[which]; // ✅ lưu giá trị để gọi API
                    chipSort.setText(sorts[which]); // hiển thị tên người dùng chọn
                })
                .show();
    }

}
