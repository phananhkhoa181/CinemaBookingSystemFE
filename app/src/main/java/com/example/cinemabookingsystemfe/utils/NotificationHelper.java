package com.example.cinemabookingsystemfe.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.cinemabookingsystemfe.R;

/**
 * NotificationHelper - Quản lý thông báo trong app
 */
public class NotificationHelper {
    
    private static final String CHANNEL_ID = "cinema_booking_channel";
    private static final String CHANNEL_NAME = "Cinema Booking Notifications";
    private static final String CHANNEL_DESC = "Thông báo về đặt vé và hủy vé";
    
    private static final int NOTIFICATION_ID_BOOKING_SUCCESS = 1001;
    private static final int NOTIFICATION_ID_BOOKING_CANCELLED = 1002;
    
    private Context context;
    private NotificationManagerCompat notificationManager;
    
    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = NotificationManagerCompat.from(context);
        createNotificationChannel();
    }
    
    /**
     * Tạo notification channel (required for Android 8.0+)
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
    
    /**
     * Hiển thị notification đặt vé thành công
     */
    public void showBookingSuccessNotification(String bookingCode, double totalAmount) {
        String title = "Đặt vé thành công! 🎉";
        String message = String.format("Mã đặt vé: %s - Tổng tiền: %,.0fđ", bookingCode, totalAmount);
        
        // Intent to open booking history
        Intent intent = new Intent(context, com.example.cinemabookingsystemfe.ui.main.MainActivity.class);
        intent.putExtra("OPEN_TAB", "history");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 
            NOTIFICATION_ID_BOOKING_SUCCESS,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_ticket)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setColor(context.getResources().getColor(R.color.primaryColor));
        
        notificationManager.notify(NOTIFICATION_ID_BOOKING_SUCCESS, builder.build());
        
        android.util.Log.d("NotificationHelper", "Booking success notification sent: " + bookingCode);
    }
    
    /**
     * Hiển thị notification hủy vé thành công
     */
    public void showBookingCancelledNotification(String bookingCode) {
        String title = "Hủy vé thành công";
        String message = String.format("Đã hủy đặt vé %s. Tiền sẽ được hoàn lại trong 3-5 ngày làm việc.", bookingCode);
        
        // Intent to open booking history
        Intent intent = new Intent(context, com.example.cinemabookingsystemfe.ui.main.MainActivity.class);
        intent.putExtra("OPEN_TAB", "history");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_BOOKING_CANCELLED,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_cancel)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setColor(context.getResources().getColor(R.color.textSecondary));
        
        notificationManager.notify(NOTIFICATION_ID_BOOKING_CANCELLED, builder.build());
        
        android.util.Log.d("NotificationHelper", "Booking cancelled notification sent: " + bookingCode);
    }
}
