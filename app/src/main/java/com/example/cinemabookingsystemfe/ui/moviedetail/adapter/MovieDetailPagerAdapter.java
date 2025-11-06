package com.example.cinemabookingsystemfe.ui.moviedetail.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cinemabookingsystemfe.data.models.response.MovieDetail;
import com.example.cinemabookingsystemfe.ui.moviedetail.fragment.MovieInfoFragment;
import com.example.cinemabookingsystemfe.ui.moviedetail.fragment.MovieReviewsFragment;

public class MovieDetailPagerAdapter extends FragmentStateAdapter {

    private MovieDetail movie;
    private int movieId;
    private boolean isComingSoon;

    public MovieDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, MovieDetail movie, int movieId, boolean isComingSoon) {
        super(fragmentActivity);
        this.movie = movie;
        this.movieId = movieId;
        this.isComingSoon = isComingSoon;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return MovieInfoFragment.newInstance(movie);
            case 1:
                return MovieReviewsFragment.newInstance(movieId, isComingSoon);
            default:
                return MovieInfoFragment.newInstance(movie);
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Two tabs: Thông tin and Đánh giá
    }
}
