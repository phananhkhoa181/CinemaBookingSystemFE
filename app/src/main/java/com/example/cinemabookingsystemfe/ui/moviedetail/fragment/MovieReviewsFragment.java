package com.example.cinemabookingsystemfe.ui.moviedetail.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.request.CreateReviewRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.ReviewResponse;
import com.example.cinemabookingsystemfe.data.repository.MovieRepository;
import com.example.cinemabookingsystemfe.ui.moviedetail.adapter.ReviewAdapter;
import com.example.cinemabookingsystemfe.utils.TokenManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MovieReviewsFragment extends Fragment {

    private static final String ARG_MOVIE_ID = "movie_id";
    private static final String ARG_IS_COMING_SOON = "is_coming_soon";
    
    private int movieId;
    private boolean isComingSoon;
    private MovieRepository movieRepository;
    private ReviewAdapter reviewAdapter;
    
    private TextView tvAverageRating, tvEmptyReviews;
    private MaterialButton btnWriteReview;
    private RecyclerView rvReviews;

    public static MovieReviewsFragment newInstance(int movieId, boolean isComingSoon) {
        MovieReviewsFragment fragment = new MovieReviewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        args.putBoolean(ARG_IS_COMING_SOON, isComingSoon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getInt(ARG_MOVIE_ID);
            isComingSoon = getArguments().getBoolean(ARG_IS_COMING_SOON, false);
        }
        movieRepository = MovieRepository.getInstance(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        tvAverageRating = view.findViewById(R.id.tvAverageRating);
        tvEmptyReviews = view.findViewById(R.id.tvEmptyReviews);
        btnWriteReview = view.findViewById(R.id.btnWriteReview);
        rvReviews = view.findViewById(R.id.rvReviews);
        
        rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        reviewAdapter = new ReviewAdapter();
        rvReviews.setAdapter(reviewAdapter);
        
        btnWriteReview.setOnClickListener(v -> showWriteReviewDialog());
        
        loadReviews();
    }

    private void loadReviews() {
        movieRepository.getMovieReviews(movieId, 1, 10, "latest", new ApiCallback<ApiResponse<ReviewResponse>>() {
            @Override
            public void onSuccess(ApiResponse<ReviewResponse> response) {
                if (response != null && response.getData() != null) {
                    ReviewResponse data = response.getData();
                    
                    if (data.getTotalCount() > 0) {
                        tvAverageRating.setText(String.format("%.1f (%d đánh giá)", 
                                data.getAverageRating(), data.getTotalCount()));
                        reviewAdapter.setReviews(data.getReviews());
                        rvReviews.setVisibility(View.VISIBLE);
                        tvEmptyReviews.setVisibility(View.GONE);
                    } else {
                        tvAverageRating.setText("Chưa có đánh giá");
                        reviewAdapter.setReviews(null);
                        rvReviews.setVisibility(View.GONE);
                        tvEmptyReviews.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Show empty state on error
                tvAverageRating.setText("Chưa có đánh giá");
                rvReviews.setVisibility(View.GONE);
                tvEmptyReviews.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showWriteReviewDialog() {
        // Check if movie is coming soon (based on release date)
        if (isComingSoon) {
            Toast.makeText(requireContext(), "Bạn không thể đánh giá phim chưa được chiếu", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check if user is logged in
        TokenManager tokenManager = TokenManager.getInstance(requireContext());
        String token = tokenManager.getToken();
        if (token == null || token.isEmpty()) {
            // Navigate to login screen
            Toast.makeText(requireContext(), "Vui lòng đăng nhập để đánh giá phim", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireContext(), com.example.cinemabookingsystemfe.ui.auth.LoginActivity.class);
            startActivity(intent);
            return;
        }

        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_write_review);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        TextInputEditText etComment = dialog.findViewById(R.id.etComment);
        MaterialButton btnCancel = dialog.findViewById(R.id.btnCancel);
        MaterialButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
        
        // Setup RatingBar interactive behavior - tô màu khi hover/touch
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (fromUser) {
                // Update the rating when user clicks
                ratingBar1.setRating(rating);
            }
        });
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnSubmit.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = etComment.getText() != null ? etComment.getText().toString().trim() : "";
            
            if (rating == 0) {
                Toast.makeText(requireContext(), "Vui lòng chọn số sao", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (comment.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập nhận xét", Toast.LENGTH_SHORT).show();
                return;
            }
            
            submitReview((int) rating, comment);
            dialog.dismiss();
        });
        
        dialog.show();
    }

    private void submitReview(int rating, String comment) {
        CreateReviewRequest request = new CreateReviewRequest(movieId, rating, comment);
        
        movieRepository.createReview(request, new ApiCallback<ApiResponse<Void>>() {
            @Override
            public void onSuccess(ApiResponse<Void> response) {
                Toast.makeText(requireContext(), "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                loadReviews(); // Reload reviews
            }

            @Override
            public void onError(String errorMessage) {
                // Check if it's an authentication error
                if (errorMessage.contains("401") || errorMessage.contains("Unauthorized") || 
                    errorMessage.contains("closed")) {
                    Toast.makeText(requireContext(), 
                            "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.", 
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(requireContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
