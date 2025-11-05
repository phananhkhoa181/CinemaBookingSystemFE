package com.example.cinemabookingsystemfe.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.Movie;
import com.example.cinemabookingsystemfe.ui.adapters.PromotionAdapter;
import com.example.cinemabookingsystemfe.ui.adapters.MovieAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    
    private ViewPager2 vpBanner;
    private TabLayout tabIndicator;
    private TabLayout tabMovies;
    private ImageButton btnPrevBanner, btnNextBanner;
    private ImageButton btnNotification;
    private RecyclerView rvMovies;
    private ProgressBar progressBar;
    
    private HomeViewModel viewModel;
    private PromotionAdapter promotionAdapter;
    private MovieAdapter movieAdapter;
    
    private Handler bannerHandler;
    private Runnable bannerRunnable;
    private boolean isNowShowing = true;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                            @Nullable ViewGroup container, 
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        initViews(view);
        initViewModel();
        setupBanner();
        setupMoviesRecyclerView();
        setupListeners();
        observeViewModel();
        
        // Load data from ViewModel
        viewModel.loadHomeData();
        
        return view;
    }

    private void initViews(View view) {
        vpBanner = view.findViewById(R.id.vpBanner);
        tabIndicator = view.findViewById(R.id.tabIndicator);
        tabMovies = view.findViewById(R.id.tabMovies);
        btnPrevBanner = view.findViewById(R.id.btnPrevBanner);
        btnNextBanner = view.findViewById(R.id.btnNextBanner);
        btnNotification = view.findViewById(R.id.btnNotification);
        rvMovies = view.findViewById(R.id.rvMovies);
        progressBar = view.findViewById(R.id.progressBar);
        
        // Setup SearchView
        try {
            int searchBarId = getResources().getIdentifier("searchBar", "id", requireContext().getPackageName());
            if (searchBarId != 0) {
                View searchBarView = view.findViewById(searchBarId);
                if (searchBarView instanceof androidx.appcompat.widget.SearchView) {
                    androidx.appcompat.widget.SearchView searchBar = (androidx.appcompat.widget.SearchView) searchBarView;
                    
                    // Get the SearchView AutoCompleteTextView and set colors
                    android.widget.AutoCompleteTextView searchText = 
                        searchBar.findViewById(androidx.appcompat.R.id.search_src_text);
                    if (searchText != null) {
                        // Use same colors as search movie activity
                        int textPrimary = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.textPrimary);
                        int textHint = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.textHint);
                        
                        searchText.setTextColor(textPrimary);
                        searchText.setHintTextColor(textHint);
                        
                        // Set cursor color (API 29+)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                            searchText.setTextCursorDrawable(R.drawable.cursor_white);
                        }
                    }
                    
                    // When SearchView gets focus, immediately open SearchMovieActivity
                    searchBar.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
                        if (hasFocus) {
                            searchBar.clearFocus(); // Clear focus to prevent keyboard
                            Intent intent = new Intent(getContext(), com.example.cinemabookingsystemfe.ui.moviedetail.SearchMovieActivity.class);
                            startActivity(intent);
                        }
                    });
                    
                    // Also handle direct clicks
                    searchBar.setOnClickListener(v -> {
                        Intent intent = new Intent(getContext(), com.example.cinemabookingsystemfe.ui.moviedetail.SearchMovieActivity.class);
                        startActivity(intent);
                    });
                    
                    // Handle search icon click
                    searchBar.setOnSearchClickListener(v -> {
                        Intent intent = new Intent(getContext(), com.example.cinemabookingsystemfe.ui.moviedetail.SearchMovieActivity.class);
                        startActivity(intent);
                    });
                }
            }
        } catch (Exception e) {
            // SearchView not available yet, will be added later
        }
    }
    
    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }
    
    private void setupBanner() {
        promotionAdapter = new PromotionAdapter(promotion -> {
            // Promotion click
            Toast.makeText(getContext(), promotion.getName(), Toast.LENGTH_SHORT).show();
        });
        vpBanner.setAdapter(promotionAdapter);
        
        new TabLayoutMediator(tabIndicator, vpBanner, (tab, position) -> {
            // Empty implementation for dots
        }).attach();
        
        // Auto-scroll banner every 7 seconds (slower and smoother)
        bannerHandler = new Handler(Looper.getMainLooper());
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = vpBanner.getCurrentItem();
                int itemCount = promotionAdapter.getItemCount();
                if (itemCount > 0) {
                    int nextItem = (currentItem + 1) % itemCount;
                    vpBanner.setCurrentItem(nextItem, true);
                }
                bannerHandler.postDelayed(this, 7000); // 7 seconds
            }
        };
        
        // Start auto-scroll with initial delay
        bannerHandler.postDelayed(bannerRunnable, 7000);
        
        // Pause auto-scroll when user touches banner
        vpBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    // User is dragging - stop auto-scroll
                    bannerHandler.removeCallbacks(bannerRunnable);
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    // User finished dragging - restart auto-scroll after delay
                    bannerHandler.removeCallbacks(bannerRunnable); // Clear any pending callbacks first
                    bannerHandler.postDelayed(bannerRunnable, 7000);
                }
            }
        });
    }
    
    private void setupMoviesRecyclerView() {
        // Single adapter that switches between Now Showing and Coming Soon
        movieAdapter = new MovieAdapter(movie -> {
            // Navigate to Movie Detail
            navigateToMovieDetail(movie);
        });
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(
            getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvMovies.setLayoutManager(layoutManager);
        rvMovies.setAdapter(movieAdapter);
    }
    
    private void setupListeners() {
        // Banner navigation buttons
        btnPrevBanner.setOnClickListener(v -> {
            int currentItem = vpBanner.getCurrentItem();
            if (currentItem > 0) {
                vpBanner.setCurrentItem(currentItem - 1, true);
            }
        });
        
        btnNextBanner.setOnClickListener(v -> {
            int currentItem = vpBanner.getCurrentItem();
            int itemCount = promotionAdapter.getItemCount();
            if (currentItem < itemCount - 1) {
                vpBanner.setCurrentItem(currentItem + 1, true);
            }
        });
        
        // Notification button
        btnNotification.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Thông báo", Toast.LENGTH_SHORT).show();
        });
        
        // Tab selection for movies
        tabMovies.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isNowShowing = tab.getPosition() == 0;
                updateMoviesList();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void observeViewModel() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
        
        viewModel.getPromotions().observe(getViewLifecycleOwner(), promotions -> {
            if (promotions != null && !promotions.isEmpty()) {
                promotionAdapter.setPromotions(promotions);
            }
        });
        
        viewModel.getNowShowingMovies().observe(getViewLifecycleOwner(), movies -> {
            if (isNowShowing && movies != null) {
                movieAdapter.setMovies(movies);
            }
        });
        
        viewModel.getComingSoonMovies().observe(getViewLifecycleOwner(), movies -> {
            if (!isNowShowing && movies != null) {
                movieAdapter.setMovies(movies);
            }
        });
        
        // Error handling
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateMoviesList() {
        if (isNowShowing) {
            if (viewModel.getNowShowingMovies().getValue() != null) {
                movieAdapter.setMovies(viewModel.getNowShowingMovies().getValue());
            }
        } else {
            if (viewModel.getComingSoonMovies().getValue() != null) {
                movieAdapter.setMovies(viewModel.getComingSoonMovies().getValue());
            }
        }
    }
    
    private void navigateToMovieDetail(Movie movie) {
        Intent intent = new Intent(getContext(), 
                com.example.cinemabookingsystemfe.ui.moviedetail.MovieDetailActivity.class);
        intent.putExtra("movieId", movie.getMovieId());
        startActivity(intent);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bannerHandler != null && bannerRunnable != null) {
            bannerHandler.removeCallbacks(bannerRunnable);
        }
    }
}
