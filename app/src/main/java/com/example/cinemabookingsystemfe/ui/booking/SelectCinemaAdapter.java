package com.example.cinemabookingsystemfe.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.ShowtimesByDate;
import com.example.cinemabookingsystemfe.utils.LocationHelper;

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
        private TextView tvCinemaDistance;
        private LinearLayout layoutShowtimesContainer;
        private ImageView ivExpandCollapse;
        
        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCinemaName = itemView.findViewById(R.id.tvCinemaName);
            tvCinemaDistance = itemView.findViewById(R.id.tvCinemaDistance);
            layoutShowtimesContainer = itemView.findViewById(R.id.layoutShowtimesContainer);
            ivExpandCollapse = itemView.findViewById(R.id.ivExpandCollapse);
        }
        
        public void bind(ShowtimesByDate.ShowtimesByCinema cinema, 
                        OnShowtimeClickListener listener, 
                        boolean isExpanded, 
                        int position, 
                        OnExpandClickListener expandListener) {
            tvCinemaName.setText(cinema.getName());
            
            // Display distance if available
            if (tvCinemaDistance != null) {
                Double distance = cinema.getDistance();
                if (distance != null && distance > 0) {
                    tvCinemaDistance.setText(LocationHelper.formatDistance(distance));
                    tvCinemaDistance.setVisibility(View.VISIBLE);
                } else {
                    tvCinemaDistance.setVisibility(View.GONE);
                }
            }
            
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
                    // Skip showtimes that have already passed
                    if (isShowtimePassed(showtime.getStartTime())) {
                        continue;
                    }
                    
                    String format = showtime.getFormat() + " " + showtime.getLanguageType();
                    if (!groupedByFormat.containsKey(format)) {
                        groupedByFormat.put(format, new ArrayList<>());
                    }
                    groupedByFormat.get(format).add(showtime);
                }
            }
            
            if (isExpanded) {
                // Expanded state - show all formats and showtimes
                layoutShowtimesContainer.setVisibility(View.VISIBLE);
                
                // Clear previous views
                layoutShowtimesContainer.removeAllViews();
                
                // Add format groups with showtimes
                for (Map.Entry<String, List<ShowtimesByDate.ShowtimeItem>> entry : groupedByFormat.entrySet()) {
                    // Create format group container
                    LinearLayout formatGroup = new LinearLayout(itemView.getContext());
                    formatGroup.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams groupParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    groupParams.setMargins(0, 0, 0, dpToPx(16)); // 16dp bottom margin
                    formatGroup.setLayoutParams(groupParams);
                    
                    // Add format title (e.g., "2D PHỤ ĐỀ")
                    TextView formatTitle = new TextView(itemView.getContext());
                    formatTitle.setText(entry.getKey());
                    formatTitle.setTextSize(13);
                    formatTitle.setTextColor(itemView.getContext().getColor(R.color.textSecondary));
                    LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    formatTitle.setLayoutParams(titleParams);
                    formatGroup.addView(formatTitle);
                    
                    // Create grid container for showtimes (Flow layout behavior)
                    LinearLayout gridContainer = new LinearLayout(itemView.getContext());
                    gridContainer.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    gridParams.setMargins(0, dpToPx(8), 0, 0); // 8dp top margin - ĐƯA GẦN LẠI
                    gridContainer.setLayoutParams(gridParams);
                    
                    // Create rows of showtime boxes (3 per row)
                    LinearLayout currentRow = null;
                    int itemsInRow = 0;
                    final int ITEMS_PER_ROW = 3;
                    
                    for (int i = 0; i < entry.getValue().size(); i++) {
                        ShowtimesByDate.ShowtimeItem showtime = entry.getValue().get(i);
                        
                        // Create new row if needed
                        if (itemsInRow == 0) {
                            currentRow = new LinearLayout(itemView.getContext());
                            currentRow.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            currentRow.setLayoutParams(rowParams);
                            gridContainer.addView(currentRow);
                        }
                        
                        // Create showtime box
                        TextView showtimeBox = new TextView(itemView.getContext());
                        String timeDisplay = formatTime(showtime.getStartTime());
                        showtimeBox.setText(timeDisplay);
                        showtimeBox.setTextSize(14);
                        showtimeBox.setTextColor(itemView.getContext().getColor(R.color.textPrimary));
                        showtimeBox.setGravity(Gravity.CENTER);
                        showtimeBox.setBackgroundResource(R.drawable.bg_showtime_box_selector);
                        showtimeBox.setClickable(true);
                        showtimeBox.setFocusable(true);
                        
                        // Set padding for showtime box
                        int paddingH = dpToPx(20);
                        int paddingV = dpToPx(10);
                        showtimeBox.setPadding(paddingH, paddingV, paddingH, paddingV);
                        
                        // Set layout params with margins
                        LinearLayout.LayoutParams boxParams = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1.0f // Equal weight for all items
                        );
                        boxParams.setMargins(0, 0, dpToPx(8), dpToPx(8));
                        showtimeBox.setLayoutParams(boxParams);
                        
                        showtimeBox.setOnClickListener(v -> {
                            if (listener != null) {
                                listener.onShowtimeClick(showtime, cinema);
                            }
                        });
                        
                        currentRow.addView(showtimeBox);
                        itemsInRow++;
                        
                        // Reset row counter after 3 items
                        if (itemsInRow == ITEMS_PER_ROW) {
                            itemsInRow = 0;
                        }
                    }
                    
                    // Add empty placeholders to fill last row
                    if (itemsInRow > 0 && itemsInRow < ITEMS_PER_ROW && currentRow != null) {
                        for (int i = itemsInRow; i < ITEMS_PER_ROW; i++) {
                            View placeholder = new View(itemView.getContext());
                            LinearLayout.LayoutParams placeholderParams = new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1.0f
                            );
                            placeholderParams.setMargins(0, 0, dpToPx(8), dpToPx(8));
                            placeholder.setLayoutParams(placeholderParams);
                            currentRow.addView(placeholder);
                        }
                    }
                    
                    formatGroup.addView(gridContainer);
                    layoutShowtimesContainer.addView(formatGroup);
                }
            } else {
                // Collapsed state - hide showtimes
                layoutShowtimesContainer.setVisibility(View.GONE);
            }
        }
        
        /**
         * Convert dp to pixels
         */
        private int dpToPx(int dp) {
            float density = itemView.getContext().getResources().getDisplayMetrics().density;
            return Math.round(dp * density);
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
        
        /**
         * Check if showtime has already passed
         * @param isoDateTime Showtime in ISO 8601 format (e.g., "2025-11-06T14:30:00")
         * @return true if showtime has passed, false otherwise
         */
        private boolean isShowtimePassed(String isoDateTime) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                Date showtimeDate = inputFormat.parse(isoDateTime);
                Date now = new Date();
                
                // Return true if showtime is before current time
                return showtimeDate != null && showtimeDate.before(now);
            } catch (Exception e) {
                android.util.Log.e("SelectCinemaAdapter", "Error checking showtime: " + e.getMessage());
                // If error parsing, don't hide the showtime
                return false;
            }
        }
    }
}
