package com.example.cinemabookingsystemfe.ui.main;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.BookingListResponse;
import com.example.cinemabookingsystemfe.data.models.response.PaginatedBookingResponse;
import com.example.cinemabookingsystemfe.data.repository.BookingRepository;

import java.util.List;

public class BookingHistoryViewModel extends AndroidViewModel {
    
    private final BookingRepository bookingRepository;
    
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<List<BookingListResponse>> bookings = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalItems = new MutableLiveData<>(0);
    
    private int currentPage = 1;
    private static final int PAGE_SIZE = 10;
    
    public BookingHistoryViewModel(Application application) {
        super(application);
        bookingRepository = BookingRepository.getInstance(application);
    }
    
    public void loadBookings(String status) {
        loadBookings(1, status); // Reset to page 1
    }
    
    public void loadBookings(int page, String status) {
        isLoading.postValue(true);
        currentPage = page;
        
        bookingRepository.getMyBookings(page, PAGE_SIZE, status, 
            new ApiCallback<ApiResponse<PaginatedBookingResponse>>() {
            @Override
            public void onSuccess(ApiResponse<PaginatedBookingResponse> response) {
                if (response != null && response.getData() != null) {
                    PaginatedBookingResponse data = response.getData();
                    bookings.postValue(data.getItems());
                    totalItems.postValue(data.getTotalItems());
                } else {
                    bookings.postValue(null);
                    totalItems.postValue(0);
                }
                isLoading.postValue(false);
            }
            
            @Override
            public void onError(String errorMessage) {
                error.postValue(errorMessage);
                isLoading.postValue(false);
            }
        });
    }
    
    public void loadNextPage(String status) {
        loadBookings(currentPage + 1, status);
    }
    
    // Getters
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<List<BookingListResponse>> getBookings() {
        return bookings;
    }
    
    public LiveData<String> getError() {
        return error;
    }
    
    public LiveData<Integer> getTotalItems() {
        return totalItems;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
}
