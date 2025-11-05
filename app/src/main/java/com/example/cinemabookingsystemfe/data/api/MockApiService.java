package com.example.cinemabookingsystemfe.data.api;

import android.os.Handler;
import android.os.Looper;

import com.example.cinemabookingsystemfe.data.models.request.LoginRequest;
import com.example.cinemabookingsystemfe.data.models.request.RegisterRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.LoginResponse;
import com.example.cinemabookingsystemfe.data.models.response.MovieDetailResponse;
import com.example.cinemabookingsystemfe.data.models.response.MovieItem;
import com.example.cinemabookingsystemfe.data.models.response.RegisterResponse;
import com.example.cinemabookingsystemfe.data.models.response.SearchMovieData;
import com.example.cinemabookingsystemfe.data.models.response.User;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.Auditorium;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.CinemaShowtimes;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.Showtime;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.ShowtimesDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Mock API Service for testing without backend
 * TODO: Remove this when real backend is ready
 */
public class MockApiService {
    
    private static final int MOCK_DELAY_MS = 1000; // Simulate network delay
    
    /**
     * Mock login - accepts any username/password
     */
    public static void login(LoginRequest request, ApiCallback<ApiResponse<LoginResponse>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // NOTE: Mock disabled - using real API now
            // Mock success response with nested user structure (matching backend format)
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken("mock_jwt_token_" + System.currentTimeMillis());
            loginResponse.setRefreshToken("mock_refresh_token_" + System.currentTimeMillis());
            loginResponse.setExpiresAt("2025-12-31T23:59:59");
            
            // Create nested user data (matching backend format)
            LoginResponse.UserData userData = new LoginResponse.UserData();
            userData.setUserId(1);
            userData.setFullName("Mock User");
            userData.setEmail(request.getEmail());
            userData.setPhoneNumber("0123456789");
            userData.setRoleId(1);
            userData.setRoleName("Customer");
            loginResponse.setUser(userData);
            
            ApiResponse<LoginResponse> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Login successful");
            response.setData(loginResponse);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }
    
    /**
     * Mock register
     */
    public static void register(RegisterRequest request, ApiCallback<ApiResponse<RegisterResponse>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // NOTE: Mock disabled - using real API now
            // Mock success response with updated request fields
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setUserId(2);
            registerResponse.setFullname(request.getFullname());
            registerResponse.setEmail(request.getEmail());
            registerResponse.setPhone(request.getPhone());
            registerResponse.setRoleName("Customer");
            
            ApiResponse<RegisterResponse> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Registration successful");
            response.setData(registerResponse);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }
    
    /**
     * Mock forgot password - always success
     */
    public static void forgotPassword(String email, ApiCallback<ApiResponse<Void>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Đã gửi email hướng dẫn đặt lại mật khẩu đến " + email);
            response.setData(null);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }
    
    /**
     * Mock logout - always success
     */
    public static void logout(ApiCallback<ApiResponse<Void>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Đăng xuất thành công");
            response.setData(null);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }



    public static void getMovieDetail(int movieId, ApiCallback<MovieDetailResponse> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // ✅ Dữ liệu mock giống hệt JSON backend
            String releaseDateString = "2012-05-04";
            Date releaseDate;
            try {
                releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(releaseDateString);
            } catch (ParseException e) {
                releaseDate = new Date();
            }

            MovieDetailResponse mockMovie = new MovieDetailResponse();
            mockMovie.setMovieId(1);
            mockMovie.setTitle("The Avengers");
            mockMovie.setOverview("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.");
            mockMovie.setDuration(143);
            mockMovie.setDirector("Joss Whedon");
            mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=eOrNdBpGMv8");
            mockMovie.setReleaseDate(releaseDate);
            mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/RYMX2wcKCBAr24UyPD7xwmjaTn.jpg");
            mockMovie.setCountry("USA");
            mockMovie.setAgeRating("PG-13");
            mockMovie.setGenre("Action, Sci-Fi, Adventure");
            mockMovie.setRating(4.5);
            mockMovie.setTotalReviews(128);
            mockMovie.setTotalShowtimes(15);
            mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/bOGkgRGdhrBYJSLpXaxhXVstddV.jpg");

            callback.onSuccess(mockMovie);

        }, 800); // Giả lập delay ~800ms
    }


    public static void searchMovies(int page, int pageSize, String genre, String sort,
                                    ApiCallback<ApiResponse<SearchMovieData>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // Mock data sẵn, đã sắp xếp theo releasedate_desc
            List<MovieItem> allMovies = new ArrayList<>();
            allMovies.add(new MovieItem(1, "The Avengers",
                    "Earth's mightiest heroes must come together...",
                    143, "Joss Whedon",
                    "https://www.youtube.com/watch?v=eOrNdBpGMv8",
                    "2012-05-04",
                    "https://image.tmdb.org/t/p/w500/RYMX2wcKCBAr24UyPD7xwmjaTn.jpg",
                    "USA", "4.5", "Action, Sci-Fi, Adventure"));
            allMovies.add(new MovieItem(2, "Inception",
                    "A thief who steals corporate secrets through dream-sharing technology...",
                    148, "Christopher Nolan",
                    "https://www.youtube.com/watch?v=YoHD9XEInc0",
                    "2010-07-16",
                    "https://image.tmdb.org/t/p/w500/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg",
                    "USA", "4.5", "Action, Sci-Fi, Thriller"));
            allMovies.add(new MovieItem(3, "Avatar",
                    "A paraplegic Marine dispatched to the moon Pandora...",
                    162, "James Cameron",
                    "https://www.youtube.com/watch?v=5PSNL1qE6VY",
                    "2009-12-18",
                    "https://image.tmdb.org/t/p/w500/6EiRUJpuoeQPghrs3YNktfnqOVh.jpg",
                    "USA", "5.1", "Action, Adventure, Fantasy"));
            // ... thêm nhiều mock movie nếu muốn

            // Paging
            int totalItems = allMovies.size();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, totalItems);
            List<MovieItem> paged = start < totalItems ? allMovies.subList(start, end) : new ArrayList<>();

            // Gói vào SearchMovieData
            SearchMovieData data = new SearchMovieData(paged, page, pageSize, totalPages, totalItems,
                    page < totalPages, page > 1);

            // Gói vào ApiResponse
            ApiResponse<SearchMovieData> response = new ApiResponse<>(true, "Movies retrieved successfully", data);

            // Callback
            callback.onSuccess(response);

        }, MOCK_DELAY_MS);
    }

    public static void getMovieShowtimes(int movieId, ApiCallback<ApiResponse<List<ShowtimesDate>>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                // --- Auditorium ---
                Auditorium auditorium1 = new Auditorium(5, "Cinema 3", 150);
                Auditorium auditorium2 = new Auditorium(6, "Cinema 4", 200);

                // --- Showtime list (2 suất chiếu) ---
                List<Showtime> showtimes = Arrays.asList(
                        new Showtime(
                                42,
                                "2025-11-05T10:30:00",
                                "2025-11-05T12:53:00",
                                100000,
                                "2D",
                                "Phụ đề",
                                auditorium1,
                                145
                        ),
                        new Showtime(
                                43,
                                "2025-11-05T14:00:00",
                                "2025-11-05T16:23:00",
                                120000,
                                "3D",
                                "Lồng tiếng",
                                auditorium2,
                                198
                        )
                );

                // --- Cinema chứa các suất chiếu ---
                CinemaShowtimes cinema = new CinemaShowtimes(
                        1,
                        "CGV Vincom Center",
                        "72 Le Thanh Ton, District 1",
                        "Ho Chi Minh City",
                        showtimes
                );

                // --- Date group (có thể có nhiều ngày sau này) ---
                ShowtimesDate showtimesDate = new ShowtimesDate(
                        "2025-11-05",
                        Collections.singletonList(cinema)
                );

                // --- List wrap (chuẩn cấu trúc API backend) ---
                List<ShowtimesDate> data = Collections.singletonList(showtimesDate);

                // --- Response ---
                ApiResponse<List<ShowtimesDate>> response = new ApiResponse<>(
                        true,
                        "Showtimes retrieved successfully",
                        data
                );

                callback.onSuccess(response);

            } catch (Exception e) {
                callback.onError("Mock showtimes error: " + e.getMessage());
            }
        }, 1000); // delay 1s cho giống gọi API thật
    }

}
