package com.example.cinemabookingsystemfe.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.cinemabookingsystemfe.ui.adapters.BannerAdapter;
import com.example.cinemabookingsystemfe.ui.adapters.MovieAdapter;
import com.example.cinemabookingsystemfe.ui.moviedetail.SearchMovieActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    
    private ViewPager2 vpBanner;
    private TabLayout tabIndicator;
    private TabLayout tabMovies;
    private ImageButton btnPrevBanner, btnNextBanner;
    private ImageButton btnNotification;

    private ImageView btnSearch;
    private RecyclerView rvMovies;
    private ProgressBar progressBar;
    
    private HomeViewModel viewModel;
    private BannerAdapter bannerAdapter;
    private MovieAdapter movieAdapter;

    private EditText etSearch;
    
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

        // Search button
        btnSearch = view.findViewById(R.id.btnSearch);
        etSearch = view.findViewById(R.id.etSearch);
    }
    
    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }
    
    private void setupBanner() {
        bannerAdapter = new BannerAdapter(banner -> {
            // Banner click
        });
        vpBanner.setAdapter(bannerAdapter);
        
        new TabLayoutMediator(tabIndicator, vpBanner, (tab, position) -> {
            // Empty implementation for dots
        }).attach();
        
        // Auto-scroll banner every 7 seconds (slower and smoother)
        bannerHandler = new Handler(Looper.getMainLooper());
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = vpBanner.getCurrentItem();
                int itemCount = bannerAdapter.getItemCount();
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
            // Movie click
            Toast.makeText(getContext(), "Movie: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
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
            int itemCount = bannerAdapter.getItemCount();
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

        // Search Button
        btnSearch.setOnClickListener(v -> {
            String movieTitle = etSearch.getText().toString().trim(); // Lấy text người dùng nhập
            if (!movieTitle.isEmpty()) {
                Intent intent = new Intent(getContext(), SearchMovieActivity.class);
                intent.putExtra("movieTitle", movieTitle); // Truyền dữ liệu
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập tên phim", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void observeViewModel() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
        
        viewModel.getBanners().observe(getViewLifecycleOwner(), banners -> {
            if (banners != null && !banners.isEmpty()) {
                bannerAdapter.setBanners(banners);
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
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bannerHandler != null && bannerRunnable != null) {
            bannerHandler.removeCallbacks(bannerRunnable);
        }
    }
}
