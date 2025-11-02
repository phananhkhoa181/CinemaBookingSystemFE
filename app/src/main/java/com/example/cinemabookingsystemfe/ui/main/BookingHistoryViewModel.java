package com.example.cinemabookingsystemfe.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cinemabookingsystemfe.data.model.Booking;
import com.example.cinemabookingsystemfe.data.repository.BookingRepository;
import com.example.cinemabookingsystemfe.network.ApiCallback;

import java.util.List;

public class BookingHistoryViewModel extends ViewModel {
    
    private final BookingRepository bookingRepository;
    
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<List<Booking>> bookings = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    
    public BookingHistoryViewModel() {
        bookingRepository = BookingRepository.getInstance();
    }
    
    public void loadBookings(String status) {
        isLoading.postValue(true);
        
        bookingRepository.getMyBookings(status, new ApiCallback<List<Booking>>() {
            @Override
            public void onSuccess(List<Booking> data) {
                bookings.postValue(data);
                isLoading.postValue(false);
            }
            
            @Override
            public void onError(String errorMessage) {
                error.postValue(errorMessage);
                isLoading.postValue(false);
            }
        });
    }
    
    // Getters
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<List<Booking>> getBookings() {
        return bookings;
    }
    
    public LiveData<String> getError() {
        return error;
    }
}
