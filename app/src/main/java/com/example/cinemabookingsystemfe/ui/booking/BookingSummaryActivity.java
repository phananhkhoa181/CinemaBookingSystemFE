package com.example.cinemabookingsystemfe.ui.booking;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemabookingsystemfe.R;

/**
 * BookingSummaryActivity - Xác nhận thông tin booking và thanh toán
 * 
 * TODO: Implement full functionality according to docs-FE/03-Screens/03-Booking.md
 * - Hiển thị tóm tắt: Movie, Cinema, Showtime, Seats, Combos
 * - Áp dụng mã voucher
 * - Price breakdown: Subtotal, Discount, Total
 * - Countdown timer tiếp tục từ SelectSeatActivity
 * - Button thanh toán → Navigate to VNPayWebViewActivity
 * 
 * REFER TO: docs-FE/03-Screens/03-Booking.md (Line 1600-2200)
 */
public class BookingSummaryActivity extends AppCompatActivity {

    public static final String EXTRA_BOOKING_ID = "booking_id";

    private int bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // TODO: Create layout: activity_booking_summary.xml
        setContentView(R.layout.activity_main); // Placeholder
        
        bookingId = getIntent().getIntExtra(EXTRA_BOOKING_ID, 0);
        
        Toast.makeText(this, 
            "BookingSummaryActivity opened!\nBooking ID: " + bookingId + 
            "\n\nTODO: Implement booking summary UI", 
            Toast.LENGTH_LONG).show();
        
        // TODO: Implement:
        // - Timer warning
        // - Movie info card
        // - Seats info card
        // - Combos info card (optional)
        // - Voucher input với validate
        // - Price summary card
        // - Payment button
        // - Navigate to VNPayWebViewActivity
    }
}
