package com.example.cinemabookingsystemfe.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.ShowtimesByDate;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Adapter for displaying cinemas with showtimes from API
 */
public class SelectCinemaAdapter extends RecyclerView.Adapter<SelectCinemaAdapter.CinemaViewHolder> {
    
    private List<ShowtimesByDate.ShowtimesByCinema> cinemas;
    private OnShowtimeClickListener listener;
    private int expandedPosition = -1; // Track which item is expanded
    
    public interface OnShowtimeClickListener {
        void onShowtimeClick(ShowtimesByDate.ShowtimeItem showtime, ShowtimesByDate.ShowtimesByCinema cinema);
    }
    
    public SelectCinemaAdapter(List<ShowtimesByDate.ShowtimesByCinema> cinemas, OnShowtimeClickListener listener) {
        this.cinemas = cinemas;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_cinema_showtime, parent, false);
        return new CinemaViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CinemaViewHolder holder, int position) {
        ShowtimesByDate.ShowtimesByCinema cinema = cinemas.get(position);
        boolean isExpanded = position == expandedPosition;
        holder.bind(cinema, listener, isExpanded, position, (clickedPosition) -> {
            // Store previous state
            int previousExpandedPosition = expandedPosition;
            
            if (clickedPosition == expandedPosition) {
                // Click on already expanded item -> collapse it
                expandedPosition = -1;
                notifyItemChanged(clickedPosition);
            } else {
                // Click on collapsed item -> expand it and collapse previous
                expandedPosition = clickedPosition;
                
                // Collapse previous if exists
                if (previousExpandedPosition != -1) {
                    notifyItemChanged(previousExpandedPosition);
                }
                // Expand current
                notifyItemChanged(clickedPosition);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return cinemas != null ? cinemas.size() : 0;
    }
    
    public void updateData(List<ShowtimesByDate.ShowtimesByCinema> newCinemas) {
        this.cinemas = newCinemas;
        expandedPosition = -1; // Reset expansion when data changes
        notifyDataSetChanged();
    }
    
    interface OnExpandClickListener {
        void onExpandClick(int position);
    }
    
    static class CinemaViewHolder extends RecyclerView.ViewHolder {
        
        private TextView tvCinemaName;
        private TextView tvCinemaFormat;
        private ChipGroup chipGroupShowtimes;
        private ImageView ivExpandCollapse;
        
        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCinemaName = itemView.findViewById(R.id.tvCinemaName);
            tvCinemaFormat = itemView.findViewById(R.id.tvCinemaFormat);
            chipGroupShowtimes = itemView.findViewById(R.id.chipGroupShowtimes);
            ivExpandCollapse = itemView.findViewById(R.id.ivExpandCollapse);
        }
        
        public void bind(ShowtimesByDate.ShowtimesByCinema cinema, 
                        OnShowtimeClickListener listener, 
                        boolean isExpanded, 
                        int position, 
                        OnExpandClickListener expandListener) {
            tvCinemaName.setText(cinema.getName());
            
            // Distance removed - API không cung cấp dữ liệu khoảng cách
            
            // Click listener for expansion
            itemView.setOnClickListener(v -> {
                if (expandListener != null) {
                    expandListener.onExpandClick(position);
                }
            });
            
            // Rotate expand/collapse icon
            if (ivExpandCollapse != null) {
                ivExpandCollapse.setRotation(isExpanded ? 180 : 0);
            }
            
            // Group showtimes by format (2D, 3D, etc.)
            Map<String, List<ShowtimesByDate.ShowtimeItem>> groupedByFormat = new LinkedHashMap<>();
            if (cinema.getShowtimes() != null) {
                for (ShowtimesByDate.ShowtimeItem showtime : cinema.getShowtimes()) {
                    String format = showtime.getFormat() + " " + showtime.getLanguageType();
                    if (!groupedByFormat.containsKey(format)) {
                        groupedByFormat.put(format, new ArrayList<>());
                    }
                    groupedByFormat.get(format).add(showtime);
                }
            }
            
            if (isExpanded) {
                // Expanded state - show all formats and showtimes
                chipGroupShowtimes.setVisibility(View.VISIBLE);
                tvCinemaFormat.setVisibility(View.GONE);
                
                // Clear previous chips
                chipGroupShowtimes.removeAllViews();
                
                // Add format labels and showtime chips
                for (Map.Entry<String, List<ShowtimesByDate.ShowtimeItem>> entry : groupedByFormat.entrySet()) {
                    // Add format label as a disabled chip - FULL WIDTH xuống dòng riêng
                    Chip formatChip = new Chip(itemView.getContext());
                    formatChip.setText(entry.getKey());
                    formatChip.setTextSize(12);
                    formatChip.setChipBackgroundColorResource(R.color.cardBackground);
                    formatChip.setTextColor(itemView.getContext().getColor(R.color.textSecondary));
                    formatChip.setClickable(false);
                    formatChip.setCheckable(false);
                    formatChip.setChipStrokeWidth(0);
                    
                    // Set layout params MATCH_PARENT để xuống dòng riêng
                    ChipGroup.LayoutParams formatParams = new ChipGroup.LayoutParams(
                        ChipGroup.LayoutParams.MATCH_PARENT,
                        ChipGroup.LayoutParams.WRAP_CONTENT
                    );
                    formatParams.setMargins(0, 12, 0, 4); // Margin top 12 để có khoảng cách
                    formatChip.setLayoutParams(formatParams);
                    
                    chipGroupShowtimes.addView(formatChip);
                    
                    // Add showtime chips for this format
                    for (ShowtimesByDate.ShowtimeItem showtime : entry.getValue()) {
                        Chip showtimeChip = new Chip(itemView.getContext());
                        
                        // Format time from "2025-11-05T11:00:00" to "11:00"
                        String timeDisplay = formatTime(showtime.getStartTime());
                        showtimeChip.setText(timeDisplay);
                        
                        // LÀM TO CHIP VÀ TEXT HỢN
                        showtimeChip.setTextSize(14); // Tăng từ 12 lên 14
                        showtimeChip.setChipBackgroundColorResource(R.color.white);
                        showtimeChip.setTextColor(itemView.getContext().getColor(R.color.black));
                        showtimeChip.setChipStrokeWidth(2);
                        showtimeChip.setChipStrokeColorResource(R.color.border);
                        
                        // Căn giữa text và tăng chiều cao chip
                        showtimeChip.setTextAlignment(android.view.View.TEXT_ALIGNMENT_CENTER);
                        showtimeChip.setChipMinHeight(56); // Tăng height để chip lớn hơn
                        showtimeChip.setMinWidth(80); // Đặt min width để đều nhau
                        
                        // Giảm margin để gần label hơn
                        ChipGroup.LayoutParams showtimeParams = new ChipGroup.LayoutParams(
                            ChipGroup.LayoutParams.WRAP_CONTENT,
                            ChipGroup.LayoutParams.WRAP_CONTENT
                        );
                        showtimeParams.setMargins(4, 4, 4, 4); // Giảm margin từ 8 xuống 4
                        showtimeChip.setLayoutParams(showtimeParams);
                        
                        showtimeChip.setOnClickListener(v -> {
                            if (listener != null) {
                                listener.onShowtimeClick(showtime, cinema);
                            }
                        });
                        
                        chipGroupShowtimes.addView(showtimeChip);
                    }
                }
            } else {
                // Collapsed state - show format summary only
                chipGroupShowtimes.setVisibility(View.GONE);
                tvCinemaFormat.setVisibility(View.VISIBLE);
                
                // Show format summary (e.g., "2D Dubbed, 2D Subtitled")
                StringBuilder formatSummary = new StringBuilder();
                int index = 0;
                for (String format : groupedByFormat.keySet()) {
                    if (index > 0) formatSummary.append(", ");
                    formatSummary.append(format);
                    index++;
                }
                tvCinemaFormat.setText(formatSummary.toString());
            }
        }
        
        /**
         * Format ISO 8601 datetime to HH:mm
         * Example: "2025-11-05T11:00:00" -> "11:00"
         */
        private String formatTime(String isoDateTime) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Date date = inputFormat.parse(isoDateTime);
                return outputFormat.format(date);
            } catch (Exception e) {
                android.util.Log.e("SelectCinemaAdapter", "Error formatting time: " + e.getMessage());
                // Fallback: try to extract time from string
                if (isoDateTime != null && isoDateTime.length() >= 16) {
                    return isoDateTime.substring(11, 16); // Extract HH:mm from "yyyy-MM-ddTHH:mm:ss"
                }
                return isoDateTime;
            }
        }
    }
}
