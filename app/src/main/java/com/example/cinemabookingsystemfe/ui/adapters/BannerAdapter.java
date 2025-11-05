package com.example.cinemabookingsystemfe.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.model.Banner;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    
    private List<Banner> banners;
    private OnBannerClickListener listener;
    
    public interface OnBannerClickListener {
        void onBannerClick(Banner banner);
    }
    
    public BannerAdapter(OnBannerClickListener listener) {
        this.banners = new ArrayList<>();
        this.listener = listener;
    }
    
    public void setBanners(List<Banner> banners) {
        this.banners = banners;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        holder.bind(banners.get(position));
    }
    
    @Override
    public int getItemCount() {
        return banners.size();
    }
    
    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBanner;
        
        BannerViewHolder(View itemView) {
            super(itemView);
            ivBanner = itemView.findViewById(R.id.ivBanner);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBannerClick(banners.get(position));
                }
            });
        }
        
        void bind(Banner banner) {
            Glide.with(ivBanner.getContext())
                .load(banner.getImageUrl())
                .centerCrop()
                .into(ivBanner);
        }
    }
}
