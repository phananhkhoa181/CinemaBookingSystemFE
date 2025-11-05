package com.example.cinemabookingsystemfe.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cinemabookingsystemfe.data.model.Movie;
import com.example.cinemabookingsystemfe.data.model.Promotion;
import com.example.cinemabookingsystemfe.data.repository.MovieRepository;
import com.example.cinemabookingsystemfe.network.ApiCallback;
import com.example.cinemabookingsystemfe.data.model.PagedResult;

import java.util.List;
import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    
    private final MovieRepository movieRepository;
    
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<List<Promotion>> promotions = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> nowShowingMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> comingSoonMovies = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    
    public HomeViewModel() {
        movieRepository = MovieRepository.getInstance();
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
        
        // TODO: Replace with real API when backend is ready
        // For now, use mock data for UI development
        loadMockData();
    }
    
    private void loadMockData() {
        // Simulate network delay
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            // Mock Now Showing Movies
            List<Movie> mockNowShowing = new ArrayList<>();
            mockNowShowing.add(createMockMovie(1, "Avatar: The Way of Water", 
                "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg", 
                "Action, Adventure, Fantasy", 8.5, 192));
            mockNowShowing.add(createMockMovie(2, "Black Panther: Wakanda Forever", 
                "https://image.tmdb.org/t/p/w500/sv1xJUazXeYqALzczSZ3O6nkH75.jpg", 
                "Action, Adventure, Drama", 8.2, 161));
            mockNowShowing.add(createMockMovie(3, "Top Gun: Maverick", 
                "https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg", 
                "Action, Drama", 8.8, 131));
            mockNowShowing.add(createMockMovie(4, "Everything Everywhere All at Once", 
                "https://image.tmdb.org/t/p/w500/w3LxiVYdWWRvEVdn5RYq6jIqkb1.jpg", 
                "Action, Adventure, Comedy", 8.7, 139));
            mockNowShowing.add(createMockMovie(5, "The Batman", 
                "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg", 
                "Action, Crime, Drama", 8.3, 176));
            mockNowShowing.add(createMockMovie(6, "Spider-Man: No Way Home", 
                "https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg", 
                "Action, Adventure, Sci-Fi", 8.9, 148));
            
            // Mock Coming Soon Movies
            List<Movie> mockComingSoon = new ArrayList<>();
            mockComingSoon.add(createMockMovie(7, "Guardians of the Galaxy Vol. 3", 
                "https://image.tmdb.org/t/p/w500/r2J02Z2OpNTctfOSN1Ydgii51I3.jpg", 
                "Action, Adventure, Comedy", 8.6, 150));
            mockComingSoon.add(createMockMovie(8, "The Flash", 
                "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg", 
                "Action, Adventure, Fantasy", 7.8, 144));
            mockComingSoon.add(createMockMovie(9, "Indiana Jones and the Dial of Destiny", 
                "https://image.tmdb.org/t/p/w500/Af4bXE63pVsb2FtbW8uYIyPBadD.jpg", 
                "Action, Adventure", 7.5, 154));
            mockComingSoon.add(createMockMovie(10, "Mission: Impossible - Dead Reckoning", 
                "https://image.tmdb.org/t/p/w500/NNxYkU70HPurnNCSiCjYAmacwm.jpg", 
                "Action, Thriller", 8.4, 163));
            mockComingSoon.add(createMockMovie(11, "Oppenheimer", 
                "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg", 
                "Biography, Drama, History", 9.0, 180));
            mockComingSoon.add(createMockMovie(12, "Barbie", 
                "https://image.tmdb.org/t/p/w500/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg", 
                "Adventure, Comedy, Fantasy", 8.1, 114));
            
            nowShowingMovies.postValue(mockNowShowing);
            comingSoonMovies.postValue(mockComingSoon);
            isLoading.postValue(false);
        }, 500); // 500ms delay to simulate network
    }
    
    private Movie createMockMovie(int id, String title, String posterUrl, 
                                 String genres, double averageRating, int duration) {
        Movie movie = new Movie();
        movie.setMovieId(id);
        movie.setTitle(title);
        movie.setPosterUrl(posterUrl);
        movie.setGenres(new ArrayList<>(java.util.Arrays.asList(genres.split(", "))));
        movie.setAverageRating(averageRating);
        movie.setDuration(duration);
        movie.setReleaseDate("2024-01-01"); // Mock date
        movie.setDescription("A captivating movie experience.");
        movie.setTrailerUrl("https://youtube.com/watch?v=mock");
        
        // Set age rating based on movie for variety
        if (id % 5 == 0) {
            movie.setRating("P"); // Phổ biến
        } else if (id % 4 == 0) {
            movie.setRating("T13"); 
        } else if (id % 3 == 0) {
            movie.setRating("T16");
        } else if (id % 2 == 0) {
            movie.setRating("T18");
        } else {
            movie.setRating("C"); // Cấm trẻ em
        }
        
        return movie;
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
