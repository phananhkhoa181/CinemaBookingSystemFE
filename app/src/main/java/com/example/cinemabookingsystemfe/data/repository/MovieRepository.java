package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;

import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.Movie;
import com.example.cinemabookingsystemfe.data.models.response.MovieDetail;
import com.example.cinemabookingsystemfe.data.models.response.MovieShowtimesResponse;
import com.example.cinemabookingsystemfe.data.models.response.PagedResult;
import com.example.cinemabookingsystemfe.data.models.response.ReviewResponse;
import com.example.cinemabookingsystemfe.data.models.response.ShowtimesByDate;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MovieRepository - Handle movie-related API calls
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
    
    /**
     * Get all movies with optional filters
     */
    public void getMovies(Integer page, Integer pageSize, String genre, Integer year, 
                         String rating, String sort, ApiCallback<ApiResponse<PagedResult<Movie>>> callback) {
        Call<ApiResponse<PagedResult<Movie>>> call = apiService.getMovies(page, pageSize, genre, year, rating, sort);
        
        call.enqueue(new Callback<ApiResponse<PagedResult<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PagedResult<Movie>>> call, Response<ApiResponse<PagedResult<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to load movies: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<PagedResult<Movie>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    /**
     * Get now showing movies
     */
    public void getNowShowingMovies(Integer page, Integer pageSize, ApiCallback<ApiResponse<PagedResult<Movie>>> callback) {
        Call<ApiResponse<PagedResult<Movie>>> call = apiService.getNowShowingMovies(page, pageSize);
        
        call.enqueue(new Callback<ApiResponse<PagedResult<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PagedResult<Movie>>> call, Response<ApiResponse<PagedResult<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to load now showing movies: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<PagedResult<Movie>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    /**
     * Get coming soon movies
     */
    public void getComingSoonMovies(Integer page, Integer pageSize, ApiCallback<ApiResponse<PagedResult<Movie>>> callback) {
        Call<ApiResponse<PagedResult<Movie>>> call = apiService.getComingSoonMovies(page, pageSize);
        
        call.enqueue(new Callback<ApiResponse<PagedResult<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PagedResult<Movie>>> call, Response<ApiResponse<PagedResult<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to load coming soon movies: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<PagedResult<Movie>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    /**
     * Search movies
     */
    public void searchMovies(String query, Integer page, Integer pageSize, ApiCallback<ApiResponse<PagedResult<Movie>>> callback) {
        Call<ApiResponse<PagedResult<Movie>>> call = apiService.searchMovies(query, page, pageSize);
        
        call.enqueue(new Callback<ApiResponse<PagedResult<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PagedResult<Movie>>> call, Response<ApiResponse<PagedResult<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to search movies: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<PagedResult<Movie>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    /**
     * Get movie detail by ID
     */
    public void getMovieDetail(int movieId, ApiCallback<ApiResponse<MovieDetail>> callback) {
        Call<ApiResponse<MovieDetail>> call = apiService.getMovieById(movieId);
        
        call.enqueue(new Callback<ApiResponse<MovieDetail>>() {
            @Override
            public void onResponse(Call<ApiResponse<MovieDetail>> call, Response<ApiResponse<MovieDetail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Failed to load movie detail: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += " - " + response.errorBody().string();
                        } catch (Exception e) {
                            android.util.Log.e("MovieRepository", "Error reading error body", e);
                        }
                    }
                    android.util.Log.e("MovieRepository", "getMovieDetail error: " + errorMsg);
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<MovieDetail>> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                android.util.Log.e("MovieRepository", "getMovieDetail failure", t);
                callback.onError(errorMsg);
            }
        });
    }
    
    /**
     * Get movie showtimes (returns movie info + showtimes grouped by date/cinema)
     */
    public void getMovieShowtimes(int movieId, String date, Integer cinemaId, 
                                  ApiCallback<ApiResponse<MovieShowtimesResponse>> callback) {
        Call<ApiResponse<MovieShowtimesResponse>> call = apiService.getMovieShowtimes(movieId, date, cinemaId);
        
        call.enqueue(new Callback<ApiResponse<MovieShowtimesResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<MovieShowtimesResponse>> call, 
                                 Response<ApiResponse<MovieShowtimesResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    // Return simple error code for UI to handle
                    String errorMsg = String.valueOf(response.code());
                    
                    // Log detailed error for debugging
                    String detailedError = "Showtimes API error: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            detailedError += " - " + response.errorBody().string();
                        } catch (Exception e) {
                            android.util.Log.e("MovieRepository", "Error reading error body", e);
                        }
                    }
                    android.util.Log.d("MovieRepository", detailedError);
                    
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<MovieShowtimesResponse>> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                android.util.Log.e("MovieRepository", "getMovieShowtimes failure", t);
                callback.onError(errorMsg);
            }
        });
    }
    
    /**
     * Get movie reviews with average rating
     */
    public void getMovieReviews(int movieId, Integer page, Integer pageSize, String sort,
                                ApiCallback<ApiResponse<ReviewResponse>> callback) {
        Call<ApiResponse<ReviewResponse>> call = apiService.getMovieReviews(movieId, page, pageSize, sort);
        
        call.enqueue(new Callback<ApiResponse<ReviewResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReviewResponse>> call, Response<ApiResponse<ReviewResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Error: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    android.util.Log.e("MovieRepository", "getMovieReviews error: " + errorMsg);
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<ReviewResponse>> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                android.util.Log.e("MovieRepository", "getMovieReviews failure", t);
                callback.onError(errorMsg);
            }
        });
    }
    
    /**
     * Create a new review for a movie
     */
    public void createReview(com.example.cinemabookingsystemfe.data.models.request.CreateReviewRequest request,
                            ApiCallback<ApiResponse<Void>> callback) {
        Call<ApiResponse<Void>> call = apiService.createReview(request);
        
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                android.util.Log.d("MovieRepository", "createReview response code: " + response.code());
                
                if (response.isSuccessful()) {
                    android.util.Log.d("MovieRepository", "createReview successful");
                    // Even if body is null, 200/201 means success
                    if (response.body() != null) {
                        callback.onSuccess(response.body());
                    } else {
                        // Create a simple success response
                        ApiResponse<Void> successResponse = new ApiResponse<>(true, "Review created successfully", null);
                        callback.onSuccess(successResponse);
                    }
                } else {
                    String errorMsg = "Error code: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            errorMsg = "Error " + response.code() + ": " + errorBody;
                            android.util.Log.e("MovieRepository", "createReview error body: " + errorBody);
                        } catch (Exception e) {
                            android.util.Log.e("MovieRepository", "createReview error reading body", e);
                        }
                    }
                    android.util.Log.e("MovieRepository", "createReview error: " + errorMsg);
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                android.util.Log.e("MovieRepository", "createReview failure: " + errorMsg, t);
                callback.onError(errorMsg);
            }
        });
    }
}
