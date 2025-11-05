package com.example.cinemabookingsystemfe.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.model.Promotion;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.Movie;
import com.example.cinemabookingsystemfe.data.models.response.PagedResult;
import com.example.cinemabookingsystemfe.data.repository.MovieRepository;

import java.util.List;
import java.util.ArrayList;

public class HomeViewModel extends AndroidViewModel {
    
    private final MovieRepository movieRepository;
    
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<List<Promotion>> promotions = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> nowShowingMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> comingSoonMovies = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    
    public HomeViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(application.getApplicationContext());
        loadPromotions();
    }
    
    private void loadPromotions() {
        // Mock promotion data with beautiful images
        List<Promotion> mockPromotions = new ArrayList<>();
        
        // Khuyến mãi đầu tuần - Cinema seats background
        mockPromotions.add(new Promotion(1, "Khuyến mãi đầu tuần", 
            "Giảm 20% cho vé xem phim vào thứ 2 và thứ 3", 
            "2024-01-01", "2024-12-31", "percentage", 20.0,
            "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=800&q=80"));
        
        // Combo bắp nước - Popcorn background
        mockPromotions.add(new Promotion(2, "Combo bắp nước giá rẻ", 
            "Chỉ 50.000đ cho combo bắp nước size L", 
            "2024-01-01", "2024-12-31", "fixed_amount", 50000.0,
            "https://images.unsplash.com/photo-1585647347384-2593bc35786b?w=800&q=80"));
        
        // Ưu đãi sinh viên - Cinema lights background
        mockPromotions.add(new Promotion(3, "Ưu đãi sinh viên", 
            "Giảm 30% cho sinh viên khi xuất trình thẻ", 
            "2024-01-01", "2024-12-31", "percentage", 30.0,
            "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=800&q=80"));
        
        // Thành viên VIP - Premium cinema background
        mockPromotions.add(new Promotion(4, "Thành viên VIP", 
            "Tích điểm đổi vé miễn phí cho thành viên", 
            "2024-01-01", "2024-12-31", "percentage", 0.0,
            "https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=800&q=80"));
        
        promotions.setValue(mockPromotions);
    }
    
    public void loadHomeData() {
        isLoading.postValue(true);
        
        // Load Now Showing Movies from API
        loadNowShowingMovies();
        
        // Load Coming Soon Movies from API
        loadComingSoonMovies();
    }
    
    private void loadNowShowingMovies() {
        movieRepository.getNowShowingMovies(1, 10, new ApiCallback<ApiResponse<PagedResult<Movie>>>() {
            @Override
            public void onSuccess(ApiResponse<PagedResult<Movie>> response) {
                if (response != null && response.getData() != null) {
                    PagedResult<Movie> pagedResult = response.getData();
                    if (pagedResult.getItems() != null && !pagedResult.getItems().isEmpty()) {
                        nowShowingMovies.postValue(pagedResult.getItems());
                    } else {
                        nowShowingMovies.postValue(new ArrayList<>());
                    }
                }
                isLoading.postValue(false);
            }
            
            @Override
            public void onError(String message) {
                error.postValue("Lỗi tải phim đang chiếu: " + message);
                nowShowingMovies.postValue(new ArrayList<>());
                isLoading.postValue(false);
            }
        });
    }
    
    private void loadComingSoonMovies() {
        movieRepository.getComingSoonMovies(1, 10, new ApiCallback<ApiResponse<PagedResult<Movie>>>() {
            @Override
            public void onSuccess(ApiResponse<PagedResult<Movie>> response) {
                if (response != null && response.getData() != null) {
                    PagedResult<Movie> pagedResult = response.getData();
                    if (pagedResult.getItems() != null && !pagedResult.getItems().isEmpty()) {
                        comingSoonMovies.postValue(pagedResult.getItems());
                    } else {
                        comingSoonMovies.postValue(new ArrayList<>());
                    }
                }
            }
            
            @Override
            public void onError(String message) {
                error.postValue("Lỗi tải phim sắp chiếu: " + message);
                comingSoonMovies.postValue(new ArrayList<>());
            }
        });
    }
    
    // Getters
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<List<Promotion>> getPromotions() {
        return promotions;
    }
    
    public LiveData<List<Movie>> getNowShowingMovies() {
        return nowShowingMovies;
    }
    
    public LiveData<List<Movie>> getComingSoonMovies() {
        return comingSoonMovies;
    }
    
    public LiveData<String> getError() {
        return error;
    }
}
