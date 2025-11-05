package com.example.cinemabookingsystemfe.ui.moviedetail.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnItemClickListener onItemClickListener;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    // ⚡ Interface listener
    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Movie> newList) {
        this.movies = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_grid, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (position < 0 || position >= movies.size()) {
            return;
        }
        
        Movie movie = movies.get(position);
        if (movie == null) {
            return;
        }
        
        // Bind data safely
        holder.tvTitle.setText(movie.getTitle() != null ? movie.getTitle() : "Unknown");
        
        // Hide rating in search list (only show in detail)
        holder.tvRating.setVisibility(View.GONE);
        
        holder.tvGenre.setText(movie.getGenre() != null ? movie.getGenre() : "");
        holder.tvAgeRating.setText(movie.getRating() != null ? movie.getRating() : "T16");
        holder.tvDuration.setText(movie.getDurationMinutes() + "'");

        // Load image
        Glide.with(holder.itemView.getContext())
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.placeholder_movie_poster)
                .error(R.drawable.placeholder_movie_poster)
                .into(holder.ivPoster);
        
        // Set click listener
        holder.bind(movie, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    // ⚡ MovieViewHolder phải là public static
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvRating, tvGenre, tvAgeRating, tvDuration;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvGenre = itemView.findViewById(R.id.tvGenre);
            tvAgeRating = itemView.findViewById(R.id.tvAgeRating);
            tvDuration = itemView.findViewById(R.id.tvDuration);
        }
        
        public void bind(Movie movie, OnItemClickListener listener) {
            // Set click listener
            itemView.setOnClickListener(v -> {
                Log.d("MovieAdapter", "Item clicked: " + (movie != null ? movie.getTitle() : "null"));
                Log.d("MovieAdapter", "Listener: " + (listener != null ? "set" : "null"));
                Log.d("MovieAdapter", "MovieId: " + (movie != null ? movie.getMovieId() : "null"));
                
                if (listener != null && movie != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(movie);
                    }
                }
            });
        }
    }
}
