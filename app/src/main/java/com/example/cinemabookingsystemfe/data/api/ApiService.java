package com.example.cinemabookingsystemfe.data.api;

import com.example.cinemabookingsystemfe.data.model.Booking;
import com.example.cinemabookingsystemfe.data.models.request.*;
import com.example.cinemabookingsystemfe.data.models.response.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * ApiService - Authentication APIs Only
 * 
 * Currently implementing:
 * - Login
 * - Register
 * - Logout
 * - Forgot Password
 * 
 * Using MockApiService for now (no backend needed)
 * TODO: Implement other modules (Movies, Bookings, etc.) later
 */
public interface ApiService {
    
    // ====================================
    // AUTHENTICATION APIs
    // ====================================
    
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);  // Backend returns LoginResponse directly (not wrapped)
    
    @POST("api/auth/register")
    Call<ApiResponse<RegisterResponse>> register(@Body RegisterRequest request);
    
    @POST("api/auth/logout")
    Call<ApiResponse<Void>> logout();
    
    @POST("api/auth/forgot-password")
    Call<ApiResponse<SendOtpResponse>> forgotPassword(@Body ForgotPasswordRequest request);
    
    // OTP Verification APIs
    @POST("api/auth/send-otp")
    Call<ApiResponse<SendOtpResponse>> sendOtp(@Body SendOtpRequest request);
    
    @POST("api/auth/verify-otp")
    Call<ApiResponse<VerifyOtpResponse>> verifyOtp(@Body VerifyOtpRequest request);
    
    @POST("api/auth/resend-otp")
    Call<ApiResponse<SendOtpResponse>> resendOtp(@Body ResendOtpRequest request);
    
    // Password Reset API
    @POST("api/auth/reset-password")
    Call<ApiResponse<ResetPasswordResponse>> resetPassword(@Body ResetPasswordRequest request);
    
    // Token Refresh API
    @POST("api/auth/refresh-token")
    Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest request);
    
    // ====================================
    // MOVIES APIs
    // ====================================
    
    // Get all movies with filters and pagination
    @GET("api/movies")
    Call<ApiResponse<PagedResult<Movie>>> getMovies(
            @Query("page") Integer page,
            @Query("pageSize") Integer pageSize,
            @Query("genre") String genre,
            @Query("year") Integer year,
            @Query("rating") String rating,
            @Query("sort") String sort
    );
    
    // Get now showing movies
    @GET("api/movies/now-showing")
    Call<ApiResponse<PagedResult<Movie>>> getNowShowingMovies(
            @Query("page") Integer page,
            @Query("pageSize") Integer pageSize
    );
    
    // Get coming soon movies
    @GET("api/movies/coming-soon")
    Call<ApiResponse<PagedResult<Movie>>> getComingSoonMovies(
            @Query("page") Integer page,
            @Query("pageSize") Integer pageSize
    );
    
    // Search movies
    @GET("api/movies/search")
    Call<ApiResponse<PagedResult<Movie>>> searchMovies(
            @Query("query") String query,
            @Query("page") Integer page,
            @Query("pageSize") Integer pageSize
    );
    
    // Get movie details by ID
    @GET("api/movies/{id}")
    Call<ApiResponse<MovieDetail>> getMovieById(@Path("id") int movieId);
    
    // Get movie showtimes (returns movie info + showtimes grouped by date)
    @GET("api/movies/{id}/showtimes")
    Call<ApiResponse<MovieShowtimesResponse>> getMovieShowtimes(
            @Path("id") int movieId,
            @Query("date") String date,
            @Query("cinemaid") Integer cinemaId
    );
    
    // ====================================
    // REVIEWS APIs
    // ====================================
    
    // Get movie reviews with average rating
    @GET("api/Reviews/movie/{movieId}")
    Call<ApiResponse<ReviewResponse>> getMovieReviews(
            @Path("movieId") int movieId,
            @Query("page") Integer page,
            @Query("pageSize") Integer pageSize,
            @Query("sort") String sort
    );
    
    // ====================================
    // BOOKINGS APIs
    // ====================================
    
    // Get my bookings (requires authentication)
    @GET("api/bookings/my-bookings")
    Call<ApiResponse<PaginatedBookingResponse>> getMyBookings(
            @Query("page") Integer page,
            @Query("pageSize") Integer pageSize,
            @Query("status") String status
    );
    
    // Get booking detail by ID (requires authentication)
    @GET("api/bookings/{id}")
    Call<ApiResponse<BookingDetail>> getBookingById(@Path("id") int bookingId);
    
    // Create booking (requires authentication)
    @POST("api/bookings/create")
    Call<ApiResponse<CreateBookingResponse>> createBooking(@Body CreateBookingRequest request);
    
    // Add combos to booking (requires authentication)
    @POST("api/bookings/{id}/add-combos")
    Call<ApiResponse<AddCombosResponse>> addCombosToBooking(
        @Path("id") int bookingId,
        @Body AddCombosRequest request
    );
    
    // Apply voucher to booking (requires authentication)
    @POST("api/bookings/{id}/apply-voucher")
    Call<ApiResponse<ApplyVoucherResponse>> applyVoucher(
        @Path("id") int bookingId,
        @Body ApplyVoucherRequest request
    );
    
    // Cancel booking (requires authentication)
    @POST("api/bookings/{id}/cancel")
    Call<ApiResponse<Void>> cancelBooking(@Path("id") int bookingId);
    
    // ====================================
    // PAYMENT APIs (VNPay)
    // ====================================
    
    // Create VNPay payment (requires authentication)
    @POST("api/payments/vnpay/create")
    Call<ApiResponse<CreatePaymentResponse>> createVNPayPayment(@Body CreatePaymentRequest request);
    
    // Get payment details (requires authentication)
    @GET("api/payments/{id}")
    Call<ApiResponse<PaymentDetailResponse>> getPaymentDetails(@Path("id") int paymentId);
    
    // ====================================
    // COMBO APIs
    // ====================================
    
    // Get all combos (no authentication required)
    @GET("api/combos")
    Call<ApiResponse<List<Combo>>> getCombos();
    
    // ====================================
    // AUDITORIUM APIs
    // ====================================
    
    // Get seats for an auditorium with showtime availability
    @GET("api/auditoriums/{id}/seats")
    Call<ApiResponse<AuditoriumSeatsResponse>> getAuditoriumSeats(
        @Path("id") int auditoriumId,
        @Query("showtimeId") int showtimeId
    );
    
    // ====================================
    // CUSTOMERS APIs
    // ====================================
    
    // Get customer profile (requires authentication)
    @GET("api/customers/profile")
    Call<ApiResponse<CustomerProfile>> getCustomerProfile();
}
