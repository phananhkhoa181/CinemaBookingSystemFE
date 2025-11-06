package com.example.cinemabookingsystemfe.ui.booking;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.BookingDetail;
import com.example.cinemabookingsystemfe.data.models.response.BookingListResponse;
import com.example.cinemabookingsystemfe.data.repository.BookingRepository;
import com.google.android.material.button.MaterialButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class BookingDetailActivity extends AppCompatActivity {
    
    public static final String EXTRA_BOOKING_DATA = "booking_data";
    public static final String EXTRA_BOOKING_ID = "booking_id";  // Keep for backward compatibility
    
    private ImageView ivMoviePoster, ivQRCode;
    private TextView tvMovieTitle, tvFormat, tvCinemaName, tvShowtime;
    private TextView tvSeatsLabel, tvBookingCode, tvAuditorium, tvTotalPrice;
    private MaterialButton btnClose;
    
    private BookingRepository bookingRepository;
    private BookingListResponse bookingData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        
        // Get booking data from Intent
        bookingData = (BookingListResponse) getIntent().getSerializableExtra(EXTRA_BOOKING_DATA);
        
        if (bookingData == null) {
            Toast.makeText(this, "Không tìm thấy thông tin đặt vé", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initViews();
        setupListeners();
        displayBookingData();
    }
    
    private void initViews() {
        ivMoviePoster = findViewById(R.id.ivMoviePoster);
        ivQRCode = findViewById(R.id.ivQRCode);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvFormat = findViewById(R.id.tvFormat);
        tvCinemaName = findViewById(R.id.tvCinemaName);
        tvShowtime = findViewById(R.id.tvShowtime);
        tvSeatsLabel = findViewById(R.id.tvSeatsLabel);
        tvBookingCode = findViewById(R.id.tvBookingCode);
        tvAuditorium = findViewById(R.id.tvAuditorium);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnClose = findViewById(R.id.btnClose);
    }
    
    private void displayBookingData() {
        // Movie info
        if (bookingData.getMovie() != null) {
            tvMovieTitle.setText(bookingData.getMovie().getTitle());
            
            // Load poster
            Glide.with(this)
                    .load(bookingData.getMovie().getPosterUrl())
                    .placeholder(R.drawable.ic_empty_bookings)
                    .error(R.drawable.ic_empty_bookings)
                    .into(ivMoviePoster);
        }
        
        // Showtime info
        if (bookingData.getShowtime() != null) {
            String format = bookingData.getShowtime().getFormat() + " • " + 
                          bookingData.getShowtime().getLanguageType();
            tvFormat.setText(format);
            
            // Format showtime with day of week
            String showtime = formatShowtimeWithDayOfWeek(bookingData.getShowtime().getStartTime());
            tvShowtime.setText(showtime);
            
            // Display auditorium name from response (if available)
            if (bookingData.getShowtime().getAuditoriumName() != null && 
                !bookingData.getShowtime().getAuditoriumName().isEmpty()) {
                tvAuditorium.setText(bookingData.getShowtime().getAuditoriumName());
            } else {
                // Fallback: extract row from first seat if auditoriumName not available
                if (bookingData.getSeats() != null && !bookingData.getSeats().isEmpty()) {
                    String firstSeat = bookingData.getSeats().get(0);
                    if (firstSeat != null && firstSeat.contains(" ")) {
                        String row = firstSeat.split(" ")[0];
                        tvAuditorium.setText(row);
                    }
                }
            }
        }
        
        // Cinema info
        if (bookingData.getCinema() != null) {
            tvCinemaName.setText(bookingData.getCinema().getName());
        }
        
        // Seats - seats are List<String> like ["F 6", "F 5"]
        if (bookingData.getSeats() != null && !bookingData.getSeats().isEmpty()) {
            String seats = String.join(", ", bookingData.getSeats());
            tvSeatsLabel.setText("Ghế: " + seats);
        }
        
        // Booking code from backend
        String bookingCode = bookingData.getBookingCode();
        if (bookingCode == null || bookingCode.isEmpty()) {
            bookingCode = "BK" + String.format("%06d", bookingData.getBookingId());
        }
        tvBookingCode.setText(bookingCode);
        
        // Generate QR Code from booking code
        generateQRCode(bookingCode);
        
        // Total price
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText(formatter.format(bookingData.getTotalAmount()) + "đ");
    }
    
    private String formatShowtime(String isoDateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(isoDateTime);
            return "Suất " + outputFormat.format(date);
        } catch (Exception e) {
            return isoDateTime;
        }
    }
    
    private String formatShowtimeWithDayOfWeek(String isoDateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(isoDateTime);
            
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            
            String time = timeFormat.format(date);
            String dayOfWeek = dayFormat.format(date);
            // Capitalize first letter
            dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1);
            String dateStr = dateFormat.format(date);
            
            return "Suất " + time + " - " + dayOfWeek + " - " + dateStr;
        } catch (Exception e) {
            return isoDateTime;
        }
    }
    
    private void generateQRCode(String content) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            
            ivQRCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            android.util.Log.e("BookingDetail", "Error generating QR code", e);
        }
    }
    
    private void setupListeners() {
        btnClose.setOnClickListener(v -> finish());
    }
}
