package com.example.cinemabookingsystemfe.ui.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.adapters.SpinnerAdapter;
import com.example.cinemabookingsystemfe.data.models.response.Cinema;
import com.example.cinemabookingsystemfe.data.models.response.Showtime;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * SelectCinemaActivity - Chọn rạp chiếu và suất chiếu cho phim
 */
public class SelectCinemaActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "movie_id";
    public static final String EXTRA_SHOWTIME_ID = "showtime_id";

    private MaterialToolbar toolbar;
    private Spinner spinnerCity;
    private Spinner spinnerCinema;
    private ChipGroup chipGroupDates;
    private TextView tvSelectedDate;
    private RecyclerView rvCinemas;

    private int movieId;
    private String selectedDate;
    private CinemaAdapter adapter;
    private List<Cinema> allCinemas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            android.util.Log.d("SelectCinema", "onCreate started");
            setContentView(R.layout.activity_select_cinema);
            
            movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
            android.util.Log.d("SelectCinema", "Movie ID: " + movieId);
            
            initViews();
            android.util.Log.d("SelectCinema", "Views initialized");
            
            setupToolbar();
            android.util.Log.d("SelectCinema", "Toolbar setup complete");
            
            setupSpinners();
            android.util.Log.d("SelectCinema", "Spinners setup complete");
            
            setupDateChips();
            android.util.Log.d("SelectCinema", "Date chips setup complete");
            
            setupRecyclerView();
            android.util.Log.d("SelectCinema", "RecyclerView setup complete");
            
            loadMockData();
            android.util.Log.d("SelectCinema", "Mock data loaded successfully");
            
        } catch (Exception e) {
            android.util.Log.e("SelectCinema", "ERROR in onCreate: " + e.getMessage(), e);
            e.printStackTrace();
            android.widget.Toast.makeText(this, 
                "Lỗi khởi tạo: " + e.getMessage(), 
                android.widget.Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerCinema = findViewById(R.id.spinnerCinema);
        chipGroupDates = findViewById(R.id.chipGroupDates);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        rvCinemas = findViewById(R.id.rvCinemas);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    
    private void setupSpinners() {
        try {
            // City spinner - chỉ cho phép TP Hồ Chí Minh
            List<String> cities = Arrays.asList("TP Hồ Chí Minh");
            SpinnerAdapter cityAdapter = new SpinnerAdapter(this, cities);
            spinnerCity.setAdapter(cityAdapter);
            android.util.Log.d("SelectCinema", "City spinner setup OK");
        
        // Cinema branch spinner (Movie88 chi nhánh)
        List<String> cinemaBranches = Arrays.asList(
            "Movie88 - Tất cả chi nhánh",
            "Movie88 - Quận 1", 
            "Movie88 - Quận 2",
            "Movie88 - Quận 3",
            "Movie88 - Quận 4",
            "Movie88 - Thủ Đức",
            "Movie88 - Gò Vấp"
        );
            SpinnerAdapter cinemaAdapter = new SpinnerAdapter(this, cinemaBranches);
            spinnerCinema.setAdapter(cinemaAdapter);
            android.util.Log.d("SelectCinema", "Cinema spinner setup OK");
            
            // Add listener to filter cinemas by branch
            spinnerCinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        filterCinemasByBranch(position);
                    } catch (Exception e) {
                        android.util.Log.e("SelectCinema", "Error in filter: " + e.getMessage(), e);
                    }
                }
                
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            
        } catch (Exception e) {
            android.util.Log.e("SelectCinema", "Error in setupSpinners: " + e.getMessage(), e);
            throw e;
        }
    }
    
    private void filterCinemasByBranch(int branchIndex) {
        if (allCinemas == null) return;
        
        List<Cinema> filtered = new ArrayList<>();
        if (branchIndex == 0) {
            // Tất cả chi nhánh
            filtered.addAll(allCinemas);
        } else {
            // Filter by district
            String[] districts = {"Quận 1", "Quận 2", "Quận 3", "Quận 4", "Thủ Đức", "Gò Vấp"};
            String selectedDistrict = districts[branchIndex - 1];
            for (Cinema cinema : allCinemas) {
                if (cinema.getDistrict().contains(selectedDistrict)) {
                    filtered.add(cinema);
                }
            }
        }
        adapter.updateData(filtered);
    }
    
    private void setupDateChips() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd / MM", Locale.getDefault());
        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEEE dd, 'tháng' MM yyyy", new Locale("vi", "VN"));
        Calendar calendar = Calendar.getInstance();
        
        // Generate next 4 days
        for (int i = 0; i < 4; i++) {
            final Calendar chipCalendar = (Calendar) calendar.clone();
            final String chipDate = dateFormat.format(chipCalendar.getTime());
            final String fullDate = fullDateFormat.format(chipCalendar.getTime());
            
            Chip chip = new Chip(this);
            
            // Real-time day name
            String dayText;
            if (i == 0) {
                dayText = "Hôm nay\n" + chipDate;
            } else if (i == 1) {
                dayText = dayNameFormat.format(chipCalendar.getTime()) + "\n" + chipDate;
            } else {
                dayText = dayNameFormat.format(chipCalendar.getTime()) + "\n" + chipDate;
            }
            
            chip.setText(dayText);
            chip.setCheckable(true);
            chip.setChipBackgroundColorResource(R.color.white);
            chip.setTextColor(getColor(R.color.black));
            chip.setChipStrokeWidth(2);
            chip.setChipStrokeColorResource(R.color.border);
            chip.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            
            // BỎ dấu tick
            chip.setCheckedIconVisible(false);
            
            // Fixed chip size - không thay đổi khi select
            chip.setChipMinHeight(60);
            chip.setMinWidth(100);
            chip.setTextSize(12);
            
            if (i == 0) {
                chip.setChecked(true);
                chip.setChipBackgroundColor(getColorStateList(android.R.color.holo_blue_dark));
                chip.setTextColor(getColor(R.color.white));
                selectedDate = chipDate;
                tvSelectedDate.setText(fullDate);
            }
            
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Chỉ đổi màu, không thay đổi size
                    chip.setChipBackgroundColor(getColorStateList(android.R.color.holo_blue_dark));
                    chip.setTextColor(getColor(R.color.white));
                    selectedDate = chipDate;
                    tvSelectedDate.setText(fullDate);
                    
                    // Filter showtimes by selected date
                    filterShowtimesByDate(chipCalendar);
                } else {
                    chip.setChipBackgroundColorResource(R.color.white);
                    chip.setTextColor(getColor(R.color.black));
                }
            });
            
            chipGroupDates.addView(chip);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    
    private void setupRecyclerView() {
        adapter = new CinemaAdapter(allCinemas, (showtime, cinema) -> onShowtimeClick(showtime, cinema));
        rvCinemas.setLayoutManager(new LinearLayoutManager(this));
        rvCinemas.setAdapter(adapter);
    }
    
    private void loadMockData() {
        allCinemas = new ArrayList<>();
        
        // Mock Cinema 1 - Thủ Đức
        Cinema cinema1 = new Cinema(1, "Movie88 Co.opXtra Linh Trung", 
            "Số 1, Khu phố 6", "Thủ Đức", "TP Hồ Chí Minh");
        cinema1.setDistance(6.9);
        List<Showtime> showtimes1 = new ArrayList<>();
        showtimes1.add(createShowtime(1, 1, "22:15", "2D PHỤ ĐỀ", 80000));
        cinema1.setShowtimes(showtimes1);
        allCinemas.add(cinema1);
        
        // Mock Cinema 2 - Quận 1 (with both 2D and 3D formats)
        Cinema cinema2 = new Cinema(2, "Movie88 Nguyễn Du",
            "116 Nguyễn Du", "Quận 1", "TP Hồ Chí Minh");
        cinema2.setDistance(16.6);
        List<Showtime> showtimes2 = new ArrayList<>();
        showtimes2.add(createShowtime(2, 2, "14:30", "2D PHỤ ĐỀ", 75000));
        showtimes2.add(createShowtime(3, 2, "18:45", "2D PHỤ ĐỀ", 85000));
        showtimes2.add(createShowtime(7, 2, "16:00", "3D PHỤ ĐỀ", 95000));
        showtimes2.add(createShowtime(8, 2, "20:30", "3D PHỤ ĐỀ", 105000));
        cinema2.setShowtimes(showtimes2);
        allCinemas.add(cinema2);
        
        // Mock Cinema 3 - Gò Vấp
        Cinema cinema3 = new Cinema(3, "Movie88 Quang Trung",
            "Ngã tư Quang Trung - Trường Chinh", "Gò Vấp", "TP Hồ Chí Minh");
        cinema3.setDistance(18.3);
        List<Showtime> showtimes3 = new ArrayList<>();
        showtimes3.add(createShowtime(4, 3, "16:00", "2D PHỤ ĐỀ", 70000));
        cinema3.setShowtimes(showtimes3);
        allCinemas.add(cinema3);
        
        // Mock Cinema 4 - Quận 2
        Cinema cinema4 = new Cinema(4, "Movie88 Thảo Điền",
            "253 Nguyễn Văn Hưởng", "Quận 2", "TP Hồ Chí Minh");
        cinema4.setDistance(12.5);
        List<Showtime> showtimes4 = new ArrayList<>();
        showtimes4.add(createShowtime(5, 4, "15:00", "2D PHỤ ĐỀ", 75000));
        showtimes4.add(createShowtime(6, 4, "20:30", "2D PHỤ ĐỀ", 85000));
        cinema4.setShowtimes(showtimes4);
        allCinemas.add(cinema4);
        
        adapter.updateData(allCinemas);
    }
    
    private Showtime createShowtime(int id, int cinemaId, String time, String format, double price) {
        Showtime showtime = new Showtime();
        showtime.setShowtimeId(id);
        showtime.setCinemaId(cinemaId);
        showtime.setTime(time);
        showtime.setFormat(format);
        showtime.setPrice(price);
        showtime.setAvailableSeats(50);
        return showtime;
    }
    
    private void filterShowtimesByDate(Calendar selectedCalendar) {
        // Get day of week (0 = Sunday, 1 = Monday, etc.)
        int dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK);
        
        // Update showtimes based on selected date
        for (Cinema cinema : allCinemas) {
            List<Showtime> newShowtimes = new ArrayList<>();
            
            // Generate different showtimes for different days
            if (cinema.getCinemaId() == 1) { // Movie88 Thủ Đức
                if (dayOfWeek == Calendar.SUNDAY) {
                    newShowtimes.add(createShowtime(1, 1, "10:00", "2D PHỤ ĐỀ", 60000));
                    newShowtimes.add(createShowtime(101, 1, "22:15", "2D PHỤ ĐỀ", 80000));
                } else {
                    newShowtimes.add(createShowtime(1, 1, "22:15", "2D PHỤ ĐỀ", 80000));
                }
            } else if (cinema.getCinemaId() == 2) { // Movie88 Nguyễn Du
                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    newShowtimes.add(createShowtime(2, 2, "10:30", "2D PHỤ ĐỀ", 70000));
                    newShowtimes.add(createShowtime(3, 2, "14:30", "2D PHỤ ĐỀ", 75000));
                    newShowtimes.add(createShowtime(102, 2, "18:45", "2D PHỤ ĐỀ", 85000));
                    newShowtimes.add(createShowtime(7, 2, "13:00", "3D PHỤ ĐỀ", 95000));
                    newShowtimes.add(createShowtime(8, 2, "16:00", "3D PHỤ ĐỀ", 95000));
                    newShowtimes.add(createShowtime(103, 2, "20:30", "3D PHỤ ĐỀ", 105000));
                } else {
                    newShowtimes.add(createShowtime(2, 2, "14:30", "2D PHỤ ĐỀ", 75000));
                    newShowtimes.add(createShowtime(3, 2, "18:45", "2D PHỤ ĐỀ", 85000));
                    newShowtimes.add(createShowtime(7, 2, "16:00", "3D PHỤ ĐỀ", 95000));
                    newShowtimes.add(createShowtime(8, 2, "20:30", "3D PHỤ ĐỀ", 105000));
                }
            } else if (cinema.getCinemaId() == 3) { // Movie88 Quang Trung
                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    newShowtimes.add(createShowtime(4, 3, "11:00", "2D PHỤ ĐỀ", 65000));
                    newShowtimes.add(createShowtime(104, 3, "16:00", "2D PHỤ ĐỀ", 70000));
                    newShowtimes.add(createShowtime(105, 3, "21:00", "2D PHỤ ĐỀ", 75000));
                } else {
                    newShowtimes.add(createShowtime(4, 3, "16:00", "2D PHỤ ĐỀ", 70000));
                }
            } else if (cinema.getCinemaId() == 4) { // Movie88 Thảo Điền
                newShowtimes.add(createShowtime(5, 4, "15:00", "2D PHỤ ĐỀ", 75000));
                newShowtimes.add(createShowtime(6, 4, "20:30", "2D PHỤ ĐỀ", 85000));
                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    newShowtimes.add(createShowtime(106, 4, "10:00", "2D PHỤ ĐỀ", 65000));
                }
            }
            
            cinema.setShowtimes(newShowtimes);
        }
        
        // Re-filter by selected branch
        int currentBranchIndex = spinnerCinema.getSelectedItemPosition();
        filterCinemasByBranch(currentBranchIndex);
    }
    
    private void onShowtimeClick(Showtime showtime, Cinema cinema) {
        Intent intent = new Intent(this, SelectSeatActivity.class);
        intent.putExtra(SelectSeatActivity.EXTRA_SHOWTIME_ID, showtime.getShowtimeId());
        intent.putExtra(SelectSeatActivity.EXTRA_MOVIE_TITLE, "Phá Đám: Sinh Nhật Mẹ"); // TODO: Get from movieId
        intent.putExtra(SelectSeatActivity.EXTRA_CINEMA_NAME, cinema.getName());
        intent.putExtra(SelectSeatActivity.EXTRA_SHOWTIME_FORMAT, showtime.getFormat());
        intent.putExtra(SelectSeatActivity.EXTRA_SHOWTIME_TIME, showtime.getTime());
        intent.putExtra(SelectSeatActivity.EXTRA_MOVIE_RATING, "C13"); // TODO: Get from movie API
        
        // Get selected date from chip
        String selectedDateStr = tvSelectedDate.getText().toString();
        intent.putExtra(SelectSeatActivity.EXTRA_SHOWTIME_DATE, selectedDateStr);
        
        startActivity(intent);
    }
}
