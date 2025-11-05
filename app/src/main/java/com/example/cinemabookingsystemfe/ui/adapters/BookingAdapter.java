package com.example.cinemabookingsystemfe.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.model.Booking;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    
    private List<Booking> bookings;
    private OnBookingClickListener listener;
    
    public interface OnBookingClickListener {
        void onBookingClick(Booking booking);
    }
    
    public BookingAdapter(OnBookingClickListener listener) {
        this.bookings = new ArrayList<>();
        this.listener = listener;
    }
    
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        holder.bind(bookings.get(position));
    }
    
    @Override
    public int getItemCount() {
        return bookings.size();
    }
    
    class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingId, tvStatus, tvMovieTitle, tvCinema, tvShowtime, tvSeats, tvTotalPrice;
        ImageView ivPoster;
        MaterialButton btnAction;
        
        BookingViewHolder(View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvCinema = itemView.findViewById(R.id.tvCinema);
            tvShowtime = itemView.findViewById(R.id.tvShowtime);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            btnAction = itemView.findViewById(R.id.btnAction);
            
            btnAction.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBookingClick(bookings.get(position));
                }
            });
        }
        
        void bind(Booking booking) {
            tvBookingId.setText("#" + booking.getBookingCode());
            tvMovieTitle.setText(booking.getMovieTitle());
            tvCinema.setText(booking.getCinemaName());
            tvShowtime.setText(booking.getShowtimeFormatted());
            tvSeats.setText("Ghế: " + booking.getSeatsString());
            tvTotalPrice.setText(booking.getTotalPriceFormatted());
            
            // Set status badge
            setStatusBadge(booking.getStatus());
            
            // Load poster
            Glide.with(ivPoster.getContext())
                .load(booking.getMoviePosterUrl())
                .into(ivPoster);
                
            // Set action button based on status
            setActionButton(booking.getStatus());
        }
        
        private void setStatusBadge(String status) {
            int backgroundRes;
            String statusText;
            
            switch (status) {
                case "Pending":
                    backgroundRes = R.drawable.bg_status_pending;
                    statusText = "Chờ thanh toán";
                    break;
                case "Confirmed":
                    backgroundRes = R.drawable.bg_status_confirmed;
                    statusText = "Đã xác nhận";
                    break;
                case "Completed":
                    backgroundRes = R.drawable.bg_status_completed;
                    statusText = "Hoàn thành";
                    break;
                case "Cancelled":
                    backgroundRes = R.drawable.bg_status_cancelled;
                    statusText = "Đã hủy";
                    break;
                default:
                    backgroundRes = R.drawable.bg_status_pending;
                    statusText = status;
            }
            
            tvStatus.setBackgroundResource(backgroundRes);
            tvStatus.setText(statusText);
        }
        
        private void setActionButton(String status) {
            if ("Pending".equals(status)) {
                btnAction.setText("Thanh toán");
                btnAction.setVisibility(View.VISIBLE);
            } else if ("Confirmed".equals(status)) {
                btnAction.setText("Xem vé");
                btnAction.setVisibility(View.VISIBLE);
            } else {
                btnAction.setText("Xem chi tiết");
                btnAction.setVisibility(View.VISIBLE);
            }
        }
    }
}
