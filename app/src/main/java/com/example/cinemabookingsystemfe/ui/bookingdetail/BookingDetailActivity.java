package com.example.cinemabookingsystemfe.ui.bookingdetail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.model.Booking;
import com.example.cinemabookingsystemfe.data.repository.BookingRepository;
import com.example.cinemabookingsystemfe.network.ApiCallback;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingDetailActivity extends AppCompatActivity {
    
    private static final String TAG = "BookingDetailActivity";
    
    private ImageView ivPoster, ivQrCode;
    private TextView tvMovieTitle, tvCinema, tvShowtime, tvFormat, tvAge;
    private TextView tvBookingId, tvStars, tvTotalAmount;
    private MaterialButton btnClose;
    
    private int bookingId;
    private BookingRepository bookingRepository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        
        bookingId = getIntent().getIntExtra("BOOKING_ID", 0);
        Log.d(TAG, "onCreate: bookingId = " + bookingId);
        
        initViews();
        initRepository();
        loadBookingDetail();
    }
    
    private void initViews() {
        try {
            ivPoster = findViewById(R.id.ivPoster);
            ivQrCode = findViewById(R.id.ivQrCode);
            tvMovieTitle = findViewById(R.id.tvMovieTitle);
            tvCinema = findViewById(R.id.tvCinema);
            tvShowtime = findViewById(R.id.tvShowtime);
            tvFormat = findViewById(R.id.tvFormat);
            tvAge = findViewById(R.id.tvAge);
            tvBookingId = findViewById(R.id.tvBookingId);
            tvStars = findViewById(R.id.tvStars);
            tvTotalAmount = findViewById(R.id.tvTotalAmount);
            btnClose = findViewById(R.id.btnClose);
            
            // Check for null views
            if (ivPoster == null) Log.e(TAG, "ivPoster is null");
            if (ivQrCode == null) Log.e(TAG, "ivQrCode is null");
            if (tvMovieTitle == null) Log.e(TAG, "tvMovieTitle is null");
            if (tvCinema == null) Log.e(TAG, "tvCinema is null");
            if (tvShowtime == null) Log.e(TAG, "tvShowtime is null");
            if (tvFormat == null) Log.e(TAG, "tvFormat is null");
            if (tvAge == null) Log.e(TAG, "tvAge is null");
            if (tvBookingId == null) Log.e(TAG, "tvBookingId is null");
            if (tvStars == null) Log.e(TAG, "tvStars is null");
            if (tvTotalAmount == null) Log.e(TAG, "tvTotalAmount is null");
            if (btnClose == null) Log.e(TAG, "btnClose is null");
            
            if (btnClose != null) {
                btnClose.setOnClickListener(v -> finish());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage(), e);
        }
    }
    
    private void initRepository() {
        bookingRepository = BookingRepository.getInstance();
    }
    
    private void loadBookingDetail() {
        Log.d(TAG, "loadBookingDetail: Starting to load booking " + bookingId);
        
        // Mock: Get booking from list and find by ID
        bookingRepository.getMyBookings(null, new ApiCallback<List<Booking>>() {
            @Override
            public void onSuccess(List<Booking> bookings) {
                Log.d(TAG, "onSuccess: Received " + bookings.size() + " bookings");
                
                runOnUiThread(() -> {
                    // Find booking by ID
                    Booking foundBooking = null;
                    for (Booking booking : bookings) {
                        if (booking.getBookingId() == bookingId) {
                            foundBooking = booking;
                            Log.d(TAG, "Found booking: " + booking.getBookingCode());
                            break;
                        }
                    }
                    
                    if (foundBooking != null) {
                        displayBookingDetail(foundBooking);
                        generateQRCode(foundBooking.getBookingCode());
                    } else {
                        Log.e(TAG, "Booking not found with ID: " + bookingId);
                        Toast.makeText(BookingDetailActivity.this, 
                            "Không tìm thấy booking", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                Log.e(TAG, "onError: " + error);
                runOnUiThread(() -> {
                    Toast.makeText(BookingDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
    
    private void displayBookingDetail(Booking booking) {
        if (booking == null) {
            Log.e(TAG, "displayBookingDetail: booking is null");
            return;
        }
        
        try {
            // Movie info
            if (booking.getMovie() != null) {
                if (tvMovieTitle != null) {
                    tvMovieTitle.setText(booking.getMovie().getTitle());
                }
                if (ivPoster != null) {
                    Glide.with(this)
                        .load(booking.getMovie().getPosterUrl())
                        .placeholder(R.color.colorSurface)
                        .error(R.color.colorSurface)
                        .into(ivPoster);
                }
                if (tvFormat != null) {
                    tvFormat.setText("2D - SUB");
                }
                if (tvAge != null) {
                    tvAge.setText("C18");
                }
            }
            
            // Cinema info
            if (booking.getCinema() != null && tvCinema != null) {
                tvCinema.setText(booking.getCinema().getName());
            }
            
            // Showtime info
            if (booking.getShowtime() != null && tvShowtime != null) {
                String showtime = formatShowtime(booking.getShowtime().getStartTime());
                tvShowtime.setText("Suất " + showtime);
            }
            
            // Booking details
            if (tvBookingId != null) {
                String bookingCode = booking.getBookingCode() != null ? 
                    booking.getBookingCode() : String.valueOf(booking.getBookingId());
                tvBookingId.setText(bookingCode.replace("BK", ""));
            }
            
            // Stars (mock data)
            if (tvStars != null) {
                tvStars.setText("7");
            }
            
            // Total amount
            if (tvTotalAmount != null) {
                tvTotalAmount.setText(String.format("%,d₫", (int)booking.getTotalAmount()));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error displaying booking detail: " + e.getMessage(), e);
            runOnUiThread(() -> Toast.makeText(this, "Lỗi hiển thị thông tin: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
    
    private String formatShowtime(String datetime) {
        try {
            Date date = null;
            
            // Try multiple formats
            String[] possibleFormats = {
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "dd/MM/yyyy HH:mm"
            };
            
            for (String format : possibleFormats) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat(format, Locale.getDefault());
                    date = inputFormat.parse(datetime);
                    if (date != null) break;
                } catch (Exception ignored) {}
            }
            
            if (date != null) {
                // Output format: "19:30 - Thứ Ba, 21/07/2020"
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                
                String time = timeFormat.format(date);
                String day = dayFormat.format(date);
                String dateStr = dateFormat.format(date);
                
                // Capitalize first letter of day
                day = day.substring(0, 1).toUpperCase() + day.substring(1);
                
                return time + " - " + day + ", " + dateStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return original if parsing fails
        return datetime;
    }
    
    private void generateQRCode(String bookingCode) {
        if (bookingCode == null || bookingCode.isEmpty()) {
            Log.e(TAG, "generateQRCode: bookingCode is null or empty");
            runOnUiThread(() -> Toast.makeText(this, "Không có mã booking", Toast.LENGTH_SHORT).show());
            return;
        }
        
        if (ivQrCode == null) {
            Log.e(TAG, "generateQRCode: ivQrCode is null");
            return;
        }
        
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(bookingCode, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            runOnUiThread(() -> {
                if (ivQrCode != null) {
                    ivQrCode.setImageBitmap(bitmap);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error generating QR code: " + e.getMessage(), e);
            runOnUiThread(() -> Toast.makeText(this, "Lỗi tạo mã QR: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}
