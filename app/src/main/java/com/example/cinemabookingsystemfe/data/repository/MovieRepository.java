package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;

import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;

/**
 * MovieRepository - Xử lý movie data
 * TODO: Implement getMovies, getMovieById, searchMovies
 * 
 * ASSIGNED TO: Developer 2
 * PRIORITY: HIGH
 * DEADLINE: Week 3
 * 
 * Refer to: docs-FE/02-API-Integration/02-Repository-Pattern.md
 */
public class MovieRepository {
    
    private static MovieRepository instance;
    private final ApiService apiService;
    
    private MovieRepository(Context context) {
        apiService = ApiClient.getInstance(context).getApiService();
    }
    
    public static synchronized MovieRepository getInstance(Context context) {
        if (instance == null) {
            instance = new MovieRepository(context);
        }
        return instance;
    }
    
    // TODO: Implement methods
    // - getMovies(status, genre, page, pageSize, callback)
    // - getMovieById(movieId, callback)
    // - getNowShowingMovies(page, pageSize, callback)
    // - getComingSoonMovies(page, pageSize, callback)
    // - searchMovies(keyword, page, pageSize, callback)
}
