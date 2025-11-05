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
        
        // Movie data with real posters
        String[][] movieData = {
            {"Avengers: Endgame", "https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg"},
            {"Venom: Let There Be Carnage", "https://image.tmdb.org/t/p/w500/1MJNcPZy46hIy2CmSqOeru0yr5C.jpg"},
            {"Spider-Man: No Way Home", "https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg"},
            {"The Batman", "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg"},
            {"Doctor Strange in the Multiverse of Madness", "https://image.tmdb.org/t/p/w500/9Gtg2DzBhmYamXBS1hKAhiwbBKS.jpg"}
        };
        
        String[] cinemas = {
            "CGV Vincom Center",
            "Galaxy Nguyễn Du - Rạp 2",
            "Lotte Cinema Landmark",
            "BHD Star Cineplex",
            "Cinestar Hai Bà Trưng"
        };
        
        for (int i = 1; i <= count; i++) {
            int movieIndex = (i - 1) % movieData.length;
            int cinemaIndex = (i - 1) % cinemas.length;
            
            Booking booking = new Booking();
            booking.setBookingId(i);
            booking.setBookingCode("BK17622700263" + (590 + i));
            booking.setCustomerId(1);
            booking.setShowtimeId(100 + i);
            
            // Set movie info with real poster
            Booking.MovieInfo movie = new Booking.MovieInfo();
            movie.setMovieId(i);
            movie.setTitle(movieData[movieIndex][0]);
            movie.setPosterUrl(movieData[movieIndex][1]);
            booking.setMovie(movie);
            
            // Set cinema info
            Booking.CinemaInfo cinema = new Booking.CinemaInfo();
            cinema.setCinemaId(i);
            cinema.setName(cinemas[cinemaIndex]);
            cinema.setAddress("191 Bà Triệu, Hai Bà Trưng, Hà Nội");
            booking.setCinema(cinema);
            
            // Set showtime info with varied times
            Booking.ShowtimeInfo showtime = new Booking.ShowtimeInfo();
            int[] hours = {15, 17, 19, 21};
            int hourIndex = (i - 1) % hours.length;
            String time = String.format("2025-10-%02dT%02d:30:00", 25 + i, hours[hourIndex]);
            showtime.setStartTime(time);
            showtime.setFormat("2D");
            showtime.setLanguageType("Phụ đề");
            booking.setShowtime(showtime);
            
            // Set seats
            List<String> seats = new ArrayList<>();
            char row = (char)('A' + ((i - 1) / 2));
            seats.add(row + "" + (i * 2 - 1));
            seats.add(row + "" + (i * 2));
            booking.setSeats(seats);
            
            // Set combos
            List<Booking.ComboInfo> combos = new ArrayList<>();
            Booking.ComboInfo combo = new Booking.ComboInfo();
            combo.setName("Combo " + i + " - Bắp + Nước");
            combo.setQuantity(1);
            combo.setPrice(80000);
            combos.add(combo);
            booking.setCombos(combos);
            
            booking.setVoucherCode("PROMO2024");
            booking.setTotalAmount(200000 + (i * 20000));
            booking.setStatus(status);
            booking.setBookingTime("2025-10-25T14:30:00");
            
            bookings.add(booking);
        }
        
        return bookings;
    }
}
