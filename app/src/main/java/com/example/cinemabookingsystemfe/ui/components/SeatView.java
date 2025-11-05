package com.example.cinemabookingsystemfe.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.Seat;

/**
 * SeatView - Reusable component for individual seat
 * Used in SelectSeatActivity seat map grid
 * 
 * Features:
 * - Displays seat number
 * - Shows seat status (Available, Selected, Occupied, VIP, Couple)
 * - Click handling for seat selection
 * - Auto background color based on status
 */
public class SeatView extends FrameLayout {
    
    private TextView tvSeatNumber;
    private Seat seat;
    private OnSeatClickListener listener;
    
    public interface OnSeatClickListener {
        void onSeatClick(Seat seat);
    }
    
    public SeatView(Context context) {
        super(context);
        init(context);
    }
    
    public SeatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    public SeatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    private void init(Context context) {
        LayoutInflater.from(context).inflate(
            R.layout.component_seat_view, this, true
        );
        
        tvSeatNumber = findViewById(R.id.tvSeatNumber);
    }
    
    /**
     * Bind seat data and update UI
     */
    public void bind(Seat seat) {
        this.seat = seat;
        
        // Set seat number
        tvSeatNumber.setText(seat.getSeatNumber());
        
        // Update background based on status
        updateSeatBackground();
        
        // Click listener (only for available seats)
        setOnClickListener(v -> {
            if (seat.isAvailable() && listener != null) {
                seat.setSelected(!seat.isSelected());
                updateSeatBackground();
                listener.onSeatClick(seat);
            }
        });
        
        // Disable click for occupied seats
        setClickable(seat.isAvailable());
        setEnabled(seat.isAvailable());
    }
    
    /**
     * Update seat background color based on status
     */
    private void updateSeatBackground() {
        int backgroundRes;
        
        if (seat.isSelected()) {
            // User selected this seat
            backgroundRes = R.drawable.bg_seat_selected;
        } else if (seat.isOccupied()) {
            // Seat already booked
            backgroundRes = R.drawable.bg_seat_occupied;
        } else if (seat.isVIP()) {
            // VIP seat (available)
            backgroundRes = R.drawable.bg_seat_vip;
        } else {
            // Standard available seat
            backgroundRes = R.drawable.bg_seat_available;
        }
        
        tvSeatNumber.setBackgroundResource(backgroundRes);
    }
    
    public void setOnSeatClickListener(OnSeatClickListener listener) {
        this.listener = listener;
    }
    
    public Seat getSeat() {
        return seat;
    }
    
    /**
     * Programmatically update selection state
     */
    public void setSelected(boolean selected) {
        if (seat != null) {
            seat.setSelected(selected);
            updateSeatBackground();
        }
    }
}
