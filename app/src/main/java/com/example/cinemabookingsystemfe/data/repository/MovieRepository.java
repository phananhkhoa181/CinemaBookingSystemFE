package com.example.cinemabookingsystemfe.data.repository;

import com.example.cinemabookingsystemfe.data.model.Movie;
import com.example.cinemabookingsystemfe.data.model.PagedResult;
import com.example.cinemabookingsystemfe.network.ApiCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieRepository {
    private static MovieRepository instance;
    
    private MovieRepository() {
    }
    
    public static synchronized MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }
    
    public void getMovies(String status, String genre, String search, 
                         int page, int pageSize, ApiCallback<PagedResult<Movie>> callback) {
        // Mock data for testing
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate network delay
                
                List<Movie> movies = generateMockMovies(status);
                PagedResult<Movie> result = new PagedResult<>(movies, movies.size(), page, pageSize);
                
                // Callback on main thread would be needed in real implementation
                callback.onSuccess(result);
            } catch (Exception e) {
                callback.onError("Lỗi khi tải danh sách phim: " + e.getMessage());
            }
        }).start();
    }
    
    private List<Movie> generateMockMovies(String status) {
        List<Movie> movies = new ArrayList<>();
        
        String[] ageRatings = {"P", "T13", "T16", "T18", "C"};
        
        for (int i = 1; i <= 5; i++) {
            Movie movie = new Movie();
            movie.setMovieId(i);
            movie.setTitle("Phim " + status + " " + i);
            movie.setPosterUrl("https://via.placeholder.com/300x450");
            movie.setRating(ageRatings[i % ageRatings.length]); // Age rating (String)
            movie.setAverageRating(7.5 + (i * 0.3)); // Average rating score (double)
            movie.setStatus(status);
            movie.setGenres(Arrays.asList("Hành động", "Phiêu lưu"));
            movies.add(movie);
        }
        
        return movies;
    }
}
