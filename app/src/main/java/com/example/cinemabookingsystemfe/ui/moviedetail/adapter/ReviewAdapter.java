package com.example.cinemabookingsystemfe.ui.moviedetail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.ReviewResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewResponse.Review> reviews;

    public ReviewAdapter() {
        this.reviews = new ArrayList<>();
    }

    public void setReviews(List<ReviewResponse.Review> reviews) {
        this.reviews = reviews != null ? reviews : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewResponse.Review review = reviews.get(position);
        
        // Anonymous user display
        holder.tvCustomerName.setText("Người dùng ẩn danh");
        
        holder.tvRating.setText(String.valueOf(review.getRating()));
        holder.tvComment.setText(review.getComment());
        
        // Format date from "2025-11-03T16:35:21.789814" to "03/11/2025"
        String formattedDate = formatDate(review.getCreatedAt());
        holder.tvCreatedAt.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    private String formatDate(String isoDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(isoDate);
            return date != null ? outputFormat.format(date) : isoDate;
        } catch (ParseException e) {
            return isoDate;
        }
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName;
        TextView tvRating;
        TextView tvComment;
        TextView tvCreatedAt;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }
}
