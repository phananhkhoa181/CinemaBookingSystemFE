package com.example.cinemabookingsystemfe.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cinemabookingsystemfe.data.model.Movie;
import com.example.cinemabookingsystemfe.data.model.Banner;
import com.example.cinemabookingsystemfe.data.repository.MovieRepository;
import com.example.cinemabookingsystemfe.network.ApiCallback;
import com.example.cinemabookingsystemfe.data.model.PagedResult;

import java.util.List;
import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    
    private final MovieRepository movieRepository;
    
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<List<Banner>> banners = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> nowShowingMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> comingSoonMovies = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    
    public HomeViewModel() {
        movieRepository = MovieRepository.getInstance();
        loadBanners();
    }
    
    private void loadBanners() {
        // Mock banner data with real movie backdrops
        List<Banner> mockBanners = new ArrayList<>();
        mockBanners.add(new Banner(1, "Avatar: The Way of Water", 
            "https://image.tmdb.org/t/p/original/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg", 
            "https://example.com/1", "External", 0, true));
        mockBanners.add(new Banner(2, "Black Panther: Wakanda Forever", 
            "https://image.tmdb.org/t/p/original/yYrvN5WFeGYjJnRzhY0QXuo4Isw.jpg", 
            "https://example.com/2", "External", 0, true));
        mockBanners.add(new Banner(3, "Top Gun: Maverick", 
            "https://image.tmdb.org/t/p/original/odJ4hx6g6vBt4lBWKFD1tI8WS4x.jpg", 
            "https://example.com/3", "External", 0, true));
        mockBanners.add(new Banner(4, "Spider-Man: No Way Home", 
            "https://image.tmdb.org/t/p/original/iQFcwSGbZXMkeyKrxbPnwnRo5fl.jpg", 
            "https://example.com/4", "External", 0, true));
        banners.setValue(mockBanners);
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
                                 String genres, double rating, int duration) {
        Movie movie = new Movie();
        movie.setMovieId(id);
        movie.setTitle(title);
        movie.setPosterUrl(posterUrl);
        movie.setGenres(new ArrayList<>(java.util.Arrays.asList(genres.split(", "))));
        movie.setRating(rating);
        movie.setDuration(duration);
        movie.setReleaseDate("2024-01-01"); // Mock date
        movie.setDescription("A captivating movie experience.");
        movie.setTrailerUrl("https://youtube.com/watch?v=mock");
        return movie;
    }
    
    // Getters
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<List<Banner>> getBanners() {
        return banners;
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
