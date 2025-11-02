package com.example.cinemabookingsystemfe.data.repository;

import com.example.cinemabookingsystemfe.data.model.Booking;
import com.example.cinemabookingsystemfe.network.ApiCallback;

import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    private static BookingRepository instance;
    
    private BookingRepository() {
    }
    
    public static synchronized BookingRepository getInstance() {
        if (instance == null) {
            instance = new BookingRepository();
        }
        return instance;
    }
    
    public void getMyBookings(String status, ApiCallback<List<Booking>> callback) {
        // Mock data for testing
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate network delay
                
                List<Booking> bookings = generateMockBookings(status);
                
                // Callback on main thread would be needed in real implementation
                callback.onSuccess(bookings);
            } catch (Exception e) {
                callback.onError("Lỗi khi tải lịch sử đặt vé: " + e.getMessage());
            }
        }).start();
    }
    
    private List<Booking> generateMockBookings(String status) {
        List<Booking> bookings = new ArrayList<>();
        
        if ("All".equals(status) || status == null) {
            // Generate bookings for all statuses
            bookings.addAll(createBookingsForStatus("Pending", 2));
            bookings.addAll(createBookingsForStatus("Confirmed", 2));
            bookings.addAll(createBookingsForStatus("Completed", 1));
        } else {
            bookings.addAll(createBookingsForStatus(status, 3));
        }
        
        return bookings;
    }
    
    private List<Booking> createBookingsForStatus(String status, int count) {
        List<Booking> bookings = new ArrayList<>();
        
        for (int i = 1; i <= count; i++) {
            Booking booking = new Booking();
            booking.setBookingId(i);
            booking.setBookingCode("BK" + System.currentTimeMillis() + i);
            booking.setMovieTitle("Avengers: Endgame");
            booking.setMoviePosterUrl("https://via.placeholder.com/300x450");
            booking.setCinemaName("CGV Vincom Center");
            booking.setShowtimeDate("29/10/2025");
            booking.setShowtimeTime("19:30");
            booking.setSeats("A1, A2");
            booking.setTotalPrice(240000);
            booking.setStatus(status);
            bookings.add(booking);
        }
        
        return bookings;
    }
}
