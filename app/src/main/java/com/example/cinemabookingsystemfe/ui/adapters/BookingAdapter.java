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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        TextView tvFormat, tvAge;
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
            tvFormat = itemView.findViewById(R.id.tvFormat);
            tvAge = itemView.findViewById(R.id.tvAge);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            btnAction = itemView.findViewById(R.id.btnAction);
            
            // Click whole card to view details
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBookingClick(bookings.get(position));
                }
            });
        }
        
        void bind(Booking booking) {
            tvBookingId.setText("#" + booking.getBookingCode());
            
            // Access nested objects
            if (booking.getMovie() != null) {
                tvMovieTitle.setText(booking.getMovie().getTitle());
                
                // Load poster
                Glide.with(ivPoster.getContext())
                    .load(booking.getMovie().getPosterUrl())
                    .into(ivPoster);
                
                // Set format (2D/3D) and age rating
                tvFormat.setText("2D - SUB"); // TODO: Get from movie data
                tvAge.setText("C18"); // TODO: Get from movie data
            }
            
            if (booking.getCinema() != null) {
                tvCinema.setText(booking.getCinema().getName());
            }
            
            if (booking.getShowtime() != null) {
                String showtime = booking.getShowtime().getStartTime();
                tvShowtime.setText(formatShowtime(showtime));
            }
            
            tvSeats.setText("Ghế: " + booking.getSeatsDisplay());
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
    }
}
