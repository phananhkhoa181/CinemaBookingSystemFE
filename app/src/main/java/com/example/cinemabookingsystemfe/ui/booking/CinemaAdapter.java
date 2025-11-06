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
import com.example.cinemabookingsystemfe.data.models.response.Cinema;
import com.example.cinemabookingsystemfe.data.models.response.Showtime;

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
        private LinearLayout layoutShowtimesContainer;
        private ImageView ivExpandCollapse;
        
        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCinemaName = itemView.findViewById(R.id.tvCinemaName);
            layoutShowtimesContainer = itemView.findViewById(R.id.layoutShowtimesContainer);
            ivExpandCollapse = itemView.findViewById(R.id.ivExpandCollapse);
        }
        
        public void bind(Cinema cinema, OnShowtimeClickListener listener, boolean isExpanded, int position, OnExpandClickListener expandListener) {
            tvCinemaName.setText(cinema.getName());
            // Distance removed - API không cung cấp
            
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
                layoutShowtimesContainer.setVisibility(View.VISIBLE);
                
                // Clear previous views
                layoutShowtimesContainer.removeAllViews();
                
                // Add format groups with showtimes
                for (java.util.Map.Entry<String, java.util.List<Showtime>> entry : groupedByFormat.entrySet()) {
                    // Create format group container
                    LinearLayout formatGroup = new LinearLayout(itemView.getContext());
                    formatGroup.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams groupParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    groupParams.setMargins(0, 0, 0, dpToPx(16));
                    formatGroup.setLayoutParams(groupParams);
                    
                    // Add format title
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
                    
                    // Create grid container for showtimes
                    LinearLayout gridContainer = new LinearLayout(itemView.getContext());
                    gridContainer.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    gridParams.setMargins(0, dpToPx(8), 0, 0);
                    gridContainer.setLayoutParams(gridParams);
                    
                    // Create rows of showtime boxes (3 per row)
                    LinearLayout currentRow = null;
                    int itemsInRow = 0;
                    final int ITEMS_PER_ROW = 3;
                    
                    for (int i = 0; i < entry.getValue().size(); i++) {
                        Showtime showtime = entry.getValue().get(i);
                        
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
                        showtimeBox.setText(showtime.getTime());
                        showtimeBox.setTextSize(14);
                        showtimeBox.setTextColor(itemView.getContext().getColor(R.color.textPrimary));
                        showtimeBox.setGravity(Gravity.CENTER);
                        showtimeBox.setBackgroundResource(R.drawable.bg_showtime_box_selector);
                        showtimeBox.setClickable(true);
                        showtimeBox.setFocusable(true);
                        
                        // Set padding
                        int paddingH = dpToPx(20);
                        int paddingV = dpToPx(10);
                        showtimeBox.setPadding(paddingH, paddingV, paddingH, paddingV);
                        
                        // Set layout params
                        LinearLayout.LayoutParams boxParams = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1.0f
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
                // Collapsed state
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
    }
}
