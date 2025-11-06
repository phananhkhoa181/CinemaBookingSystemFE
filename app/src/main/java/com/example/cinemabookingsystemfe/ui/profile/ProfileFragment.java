package com.example.cinemabookingsystemfe.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.CustomerProfile;
import com.example.cinemabookingsystemfe.data.repository.UserRepository;
import com.example.cinemabookingsystemfe.ui.auth.LoginActivity;
import com.example.cinemabookingsystemfe.utils.SharedPrefsManager;
// import com.google.android.material.tabs.TabLayout; // Not used - tabLayout ID is MaterialCardView

/**
 * ProfileFragment - Màn hình thông tin tài khoản
 * Hiển thị thông tin user, membership tier, loyalty points
 * Nối API: GET /api/customers/profile
 */
public class ProfileFragment extends Fragment {
    
    private static final String TAG = "ProfileFragment";

    // Views
    private TextView tvAvatarInitial;
    private TextView tvFullName;
    private TextView tvMembershipTier;
    private TextView tvTotalSpending;
    private View progressForeground;
    // private TabLayout tabLayout; // Removed - ID collision with MaterialCardView
    private ImageView btnSettings;
    private ProgressBar loadingProgress;

    // Repository
    private UserRepository userRepository;
    private com.example.cinemabookingsystemfe.data.repository.BookingRepository bookingRepository;
    
    // Profile data from API
    private CustomerProfile customerProfile;
    private double totalSpending = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize repository
        userRepository = UserRepository.getInstance(requireContext());
        bookingRepository = com.example.cinemabookingsystemfe.data.repository.BookingRepository.getInstance(requireContext());

        // Initialize views
        initViews(view);

        // Setup listeners - Pass view to method
        setupListeners(view);

        // Load profile from API
        loadProfileFromAPI();

        return view;
    }

    private void initViews(View view) {
        tvAvatarInitial = view.findViewById(R.id.tvAvatarInitial);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvMembershipTier = view.findViewById(R.id.tvMembershipTier);
        tvTotalSpending = view.findViewById(R.id.tvTotalSpending);
        progressForeground = view.findViewById(R.id.progressForeground);
        // tabLayout = view.findViewById(R.id.tabLayout); // Removed - ID is MaterialCardView, not TabLayout
        btnSettings = view.findViewById(R.id.btnSettings);
        // loadingProgress = view.findViewById(R.id.loadingProgress); // TODO: Add to layout if needed
    }
    
    /**
     * Load profile từ API
     */
    private void loadProfileFromAPI() {
        Log.d(TAG, "loadProfileFromAPI() - Bắt đầu load profile");
        
        // Show loading (nếu có)
        if (loadingProgress != null) {
            loadingProgress.setVisibility(View.VISIBLE);
        }
        
        // Load profile
        userRepository.getCustomerProfile(new UserRepository.ProfileCallback() {
            @Override
            public void onSuccess(CustomerProfile profile) {
                Log.d(TAG, "✅ API SUCCESS - Profile loaded: " + profile.getFullName());
                
                // Save profile data
                customerProfile = profile;
                
                // Load bookings để tính tổng chi tiêu
                loadTotalSpendingFromBookings();
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "❌ API ERROR - Failed to load profile: " + error);
                
                // Hide loading
                if (loadingProgress != null && isAdded()) {
                    loadingProgress.setVisibility(View.GONE);
                }
                
                if (isAdded()) {
                    // Hiển thị dialog với lỗi chi tiết
                    new AlertDialog.Builder(getContext())
                        .setTitle("⚠️ Lỗi API Profile")
                        .setMessage("Không thể tải thông tin từ server:\n\n" + error + "\n\nHiển thị dữ liệu demo.")
                        .setPositiveButton("Đăng nhập lại", (dialog, which) -> {
                            // Navigate to login
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .setNegativeButton("Đóng", null)
                        .show();
                    
                    // Hiển thị dữ liệu mock nếu API lỗi (fallback)
                    Log.w(TAG, "⚠️ Fallback to MOCK DATA");
                    customerProfile = createMockProfile();
                    totalSpending = 349000; // Mock spending
                    displayUserProfile();
                }
            }
        });
    }
    
    /**
     * Load tất cả bookings để tính tổng chi tiêu
     */
    private void loadTotalSpendingFromBookings() {
        Log.d(TAG, "loadTotalSpendingFromBookings() - Bắt đầu load bookings");
        
        bookingRepository.getMyBookings(1, 100, null, new com.example.cinemabookingsystemfe.data.api.ApiCallback<com.example.cinemabookingsystemfe.data.models.response.ApiResponse<com.example.cinemabookingsystemfe.data.models.response.PaginatedBookingResponse>>() {
            @Override
            public void onSuccess(com.example.cinemabookingsystemfe.data.models.response.ApiResponse<com.example.cinemabookingsystemfe.data.models.response.PaginatedBookingResponse> response) {
                Log.d(TAG, "✅ Bookings API SUCCESS");
                
                // Hide loading
                if (loadingProgress != null && isAdded()) {
                    loadingProgress.setVisibility(View.GONE);
                }
                
                // Tính tổng chi tiêu từ tất cả bookings (trừ booking đã hủy)
                totalSpending = 0;
                if (response.getData() != null && response.getData().getItems() != null) {
                    int bookingCount = response.getData().getItems().size();
                    int cancelledCount = 0;
                    for (com.example.cinemabookingsystemfe.data.models.response.BookingListResponse booking : response.getData().getItems()) {
                        // Chỉ tính booking không bị hủy
                        if (!"Cancelled".equalsIgnoreCase(booking.getStatus())) {
                            totalSpending += booking.getTotalAmount();
                        } else {
                            cancelledCount++;
                        }
                    }
                    Log.d(TAG, "📊 Tính toán: " + bookingCount + " bookings (" + cancelledCount + " đã hủy), Tổng chi tiêu = " + totalSpending + "đ");
                } else {
                    Log.w(TAG, "⚠️ Không có booking nào");
                }
                
                // Display profile với total spending thực
                if (isAdded()) {
                    displayUserProfile();
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "❌ Bookings API ERROR: " + error);
                
                // Hide loading
                if (loadingProgress != null && isAdded()) {
                    loadingProgress.setVisibility(View.GONE);
                }
                
                // Fallback: Hiển thị với spending = 0
                totalSpending = 0;
                Log.w(TAG, "⚠️ Fallback: totalSpending = 0");
                
                if (isAdded()) {
                    displayUserProfile();
                }
            }
        });
    }
    
    /**
     * Tạo mock profile cho fallback (khi API lỗi)
     */
    private CustomerProfile createMockProfile() {
        CustomerProfile profile = new CustomerProfile();
        profile.setFullName("Đoàn Ngọc Trung");
        profile.setEmail("noshibi123@gmail.com");
        profile.setPhone("0787171600");
        profile.setDateOfBirth("2004-02-17");
        profile.setGender("Male");
        profile.setAddress("123 Đường ABC, Quận 1, TP.HCM");
        return profile;
    }

    private void setupListeners(View view) {
        // Settings
        btnSettings.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Cài đặt", Toast.LENGTH_SHORT).show();
        });

        // Tab "Thông tin" - Mở EditProfileActivity
        View tabInfo = view.findViewById(R.id.tabInfo);
        if (tabInfo != null) {
            tabInfo.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            });
        }
        
        // Tab "Thông báo" - Chưa có chức năng
        View tabNotification = view.findViewById(R.id.tabNotification);
        if (tabNotification != null) {
            tabNotification.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Chức năng thông báo đang phát triển", Toast.LENGTH_SHORT).show();
            });
        }
        
        // Logout button
        View btnLogout = view.findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> showLogoutDialog());
        }
    }

    private void displayUserProfile() {
        if (customerProfile == null) return;
        
        Log.d(TAG, "displayUserProfile() - Hiển thị: " + customerProfile.getFullName() + ", Chi tiêu: " + totalSpending);
        
        // Avatar initial (first 2 letters)
        String initials = getInitials(customerProfile.getFullName());
        tvAvatarInitial.setText(initials);

        // Full name
        tvFullName.setText(customerProfile.getFullName());

        // Tính membership tier dựa trên tổng chi tiêu
        String tier = calculateMembershipTier(totalSpending);
        String tierDisplay = getTierEmoji(tier) + " " + tier;
        tvMembershipTier.setText(tierDisplay);
        
        Log.d(TAG, "🏆 Membership Tier: " + tier);

        // Hiển thị tổng chi tiêu từ bookings
        tvTotalSpending.setText(formatCurrency(totalSpending));

        // Update progress bar với milestone 1.5M
        updateProgressBar(totalSpending);
    }
    
    /**
     * Tính membership tier dựa trên tổng chi tiêu
     * Star: 0đ - 1tr
     * Gold: 1tr - 2tr
     * Platinum: > 2tr
     */
    private String calculateMembershipTier(double spending) {
        if (spending >= 2000000) {
            return "Platinum";
        } else if (spending >= 1000000) {
            return "Gold";
        } else {
            return "Star";
        }
    }

    private String getInitials(String fullName) {
        String[] words = fullName.trim().split("\\s+");
        if (words.length >= 2) {
            return (words[0].charAt(0) + "" + words[words.length - 1].charAt(0)).toUpperCase();
        } else if (words.length == 1) {
            return words[0].substring(0, Math.min(2, words[0].length())).toUpperCase();
        }
        return "NA";
    }

    private String getTierEmoji(String tier) {
        switch (tier) {
            case "Star":
                return "⭐";
            case "Gold":
                return "🥇";
            case "Platinum":
                return "💎";
            default:
                return "⭐";
        }
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0fđ", amount);
    }

    private void updateProgressBar(double spending) {
        // Calculate progress percentage based on NEW milestones
        // Milestone 1 (Star): 0đ - 1tr (0-50%)
        // Milestone 2 (Gold): 1tr - 2tr (50-100%)
        // Milestone 3 (Platinum): 2tr+ (100%)
        
        int progressPercent;
        if (spending >= 2000000) {
            // Platinum tier - 100%
            progressPercent = 100;
        } else if (spending >= 1000000) {
            // Gold tier - 50% to 100%
            // 1tr -> 2tr maps to 50% -> 100%
            progressPercent = 50 + (int) ((spending - 1000000) / 1000000 * 50);
        } else {
            // Star tier - 0% to 50%
            // 0 - 1tr: maps to 0% - 50%
            progressPercent = (int) (spending / 1000000 * 50);
        }

        Log.d(TAG, "💰 Progress Bar: Spending = " + spending + "đ, Progress = " + progressPercent + "%");

        // Update progress bar width
        if (progressForeground != null) {
            ViewGroup.LayoutParams params = progressForeground.getLayoutParams();
            // Get parent width and calculate progress width
            progressForeground.post(() -> {
                View parent = (View) progressForeground.getParent();
                int parentWidth = parent.getWidth();
                params.width = parentWidth * progressPercent / 100;
                progressForeground.setLayoutParams(params);
            });
        }

        // Update marker opacity based on spending milestones
        TextView marker0 = getView().findViewById(R.id.marker0);
        TextView marker1m = getView().findViewById(R.id.marker1m);
        TextView marker2m = getView().findViewById(R.id.marker2m);

        // marker0 (0đ) - always achieved (full opacity)
        if (marker0 != null) {
            marker0.setAlpha(1.0f);
        }

        // marker1m (1tr) - achieved if spending >= 1,000,000
        if (marker1m != null) {
            marker1m.setAlpha(spending >= 1000000 ? 1.0f : 0.3f);
        }

        // marker2m (2tr) - achieved if spending >= 2,000,000
        if (marker2m != null) {
            marker2m.setAlpha(spending >= 2000000 ? 1.0f : 0.3f);
        }

        Log.d(TAG, "💰 Markers - 0đ: 100%, 1tr: " + (spending >= 1000000 ? "100%" : "30%") + ", 2tr: " + (spending >= 2000000 ? "100%" : "30%"));
    }

    private void showMembershipCodeDialog() {
        String memberCode = "MEMBER" + (customerProfile != null ? customerProfile.getCustomerId() : "001");
        
        new AlertDialog.Builder(getContext())
                .setTitle("Mã Thành Viên")
                .setMessage("Mã: " + memberCode + "\n\nQuét mã QR này tại rạp để tích điểm!")
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Đăng Xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng Xuất", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void performLogout() {
        // Clear shared preferences
        SharedPrefsManager sharedPrefsManager = SharedPrefsManager.getInstance(requireContext());
        sharedPrefsManager.clearAll();

        // Navigate to login
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        Toast.makeText(getContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning from EditProfileActivity
        loadProfileFromAPI();
    }
}

