package com.example.cinemabookingsystemfe.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.ui.adapters.BookingAdapter;
import com.example.cinemabookingsystemfe.ui.bookingdetail.BookingDetailActivity;

public class BookingHistoryFragment extends Fragment {
    
    private RecyclerView rvBookings;
    private SwipeRefreshLayout swipeRefresh;
    private View layoutEmpty;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    
    private BookingHistoryViewModel viewModel;
    private BookingAdapter adapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                            @Nullable ViewGroup container, 
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_history, container, false);
        
        initViews(view);
        initViewModel();
        setupRecyclerView();
        setupListeners();
        observeViewModel();
        
        // Load all bookings
        viewModel.loadBookings("All");
        
        return view;
    }
    
    private void initViews(View view) {
        rvBookings = view.findViewById(R.id.rvBookings);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);
        progressBar = view.findViewById(R.id.progressBar);
        toolbar = view.findViewById(R.id.toolbar);
        
        // Setup toolbar - center the title
        if (toolbar != null) {
            toolbar.post(() -> {
                for (int i = 0; i < toolbar.getChildCount(); i++) {
                    View child = toolbar.getChildAt(i);
                    if (child instanceof android.widget.TextView) {
                        android.widget.TextView textView = (android.widget.TextView) child;
                        Toolbar.LayoutParams params = (Toolbar.LayoutParams) textView.getLayoutParams();
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        params.gravity = Gravity.CENTER;
                        textView.setLayoutParams(params);
                        textView.setGravity(Gravity.CENTER);
                        break;
                    }
                }
            });
        }
    }
    
    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(BookingHistoryViewModel.class);
    }
    
    private void setupRecyclerView() {
        adapter = new BookingAdapter(booking -> {
            navigateToBookingDetail(booking.getBookingId());
        });
        
        rvBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBookings.setAdapter(adapter);
    }
    
    private void setupListeners() {
        // Pull to refresh
        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.loadBookings("All");
        });
    }
    
    private void observeViewModel() {
        // Loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            swipeRefresh.setRefreshing(false);
        });
        
        // Bookings data
        viewModel.getBookings().observe(getViewLifecycleOwner(), bookings -> {
            if (bookings != null && !bookings.isEmpty()) {
                adapter.setBookings(bookings);
                rvBookings.setVisibility(View.VISIBLE);
                layoutEmpty.setVisibility(View.GONE);
            } else {
                rvBookings.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        });
        
        // Error handling
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void navigateToBookingDetail(int bookingId) {
        Intent intent = new Intent(getContext(), BookingDetailActivity.class);
        intent.putExtra("BOOKING_ID", bookingId);
        startActivity(intent);
    }
}
