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
import com.example.cinemabookingsystemfe.data.models.response.BookingListResponse;
import com.google.android.material.chip.Chip;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    
    private List<BookingListResponse> bookings;
    private OnBookingClickListener listener;
    
    public interface OnBookingClickListener {
        void onBookingClick(BookingListResponse booking);
    }
    
    public BookingAdapter(OnBookingClickListener listener) {
        this.bookings = new ArrayList<>();
        this.listener = listener;
    }
    
    public void setBookings(List<BookingListResponse> bookings) {
        this.bookings = bookings != null ? bookings : new ArrayList<>();
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
        ImageView ivPoster;
        TextView tvMovieTitle, tvFormat, tvCinemaName, tvShowtime, tvSeats, tvTotalAmount;
        Chip chipStatus;
        
        BookingViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvFormat = itemView.findViewById(R.id.tvFormat);
            tvCinemaName = itemView.findViewById(R.id.tvCinemaName);
            tvShowtime = itemView.findViewById(R.id.tvShowtime);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            chipStatus = itemView.findViewById(R.id.chipStatus);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBookingClick(bookings.get(position));
                }
            });
        }
        
        void bind(BookingListResponse booking) {
            // Movie title
            if (booking.getMovie() != null) {
                tvMovieTitle.setText(booking.getMovie().getTitle());
                
                // Load poster
                Glide.with(ivPoster.getContext())
                    .load(booking.getMovie().getPosterUrl())
                    .placeholder(R.drawable.ic_empty_bookings)
                    .error(R.drawable.ic_empty_bookings)
                    .into(ivPoster);
            }
            
            // Format & Language Type
            if (booking.getShowtime() != null) {
                String format = booking.getShowtime().getFormat() != null ? booking.getShowtime().getFormat() : "";
                String lang = booking.getShowtime().getLanguageType() != null ? booking.getShowtime().getLanguageType() : "";
                tvFormat.setText(format + (lang.isEmpty() ? "" : " • " + lang));
                
                // Showtime
                tvShowtime.setText(formatShowtime(booking.getShowtime().getStartTime()));
            }
            
            // Cinema name
            if (booking.getCinema() != null) {
                tvCinemaName.setText(booking.getCinema().getName());
            }
            
            // Seats
            if (booking.getSeats() != null && !booking.getSeats().isEmpty()) {
                tvSeats.setText("Ghế: " + String.join(", ", booking.getSeats()));
            }
            
            // Total amount
            DecimalFormat formatter = new DecimalFormat("#,###");
            tvTotalAmount.setText(formatter.format(booking.getTotalAmount()) + "đ");
            
            // Status
            setStatusChip(booking.getStatus());
        }
        
        private String formatShowtime(String startTime) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());
                Date date = inputFormat.parse(startTime);
                return outputFormat.format(date);
            } catch (ParseException e) {
                return startTime;
            }
        }
        
        private void setStatusChip(String status) {
            int colorRes;
            String statusText;
            
            switch (status) {
                case "Pending":
                    statusText = "Chờ xử lý";
                    colorRes = R.color.statusPending;
                    break;
                case "Confirmed":
                    statusText = "Đã xác nhận";
                    colorRes = R.color.colorPrimary;
                    break;
                case "CheckedIn":
                    statusText = "Đã check-in";
                    colorRes = R.color.statusSuccess;
                    break;
                case "Completed":
                    statusText = "Hoàn thành";
                    colorRes = R.color.statusSuccess;
                    break;
                case "Cancelled":
                    statusText = "Đã hủy";
                    colorRes = R.color.statusCancelled;
                    break;
                default:
                    statusText = status;
                    colorRes = R.color.textSecondary;
            }
            
            chipStatus.setText(statusText);
            chipStatus.setChipBackgroundColorResource(colorRes);
        }
    }
}
