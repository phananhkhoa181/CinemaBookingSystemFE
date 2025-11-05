package com.example.cinemabookingsystemfe.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private List<Seat> seats = new ArrayList<>();
    private OnSeatClickListener listener;

    public interface OnSeatClickListener {
        void onSeatClick(Seat seat, int position);
    }

    public SeatAdapter(OnSeatClickListener listener) {
        this.listener = listener;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seats.get(position);
        holder.bind(seat, position, listener);
    }

    @Override
    public int getItemCount() {
        return seats.size();
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSeatNumber;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeatNumber = itemView.findViewById(R.id.tvSeatNumber);
        }

        public void bind(Seat seat, int position, OnSeatClickListener listener) {
            // Display as "A1", "B2", "C3" format (row first, then column)
            tvSeatNumber.setText(seat.getRow() + seat.getColumn());

            // Set background based on status and type
            int backgroundRes;
            int textColor;

            if (seat.isSelected()) {
                // Selected state
                backgroundRes = R.drawable.bg_seat_selected;
                textColor = R.color.white;
            } else if ("OCCUPIED".equals(seat.getStatus()) || "Occupied".equals(seat.getStatus())) {
                // Occupied (already sold)
                backgroundRes = R.drawable.bg_seat_occupied;
                textColor = R.color.textSecondary;
            } else {
                // Available - show by type
                if ("VIP".equalsIgnoreCase(seat.getType())) {
                    backgroundRes = R.drawable.bg_seat_vip;
                    textColor = R.color.textPrimary;
                } else if ("COUPLE".equalsIgnoreCase(seat.getType()) || "Couple".equals(seat.getType())) {
                    backgroundRes = R.drawable.bg_seat_couple;
                    textColor = R.color.textPrimary;
                } else {
                    // Standard
                    backgroundRes = R.drawable.bg_seat_available;
                    textColor = R.color.textPrimary;
                }
            }

            tvSeatNumber.setBackgroundResource(backgroundRes);
            tvSeatNumber.setTextColor(ContextCompat.getColor(itemView.getContext(), textColor));

            // Click listener - đặt trên TextView chứ không phải itemView
            tvSeatNumber.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSeatClick(seat, position);
                }
            });
        }
    }
}
