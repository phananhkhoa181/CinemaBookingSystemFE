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
import com.example.cinemabookingsystemfe.data.models.response.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    
    private List<Movie> movies;
    private OnMovieClickListener listener;
    
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
    
    public MovieAdapter(OnMovieClickListener listener) {
        this.movies = new ArrayList<>();
        this.listener = listener;
    }
    
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }
    
    @Override
    public int getItemCount() {
        return movies.size();
    }
    
    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster, ivPlaceholder;
        TextView tvTitle, tvGenre, tvRating, tvDuration, tvAgeRating;
        
        MovieViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            ivPlaceholder = itemView.findViewById(R.id.ivPlaceholder);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvGenre = itemView.findViewById(R.id.tvGenre);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvAgeRating = itemView.findViewById(R.id.tvAgeRating);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onMovieClick(movies.get(position));
                }
            });
        }
        
        void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvGenre.setText(movie.getGenresString());
            
            // Hide rating in list view (only show in detail)
            tvRating.setVisibility(View.GONE);
            
            tvDuration.setText(movie.getDuration() + "'");
            
            // Age rating badge (rating field from backend)
            if (movie.getRating() != null && !movie.getRating().isEmpty()) {
                tvAgeRating.setText(movie.getRating());
                tvAgeRating.setVisibility(View.VISIBLE);
            } else {
                tvAgeRating.setVisibility(View.GONE);
            }
            
            // Show placeholder by default
            ivPlaceholder.setVisibility(View.VISIBLE);
            
            Glide.with(ivPoster.getContext())
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.bg_movie_placeholder)
                .error(R.drawable.bg_movie_placeholder)
                .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(com.bumptech.glide.load.engine.GlideException e, Object model, 
                        com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, 
                        boolean isFirstResource) {
                        ivPlaceholder.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(android.graphics.drawable.Drawable resource, 
                        Object model, 
                        com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, 
                        com.bumptech.glide.load.DataSource dataSource, 
                        boolean isFirstResource) {
                        ivPlaceholder.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(ivPoster);
        }
    }
}
