package com.example.cinemabookingsystemfe.ui.moviedetail.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.MovieDetail;

public class MovieInfoFragment extends Fragment {

    private static final String ARG_MOVIE = "movie";
    
    private MovieDetail movie;
    private TextView tvOverview, tvGenre, tvDirector;

    public static MovieInfoFragment newInstance(MovieDetail movie) {
        MovieInfoFragment fragment = new MovieInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (MovieDetail) getArguments().getSerializable(ARG_MOVIE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        tvOverview = view.findViewById(R.id.tvOverview);
        tvGenre = view.findViewById(R.id.tvGenre);
        tvDirector = view.findViewById(R.id.tvDirector);
        
        if (movie != null) {
            tvOverview.setText(movie.getDescription() != null ? movie.getDescription() : "Chưa có thông tin");
            tvGenre.setText(movie.getGenre() != null ? movie.getGenre() : "Chưa cập nhật");
            tvDirector.setText(movie.getDirector() != null ? movie.getDirector() : "Chưa cập nhật");
        }
    }
}
