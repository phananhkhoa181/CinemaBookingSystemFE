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
import com.example.cinemabookingsystemfe.data.model.Promotion;

import java.util.ArrayList;
import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {
    
    private List<Promotion> promotions;
    private OnPromotionClickListener listener;
    
    public interface OnPromotionClickListener {
        void onPromotionClick(Promotion promotion);
    }
    
    public PromotionAdapter(OnPromotionClickListener listener) {
        this.promotions = new ArrayList<>();
        this.listener = listener;
    }
    
    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_banner, parent, false);
        return new PromotionViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        holder.bind(promotions.get(position));
    }
    
    @Override
    public int getItemCount() {
        return promotions.size();
    }
    
    class PromotionViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBanner;
        TextView tvPromotionName;
        TextView tvPromotionDescription;
        TextView tvPromotionDiscount;
        
        PromotionViewHolder(View itemView) {
            super(itemView);
            ivBanner = itemView.findViewById(R.id.ivBanner);
            tvPromotionName = itemView.findViewById(R.id.tvPromotionName);
            tvPromotionDescription = itemView.findViewById(R.id.tvPromotionDescription);
            tvPromotionDiscount = itemView.findViewById(R.id.tvPromotionDiscount);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onPromotionClick(promotions.get(position));
                }
            });
        }
        
        void bind(Promotion promotion) {
            // Set text content
            tvPromotionName.setText(promotion.getName());
            tvPromotionDescription.setText(promotion.getDescription());
            tvPromotionDiscount.setText(promotion.getDiscountDisplay());
            
            // Load banner image
            if (promotion.getImageUrl() != null && !promotion.getImageUrl().isEmpty()) {
                Glide.with(ivBanner.getContext())
                    .load(promotion.getImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(ivBanner);
            } else {
                // Fallback: Use colorful background if no image URL
                int[] colors = {
                    0xFFE53935, // Red
                    0xFF1E88E5, // Blue
                    0xFFFB8C00, // Orange
                    0xFF43A047  // Green
                };
                int position = getAdapterPosition();
                int color = colors[position % colors.length];
                ivBanner.setBackgroundColor(color);
            }
        }
    }
}
