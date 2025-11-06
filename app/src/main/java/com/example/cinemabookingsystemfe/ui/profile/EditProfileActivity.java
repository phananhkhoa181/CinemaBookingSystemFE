package com.example.cinemabookingsystemfe.ui.profile;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.CustomerProfile;
import com.example.cinemabookingsystemfe.data.repository.UserRepository;
import com.example.cinemabookingsystemfe.utils.TokenManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * EditProfileActivity - Màn hình cập nhật thông tin cá nhân
 * Nối API: GET /api/customers/profile, PUT /api/users/{id}, PUT /api/customers/profile
 */
public class EditProfileActivity extends AppCompatActivity {

    // Views
    private ImageView btnBack;
    private TextView tvAvatarInitial; // Avatar with user initials
    private TextInputEditText etFullName;
    private TextInputEditText etPhone;
    private TextInputEditText etAddress;
    private TextInputEditText etDateOfBirth;
    private TextInputEditText etEmail;
    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private RadioButton rbOther;
    private MaterialButton btnUpdate;
    private TextView tvChangePassword;

    // Repository & Token Manager
    private UserRepository userRepository;
    private TokenManager tokenManager;
    
    // Profile data from API
    private CustomerProfile customerProfile;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize repository and token manager
        userRepository = UserRepository.getInstance(this);
        tokenManager = TokenManager.getInstance(this);

        // Initialize views
        initViews();

        // Setup listeners
        setupListeners();

        // Setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải...");
        progressDialog.setCancelable(false);

        // Load profile from API
        loadProfileFromAPI();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvAvatarInitial = findViewById(R.id.tvAvatarInitial);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        etEmail = findViewById(R.id.etEmail);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvChangePassword = findViewById(R.id.tvChangePassword);
    }

    private void setupListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Change avatar - Commented out (not needed)
        /*
        btnChangeAvatar.setOnClickListener(v -> {
            Toast.makeText(this, "Chọn ảnh từ thư viện", Toast.LENGTH_SHORT).show();
            // TODO: Implement image picker
        });
        */

        // Date of birth picker
        etDateOfBirth.setOnClickListener(v -> showDatePicker());

        // Update button
        btnUpdate.setOnClickListener(v -> updateProfile());

        // Change password
        tvChangePassword.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Thay đổi mật khẩu", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to ChangePasswordActivity
        });
    }
    
    /**
     * Load profile từ API
     */
    private void loadProfileFromAPI() {
        progressDialog.show();
        
        userRepository.getCustomerProfile(new UserRepository.ProfileCallback() {
            @Override
            public void onSuccess(CustomerProfile profile) {
                progressDialog.dismiss();
                customerProfile = profile;
                fillFormWithData();
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                // Fallback: Tạo mock profile
                customerProfile = createMockProfile();
                fillFormWithData();
            }
        });
    }
    
    /**
     * Tạo mock profile cho fallback (khi API lỗi)
     */
    private CustomerProfile createMockProfile() {
        CustomerProfile profile = new CustomerProfile();
        profile.setUserId(1);
        profile.setCustomerId(1);
        profile.setFullName("Đoàn Ngọc Trung");
        profile.setEmail("noshibi123@gmail.com");
        profile.setPhone("0787171600");
        profile.setDateOfBirth("2004-02-17");
        profile.setGender("Male");
        profile.setAddress("123 Đường ABC, Quận 1, TP.HCM");
        return profile;
    }

    private void fillFormWithData() {
        if (customerProfile == null) return;
        
        // Set avatar initials
        String initials = getInitials(customerProfile.getFullName());
        tvAvatarInitial.setText(initials);
        
        etFullName.setText(customerProfile.getFullName());
        etPhone.setText(customerProfile.getPhone());
        etAddress.setText(customerProfile.getAddress());
        
        // Format date from "yyyy-MM-dd" to "dd/MM/yyyy"
        String dateOfBirth = customerProfile.getDateOfBirth();
        if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
            try {
                SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = apiFormat.parse(dateOfBirth);
                if (date != null) {
                    etDateOfBirth.setText(displayFormat.format(date));
                }
            } catch (ParseException e) {
                etDateOfBirth.setText(dateOfBirth);
            }
        }
        
        etEmail.setText(customerProfile.getEmail());

        // Set gender - Convert from API format (Male/Female/Other) to Vietnamese
        String gender = customerProfile.getGender();
        if (gender != null) {
            switch (gender) {
                case "Male":
                    rbMale.setChecked(true);
                    break;
                case "Female":
                    rbFemale.setChecked(true);
                    break;
                default:
                    rbOther.setChecked(true);
                    break;
            }
        }
    }
    
    // Get initials from full name (first 2 letters)
    private String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "??";
        }
        
        String[] words = fullName.trim().split("\\s+");
        if (words.length >= 2) {
            // Take first letter of first word and first letter of last word
            return (words[0].substring(0, 1) + words[words.length - 1].substring(0, 1)).toUpperCase();
        } else if (words.length == 1 && words[0].length() >= 2) {
            // Take first 2 letters
            return words[0].substring(0, 2).toUpperCase();
        } else {
            return words[0].substring(0, 1).toUpperCase();
        }
    }

    private void showDatePicker() {
        // Parse current date from etDateOfBirth (dd/MM/yyyy)
        String currentDate = etDateOfBirth.getText().toString();
        int day = 17;
        int month = 1; // Calendar month is 0-indexed
        int year = 2004;

        if (!currentDate.isEmpty()) {
            String[] dateParts = currentDate.split("/");
            if (dateParts.length == 3) {
                try {
                    day = Integer.parseInt(dateParts[0]);
                    month = Integer.parseInt(dateParts[1]) - 1; // Calendar month is 0-indexed
                    year = Integer.parseInt(dateParts[2]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        // Create date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format: dd/MM/yyyy
                    String date = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    etDateOfBirth.setText(date);
                },
                year,
                month,
                day
        );

        // Set max date to today
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private void updateProfile() {
        // Get values from form
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String dateOfBirthDisplay = etDateOfBirth.getText().toString().trim();

        // Get gender - Convert to API format (Male/Female/Other)
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        String gender = "Other";
        if (selectedGenderId == R.id.rbMale) {
            gender = "Male";
        } else if (selectedGenderId == R.id.rbFemale) {
            gender = "Female";
        }

        // Validation
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Vui lòng nhập số điện thoại");
            etPhone.requestFocus();
            return;
        }

        if (!isValidPhone(phone)) {
            etPhone.setError("Số điện thoại không hợp lệ");
            etPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(dateOfBirthDisplay)) {
            etDateOfBirth.setError("Vui lòng chọn ngày sinh");
            etDateOfBirth.requestFocus();
            return;
        }

        // Convert date from "dd/MM/yyyy" to "yyyy-MM-dd" for API
        String dateOfBirthAPI = "";
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = displayFormat.parse(dateOfBirthDisplay);
            if (date != null) {
                dateOfBirthAPI = apiFormat.format(date);
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Định dạng ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get userId from profile or token
        int userId = customerProfile != null ? customerProfile.getUserId() : tokenManager.getUserIdFromToken();
        
        // Call API to update profile
        progressDialog.setMessage("Đang cập nhật...");
        progressDialog.show();
        
        userRepository.updateProfile(userId, fullName, phone, address, dateOfBirthAPI, gender, 
            new UserRepository.UpdateProfileCallback() {
                @Override
                public void onSuccess(CustomerProfile updatedProfile) {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String error) {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                }
            });
    }

    private boolean isValidPhone(String phone) {
        // Simple phone validation (10 digits starting with 0)
        return phone.matches("^0\\d{9}$");
    }
}
