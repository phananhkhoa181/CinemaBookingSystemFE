package com.example.cinemabookingsystemfe.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.Cinema;

import java.util.List;

/**
 * CinemaAdapter - Displays list of cinemas with images and distance
 */
public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaViewHolder> {
    
    private List<Cinema> cinemas;
    private OnCinemaClickListener listener;
    
    public interface OnCinemaClickListener {
        void onCinemaClick(Cinema cinema);
    }
    
    public CinemaAdapter(List<Cinema> cinemas, OnCinemaClickListener listener) {
        this.cinemas = cinemas;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cinema, parent, false);
        return new CinemaViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CinemaViewHolder holder, int position) {
        Cinema cinema = cinemas.get(position);
        holder.bind(cinema, listener);
    }
    
    @Override
    public int getItemCount() {
        return cinemas.size();
    }
    
    public void updateData(List<Cinema> newCinemas) {
        this.cinemas = newCinemas;
        notifyDataSetChanged();
    }
    
    static class CinemaViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView ivCinema;
        private TextView tvCinemaName;
        private TextView tvCinemaAddress;
        private TextView tvCinemaPhone;
        private TextView tvDistance;
        private com.google.android.material.button.MaterialButton btnViewOnMap;
        
        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCinema = itemView.findViewById(R.id.ivCinema);
            tvCinemaName = itemView.findViewById(R.id.tvCinemaName);
            tvCinemaAddress = itemView.findViewById(R.id.tvCinemaAddress);
            tvCinemaPhone = itemView.findViewById(R.id.tvCinemaPhone);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            btnViewOnMap = itemView.findViewById(R.id.btnViewOnMap);
        }
        
        public void bind(Cinema cinema, OnCinemaClickListener listener) {
            // Set cinema name
            tvCinemaName.setText(cinema.getName());
            
            // Set address
            tvCinemaAddress.setText(cinema.getAddress());
            
            // Set phone
            tvCinemaPhone.setText(cinema.getPhone());
            
            // Set hardcoded images based on cinema ID
            int imageResource = getCinemaImageResource(cinema.getCinemaId());
            ivCinema.setImageResource(imageResource);
            
            // Debug log
            android.util.Log.d("CinemaAdapter", "Cinema ID: " + cinema.getCinemaId() + 
                " -> Image: " + imageResource + " (" + cinema.getName() + ")");
            
            // Set distance if available
            if (cinema.getDistance() > 0) {
                tvDistance.setVisibility(View.VISIBLE);
                tvDistance.setText(String.format("%.1f km", cinema.getDistance()));
            } else {
                tvDistance.setVisibility(View.GONE);
            }
            
            // Click listener for entire card to open Google Maps
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCinemaClick(cinema);
                }
            });
            
            // Click listener for button to open Google Maps
            btnViewOnMap.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCinemaClick(cinema);
                }
            });
        }
        
        /**
         * Get hardcoded image resource based on cinema ID
         * ID 1 -> rap1.png
         * ID 2 -> rap2.png
         * ID 3 -> rap3.png
         */
        private int getCinemaImageResource(int cinemaId) {
            switch (cinemaId) {
                case 1:
                    return R.drawable.rap1;
                case 2:
                    return R.drawable.rap2;
                case 3:
                    return R.drawable.rap3;
                default:
                    return R.drawable.rap1; // Default fallback
            }
        }
    }
}
