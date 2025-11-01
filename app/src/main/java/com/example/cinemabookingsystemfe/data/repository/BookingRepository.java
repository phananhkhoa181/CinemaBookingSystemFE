package com.example.cinemabookingsystemfe.data.repository;

import android.content.Context;

import com.example.cinemabookingsystemfe.data.api.ApiClient;
import com.example.cinemabookingsystemfe.data.api.ApiService;

/**
 * BookingRepository - Xử lý booking logic
 * TODO: Implement createBooking, getBookings, reserveSeats
 * 
 * ASSIGNED TO: Developer 3
 * PRIORITY: HIGH
 * DEADLINE: Week 5-6
 * 
 * Refer to: docs-FE/03-Screens/03-Booking.md
 */
public class BookingRepository {
    
    private static BookingRepository instance;
    private final ApiService apiService;
    
    private BookingRepository(Context context) {
        apiService = ApiClient.getInstance(context).getApiService();
    }
    
    public static synchronized BookingRepository getInstance(Context context) {
        if (instance == null) {
            instance = new BookingRepository(context);
        }
        return instance;
    }
    
    // TODO: Implement methods
    // - getShowtimes(movieId, cinemaId, date, callback)
    // - getShowtimeSeats(showtimeId, callback)
    // - reserveSeats(showtimeId, seatIds, callback)
    // - createBooking(request, callback)
    // - getMyBookings(status, page, pageSize, callback)
    // - getBookingById(bookingId, callback)
}
