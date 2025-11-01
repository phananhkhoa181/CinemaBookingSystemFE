package com.example.cinemabookingsystemfe.ui.components;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.cinemabookingsystemfe.R;

/**
 * LoadingDialog - Reusable loading dialog component
 * 
 * USAGE: Show loading indicator when calling API
 * 
 * USED IN:
 * - ALL activities/fragments with API calls
 * - LoginActivity
 * - RegisterActivity
 * - HomeFragment
 * - SelectSeatActivity
 * - BookingSummaryActivity
 * - PaymentResultActivity
 * 
 * PRIORITY: ⭐⭐⭐⭐⭐ CRITICAL (Used everywhere)
 */
public class LoadingDialog extends Dialog {
    
    private TextView tvMessage;
    
    public LoadingDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        
        View view = LayoutInflater.from(context).inflate(
            R.layout.dialog_loading, null
        );
        
        tvMessage = view.findViewById(R.id.tvMessage);
        
        setContentView(view);
        setCancelable(false); // User cannot dismiss by clicking outside
    }
    
    /**
     * Set custom loading message
     * @param message The message to display
     */
    public void setMessage(String message) {
        if (tvMessage != null) {
            tvMessage.setText(message);
        }
    }
    
    /**
     * Show loading with default message
     */
    public void showLoading() {
        if (!isShowing()) {
            setMessage("Đang tải...");
            show();
        }
    }
    
    /**
     * Show loading with custom message
     * @param message The message to display
     */
    public void showLoading(String message) {
        if (!isShowing()) {
            setMessage(message);
            show();
        }
    }
    
    /**
     * Hide loading dialog
     */
    public void hideLoading() {
        if (isShowing()) {
            dismiss();
        }
    }
}
