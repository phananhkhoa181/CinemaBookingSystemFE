package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;
import com.example.cinemabookingsystemfe.data.models.request.CreateBookingRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.AvailableSeatsResponse;
import com.example.cinemabookingsystemfe.data.models.response.CreateBookingResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ShowtimeRepository - Handles showtime and seat selection APIs
 */
public class ShowtimeRepository {
    
    private static ShowtimeRepository instance;
    private final ApiService apiService;
    private final Context context;
    
    private ShowtimeRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = ApiClient.getInstance(context).getApiService();
    }
    
    public static synchronized ShowtimeRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ShowtimeRepository(context);
        }
        return instance;
    }
    
    /**
     * Get seats for an auditorium with showtime availability
     */
    public void getAuditoriumSeats(int auditoriumId, int showtimeId, 
                                  ApiCallback<ApiResponse<com.example.cinemabookingsystemfe.data.models.response.AuditoriumSeatsResponse>> callback) {
        Call<ApiResponse<com.example.cinemabookingsystemfe.data.models.response.AuditoriumSeatsResponse>> call = 
            apiService.getAuditoriumSeats(auditoriumId, showtimeId);
        
        call.enqueue(new Callback<ApiResponse<com.example.cinemabookingsystemfe.data.models.response.AuditoriumSeatsResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<com.example.cinemabookingsystemfe.data.models.response.AuditoriumSeatsResponse>> call, 
                                 Response<ApiResponse<com.example.cinemabookingsystemfe.data.models.response.AuditoriumSeatsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Error " + response.code();
                    android.util.Log.d("ShowtimeRepository", "Auditorium seats error: " + errorMsg);
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<com.example.cinemabookingsystemfe.data.models.response.AuditoriumSeatsResponse>> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                android.util.Log.e("ShowtimeRepository", "getAuditoriumSeats failure", t);
                callback.onError(errorMsg);
            }
        });
    }
    
    /**
     * Create a new booking (requires authentication)
     */
    public void createBooking(CreateBookingRequest request, ApiCallback<ApiResponse<CreateBookingResponse>> callback) {
        Call<ApiResponse<CreateBookingResponse>> call = apiService.createBooking(request);
        
        call.enqueue(new Callback<ApiResponse<CreateBookingResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CreateBookingResponse>> call, 
                                 Response<ApiResponse<CreateBookingResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = String.valueOf(response.code());
                    
                    // Log detailed error
                    String detailedError = "Create booking error: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            detailedError += " - " + response.errorBody().string();
                        } catch (Exception e) {
                            android.util.Log.e("ShowtimeRepository", "Error reading error body", e);
                        }
                    }
                    android.util.Log.d("ShowtimeRepository", detailedError);
                    
                    callback.onError(errorMsg);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<CreateBookingResponse>> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                android.util.Log.e("ShowtimeRepository", "createBooking failure", t);
                callback.onError(errorMsg);
            }
        });
    }
}
