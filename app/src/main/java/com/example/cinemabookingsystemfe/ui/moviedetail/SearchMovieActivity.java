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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.Movie;
import com.example.cinemabookingsystemfe.data.models.response.PagedResult;
import com.example.cinemabookingsystemfe.data.repository.MovieRepository;
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
    private ImageView btnBack;
    private Chip chipGenre;
    private Chip chipSort;
    private Chip chipYear;
    private Chip chipRating;
    
    private View resultsContainer;
    private View emptyStateInitial;
    private View emptyStateNoResults;
    private View loadingState;
    private TextView tvResultsCount;
    private TextView tvResultsTitle;
    private TextView tvNoResultsMessage;

    private MovieRepository movieRepository;
    private List<Movie> movieList = new ArrayList<>();
    private List<Movie> displayList = new ArrayList<>();
    private String selectedGenre = ""; // lưu thể loại đã chọn
    private String selectedSort = ""; // e.g. "releasedate_desc"
    private String selectedYear = "";
    private String selectedRating = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        initViews();
        setupRecyclerView();
        setupListeners();
        
        // Check if search query passed from HomeFragment
        String movieTitle = getIntent().getStringExtra("movieTitle");
        if (movieTitle != null && !movieTitle.isEmpty()) {
            etSearch.setText(movieTitle);
            searchMovies();
        } else {
            // Load now showing movies when no search query
            loadNowShowingMovies();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        rvMovies = findViewById(R.id.rvMovies);
        progressBar = findViewById(R.id.progressBar);
        etSearch = findViewById(R.id.etSearch);
        btnClear = findViewById(R.id.btnClear);
        btnSearch = findViewById(R.id.btnSearch);
        chipGenre = findViewById(R.id.chipGenre);
        chipSort = findViewById(R.id.chipSort);
        chipYear = findViewById(R.id.chipYear);
        chipRating = findViewById(R.id.chipRating);
        
        resultsContainer = findViewById(R.id.resultsContainer);
        emptyStateInitial = findViewById(R.id.emptyStateInitial);
        emptyStateNoResults = findViewById(R.id.emptyStateNoResults);
        loadingState = findViewById(R.id.loadingState);
        tvResultsCount = findViewById(R.id.tvResultsCount);
        tvResultsTitle = findViewById(R.id.tvResultsTitle);
        tvNoResultsMessage = findViewById(R.id.tvNoResultsMessage);
        
        // Initialize MovieRepository
        movieRepository = MovieRepository.getInstance(this);
    }
    
    private void loadNowShowingMovies() {
        showLoadingState();
        
        // Load all movies using getMovies API (no status filter = all movies)
        movieRepository.getMovies(1, 50, null, null, null, null, new ApiCallback<ApiResponse<PagedResult<Movie>>>() {
            @Override
            public void onSuccess(ApiResponse<PagedResult<Movie>> response) {
                if (response != null && response.getData() != null) {
                    PagedResult<Movie> pagedResult = response.getData();
                    movieList.clear();
                    movieList.addAll(pagedResult.getItems());
                    displayList.clear();
                    displayList.addAll(movieList);
                    adapter.notifyDataSetChanged();
                    
                    if (displayList.isEmpty()) {
                        showEmptyStateInitial();
                    } else {
                        tvResultsTitle.setText("Tất cả phim");
                        showResults(displayList.size());
                    }
                } else {
                    showEmptyStateInitial();
                }
            }

            @Override
            public void onError(String errorMessage) {
                showEmptyStateInitial();
                Toast.makeText(SearchMovieActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setupRecyclerView() {
        adapter = new MovieAdapter(displayList);
        rvMovies.setLayoutManager(new GridLayoutManager(this, 3));
        rvMovies.setAdapter(adapter);
        
        adapter.setOnItemClickListener(movie -> {
            android.util.Log.d("SearchMovieActivity", "Movie clicked: " + movie.getTitle() + " ID: " + movie.getMovieId());
            
            Intent intent = new Intent(SearchMovieActivity.this, MovieDetailActivity.class);
            intent.putExtra("movieId", movie.getMovieId());
            startActivity(intent);
        });
    }
    
    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        
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
                // Auto search after typing (debounced)
                if (s.length() >= 2) {
                    searchMovies();
                } else if (s.length() == 0) {
                    // Show now showing movies when search is cleared
                    loadNowShowingMovies();
                }
            }
        });

        // Clear button
        btnClear.setOnClickListener(v -> {
            etSearch.setText("");
            // Reload now showing movies when cleared
            loadNowShowingMovies();
        });
        
        btnSearch.setOnClickListener(v -> searchMovies());
        
        // Filter chips
        chipGenre.setOnClickListener(v -> showGenreDialog());
        chipSort.setOnClickListener(v -> showSortDialog());
        chipYear.setOnClickListener(v -> showYearDialog());
        chipRating.setOnClickListener(v -> showRatingDialog());
        
        // Enter key on keyboard triggers search
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            searchMovies();
            return true;
        });
    }
    
    private void showEmptyStateInitial() {
        resultsContainer.setVisibility(View.GONE);
        emptyStateInitial.setVisibility(View.VISIBLE);
        emptyStateNoResults.setVisibility(View.GONE);
        loadingState.setVisibility(View.GONE);
    }
    
    private void showEmptyStateNoResults(String query) {
        resultsContainer.setVisibility(View.GONE);
        emptyStateInitial.setVisibility(View.GONE);
        emptyStateNoResults.setVisibility(View.VISIBLE);
        loadingState.setVisibility(View.GONE);
        tvNoResultsMessage.setText("Không tìm thấy phim với từ khóa \"" + query + "\"");
    }
    
    private void showLoadingState() {
        resultsContainer.setVisibility(View.GONE);
        emptyStateInitial.setVisibility(View.GONE);
        emptyStateNoResults.setVisibility(View.GONE);
        loadingState.setVisibility(View.VISIBLE);
    }
    
    private void showResults(int count) {
        resultsContainer.setVisibility(View.VISIBLE);
        emptyStateInitial.setVisibility(View.GONE);
        emptyStateNoResults.setVisibility(View.GONE);
        loadingState.setVisibility(View.GONE);
        tvResultsCount.setText(count + " phim");
    }

    private void searchMovies() {
        String query = etSearch.getText().toString().trim();
        
        if (query.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (query.length() < 2) {
            Toast.makeText(this, "Vui lòng nhập ít nhất 2 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showLoadingState();

        // Use searchMovies API endpoint
        movieRepository.searchMovies(query, 1, 50, new ApiCallback<ApiResponse<PagedResult<Movie>>>() {
            @Override
            public void onSuccess(ApiResponse<PagedResult<Movie>> response) {
                if (response != null && response.getData() != null) {
                    PagedResult<Movie> pagedResult = response.getData();
                    movieList.clear();
                    movieList.addAll(pagedResult.getItems());
                    
                    // Apply local filters if any selected
                    filterMovies(query);
                    
                    if (displayList.isEmpty()) {
                        showEmptyStateNoResults(query);
                    } else {
                        tvResultsTitle.setText("Kết quả tìm kiếm");
                        showResults(displayList.size());
                    }
                } else {
                    showEmptyStateNoResults(query);
                    Toast.makeText(SearchMovieActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                showEmptyStateNoResults(query);
                Toast.makeText(SearchMovieActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterMovies(String keyword) {
        displayList.clear();
        
        for (Movie movie : movieList) {
            boolean matchesKeyword = keyword.isEmpty() || 
                    movie.getTitle().toLowerCase().contains(keyword.toLowerCase());
            boolean matchesGenre = selectedGenre.isEmpty() || 
                    (movie.getGenre() != null && movie.getGenre().toLowerCase().contains(selectedGenre.toLowerCase()));
            boolean matchesRating = selectedRating.isEmpty() || 
                    (movie.getRating() != null && movie.getRating().equalsIgnoreCase(selectedRating));
            
            if (matchesKeyword && matchesGenre && matchesRating) {
                displayList.add(movie);
            }
        }
        
        // Apply sorting
        if (!selectedSort.isEmpty()) {
            sortMovies();
        }
        
        adapter.notifyDataSetChanged();
        
        if (!keyword.isEmpty()) {
            if (displayList.isEmpty()) {
                showEmptyStateNoResults(keyword);
            } else {
                showResults(displayList.size());
            }
        }
    }
    
    private void sortMovies() {
        switch (selectedSort) {
            case "title_asc":
                displayList.sort((m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()));
                break;
            case "title_desc":
                displayList.sort((m1, m2) -> m2.getTitle().compareToIgnoreCase(m1.getTitle()));
                break;
            case "releasedate_desc":
                displayList.sort((m1, m2) -> {
                    if (m1.getReleaseDate() == null) return 1;
                    if (m2.getReleaseDate() == null) return -1;
                    return m2.getReleaseDate().compareTo(m1.getReleaseDate());
                });
                break;
            case "releasedate_asc":
                displayList.sort((m1, m2) -> {
                    if (m1.getReleaseDate() == null) return 1;
                    if (m2.getReleaseDate() == null) return -1;
                    return m1.getReleaseDate().compareTo(m2.getReleaseDate());
                });
                break;
        }
    }


    private void showGenreDialog() {
        String[] genres = {"Tất cả", "Action", "Sci-Fi", "Adventure", "Drama", "Comedy", "Horror", "Romance", "Thriller", "Animation"};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chọn thể loại")
                .setItems(genres, (dialog, which) -> {
                    if (which == 0) {
                        selectedGenre = "";
                        chipGenre.setText("Thể loại");
                    } else {
                        selectedGenre = genres[which];
                        chipGenre.setText(selectedGenre);
                    }
                    chipGenre.setChecked(!selectedGenre.isEmpty());
                    filterMovies(etSearch.getText().toString().trim());
                })
                .show();
    }

    private void showSortDialog() {
        String[] sorts = {"Mới nhất → Cũ nhất", "Cũ nhất → Mới nhất", "Tên A → Z", "Tên Z → A"};
        String[] sortValues = {"releasedate_desc", "releasedate_asc", "title_asc", "title_desc"};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Sắp xếp theo")
                .setItems(sorts, (dialog, which) -> {
                    selectedSort = sortValues[which];
                    chipSort.setText(sorts[which]);
                    chipSort.setChecked(true);
                    filterMovies(etSearch.getText().toString().trim());
                })
                .show();
    }
    
    private void showYearDialog() {
        String[] years = {"Tất cả", "2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015"};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chọn năm phát hành")
                .setItems(years, (dialog, which) -> {
                    if (which == 0) {
                        selectedYear = "";
                        chipYear.setText("Năm");
                    } else {
                        selectedYear = years[which];
                        chipYear.setText(selectedYear);
                    }
                    chipYear.setChecked(!selectedYear.isEmpty());
                    // TODO: Apply year filter when API supports it
                    Toast.makeText(this, "Lọc theo năm: " + (selectedYear.isEmpty() ? "Tất cả" : selectedYear), Toast.LENGTH_SHORT).show();
                })
                .show();
    }
    
    private void showRatingDialog() {
        String[] ratings = {"Tất cả", "G", "PG", "PG-13", "R", "NC-17"};
        String[] ratingDescriptions = {
                "Tất cả độ tuổi",
                "G - Mọi lứa tuổi",
                "PG - Dưới 13 tuổi cần cha mẹ hướng dẫn",
                "PG-13 - Không phù hợp cho trẻ dưới 13 tuổi",
                "R - Dưới 17 tuổi cần cha mẹ đi cùng",
                "NC-17 - Người lớn (17+)"
        };

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chọn độ tuổi")
                .setItems(ratingDescriptions, (dialog, which) -> {
                    if (which == 0) {
                        selectedRating = "";
                        chipRating.setText("Độ tuổi");
                    } else {
                        selectedRating = ratings[which];
                        chipRating.setText(ratings[which]);
                    }
                    chipRating.setChecked(!selectedRating.isEmpty());
                    filterMovies(etSearch.getText().toString().trim());
                })
                .show();
    }

}
