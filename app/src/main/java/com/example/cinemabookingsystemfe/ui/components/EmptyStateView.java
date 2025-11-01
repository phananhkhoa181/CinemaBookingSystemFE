package com.example.cinemabookingsystemfe.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import com.example.cinemabookingsystemfe.R;
import com.google.android.material.button.MaterialButton;

/**
 * EmptyStateView - Reusable empty state component
 * 
 * USAGE: Display empty state when no data is available
 * 
 * USED IN:
 * - BookingHistoryFragment (no bookings)
 * - SearchMovieActivity (no search results)
 * - ReviewListActivity (no reviews)
 * - NotificationsFragment (no notifications)
 * 
 * PRIORITY: ⭐⭐⭐⭐ IMPORTANT (Good UX for empty states)
 */
public class EmptyStateView extends LinearLayout {
    
    private ImageView ivEmptyIcon;
    private TextView tvEmptyTitle;
    private TextView tvEmptyMessage;
    private MaterialButton btnAction;
    
    private OnActionClickListener actionListener;
    
    public interface OnActionClickListener {
        void onActionClick();
    }
    
    public EmptyStateView(Context context) {
        super(context);
        init(context);
    }
    
    public EmptyStateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    private void init(Context context) {
        LayoutInflater.from(context).inflate(
            R.layout.component_empty_state, this, true
        );
        
        ivEmptyIcon = findViewById(R.id.ivEmptyIcon);
        tvEmptyTitle = findViewById(R.id.tvEmptyTitle);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        btnAction = findViewById(R.id.btnAction);
        
        btnAction.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onActionClick();
            }
        });
    }
    
    /**
     * Set empty state with icon, title, and message
     */
    public void setEmptyState(@DrawableRes int iconRes, String title, String message) {
        ivEmptyIcon.setImageResource(iconRes);
        tvEmptyTitle.setText(title);
        tvEmptyMessage.setText(message);
        btnAction.setVisibility(View.GONE);
    }
    
    /**
     * Set empty state with icon, title, message, and action button
     */
    public void setEmptyState(@DrawableRes int iconRes, String title, String message, 
                             String actionText, OnActionClickListener listener) {
        ivEmptyIcon.setImageResource(iconRes);
        tvEmptyTitle.setText(title);
        tvEmptyMessage.setText(message);
        btnAction.setText(actionText);
        btnAction.setVisibility(View.VISIBLE);
        this.actionListener = listener;
    }
    
    /**
     * Show action button
     */
    public void showActionButton(String actionText, OnActionClickListener listener) {
        btnAction.setText(actionText);
        btnAction.setVisibility(View.VISIBLE);
        this.actionListener = listener;
    }
    
    /**
     * Hide action button
     */
    public void hideActionButton() {
        btnAction.setVisibility(View.GONE);
    }
}
