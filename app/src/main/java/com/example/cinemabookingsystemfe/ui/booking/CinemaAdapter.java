package com.example.cinemabookingsystemfe.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.Cinema;
import com.example.cinemabookingsystemfe.data.models.response.Showtime;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaViewHolder> {
    
    private List<Cinema> cinemas;
    private OnShowtimeClickListener listener;
    private int expandedPosition = -1; // Track which item is expanded
    
    public interface OnShowtimeClickListener {
        void onShowtimeClick(Showtime showtime, Cinema cinema);
    }
    
    public CinemaAdapter(List<Cinema> cinemas, OnShowtimeClickListener listener) {
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
        Cinema cinema = cinemas.get(position);
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
        return cinemas.size();
    }
    
    public void updateData(List<Cinema> newCinemas) {
        this.cinemas = newCinemas;
        expandedPosition = -1; // Reset expansion when data changes
        notifyDataSetChanged();
    }
    
    interface OnExpandClickListener {
        void onExpandClick(int position);
    }
    
    static class CinemaViewHolder extends RecyclerView.ViewHolder {
        
        private TextView tvCinemaName;
        private TextView tvDistance;
        private TextView tvCinemaFormat;
        private ChipGroup chipGroupShowtimes;
        private ImageView ivExpandCollapse;
        
        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCinemaName = itemView.findViewById(R.id.tvCinemaName);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvCinemaFormat = itemView.findViewById(R.id.tvCinemaFormat);
            chipGroupShowtimes = itemView.findViewById(R.id.chipGroupShowtimes);
            ivExpandCollapse = itemView.findViewById(R.id.ivExpandCollapse);
        }
        
        public void bind(Cinema cinema, OnShowtimeClickListener listener, boolean isExpanded, int position, OnExpandClickListener expandListener) {
            tvCinemaName.setText(cinema.getName());
            tvDistance.setText(String.format("%.1f km", cinema.getDistance()));
            
            // Click listener for expansion
            itemView.setOnClickListener(v -> {
                if (expandListener != null) {
                    expandListener.onExpandClick(position);
                }
            });
            
            // Rotate expand/collapse icon
            ivExpandCollapse.setRotation(isExpanded ? 180 : 0);
            
            // Group showtimes by format (2D, 3D, etc.)
            java.util.Map<String, java.util.List<Showtime>> groupedByFormat = new java.util.LinkedHashMap<>();
            if (cinema.getShowtimes() != null) {
                for (Showtime showtime : cinema.getShowtimes()) {
                    String format = showtime.getFormat();
                    if (!groupedByFormat.containsKey(format)) {
                        groupedByFormat.put(format, new java.util.ArrayList<>());
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
                
                // Set ChipGroup to wrap content and break lines
                chipGroupShowtimes.removeAllViews();
                
                // Add format labels and showtime chips with line breaks
                for (java.util.Map.Entry<String, java.util.List<Showtime>> entry : groupedByFormat.entrySet()) {
                    // Add format label as a disabled chip - full width để xuống dòng
                    Chip formatChip = new Chip(itemView.getContext());
                    formatChip.setText(entry.getKey());
                    formatChip.setTextSize(12);
                    formatChip.setChipBackgroundColorResource(R.color.cardBackground);
                    formatChip.setTextColor(itemView.getContext().getColor(R.color.textSecondary));
                    formatChip.setClickable(false);
                    formatChip.setCheckable(false);
                    formatChip.setChipStrokeWidth(0);
                    
                    // Set layout params to force new line - margin nhỏ
                    ViewGroup.MarginLayoutParams formatParams = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    formatParams.topMargin = (int) (4 * itemView.getContext().getResources().getDisplayMetrics().density);
                    formatParams.bottomMargin = 0; // KHÔNG có margin bottom
                    formatChip.setLayoutParams(formatParams);
                    
                    chipGroupShowtimes.addView(formatChip);
                    
                    // Add showtime chips for this format - trên cùng 1 dòng
                    for (Showtime showtime : entry.getValue()) {
                        Chip chip = new Chip(itemView.getContext());
                        chip.setText(showtime.getTime());
                        chip.setCheckable(false);
                        chip.setClickable(true);
                        chip.setChipBackgroundColorResource(android.R.color.white);
                        chip.setTextColor(itemView.getContext().getColor(android.R.color.black));
                        chip.setChipStrokeWidth(1);
                        chip.setChipStrokeColorResource(R.color.border);
                        
                        chip.setOnClickListener(v -> {
                            if (listener != null) {
                                listener.onShowtimeClick(showtime, cinema);
                            }
                        });
                        
                        chipGroupShowtimes.addView(chip);
                    }
                }
            } else {
                // Collapsed state - show only format text
                chipGroupShowtimes.setVisibility(View.GONE);
                
                if (!groupedByFormat.isEmpty()) {
                    // Show all formats separated by comma
                    StringBuilder formats = new StringBuilder();
                    int count = 0;
                    for (String format : groupedByFormat.keySet()) {
                        if (count > 0) formats.append(", ");
                        formats.append(format);
                        count++;
                    }
                    tvCinemaFormat.setText(formats.toString());
                    tvCinemaFormat.setVisibility(View.VISIBLE);
                } else {
                    tvCinemaFormat.setVisibility(View.GONE);
                }
            }
        }
    }
}
