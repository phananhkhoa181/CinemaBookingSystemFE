package com.example.cinemabookingsystemfe.ui.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.api.ApiCallback;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.Cinema;
import com.example.cinemabookingsystemfe.data.repository.CinemaRepository;
import com.example.cinemabookingsystemfe.ui.adapters.CinemaAdapter;
import com.example.cinemabookingsystemfe.utils.LocationHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * CinemasFragment - Displays list of all cinemas with distance and map integration
 */
public class CinemasFragment extends Fragment {
    
    private static final String TAG = "CinemasFragment";
    
    private RecyclerView rvCinemas;
    private ProgressBar progressBar;
    private View layoutEmptyState;
    
    private CinemaAdapter adapter;
    private CinemaRepository cinemaRepository;
    private LocationHelper locationHelper;
    
    private double userLatitude = 0;
    private double userLongitude = 0;
    private boolean hasUserLocation = false;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinemas, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupLocationHelper();
        loadCinemas();
        
        return view;
    }
    
    private void initViews(View view) {
        rvCinemas = view.findViewById(R.id.rvCinemas);
        progressBar = view.findViewById(R.id.progressBar);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);
    }
    
    private void setupRecyclerView() {
        adapter = new CinemaAdapter(new ArrayList<>(), this::onCinemaClick);
        rvCinemas.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCinemas.setAdapter(adapter);
    }
    
    private void setupLocationHelper() {
        locationHelper = new LocationHelper(requireContext());
        requestUserLocation();
    }
    
    private void loadCinemas() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        
        cinemaRepository = CinemaRepository.getInstance(requireContext());
        cinemaRepository.getAllCinemas(new ApiCallback<ApiResponse<List<Cinema>>>() {
            @Override
            public void onSuccess(ApiResponse<List<Cinema>> response) {
                if (response.getData() != null && !response.getData().isEmpty()) {
                    Log.d(TAG, "✅ Loaded " + response.getData().size() + " cinemas");
                    
                    // Calculate distances if we have user location
                    if (hasUserLocation) {
                        calculateDistances(response.getData());
                    }
                    
                    adapter.updateData(response.getData());
                    showContent();
                } else {
                    showEmptyState();
                }
                
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
            
            @Override
            public void onError(String error) {
                Log.e(TAG, "❌ Failed to load cinemas: " + error);
                Toast.makeText(requireContext(), "Không thể tải danh sách rạp", Toast.LENGTH_SHORT).show();
                showEmptyState();
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
        });
    }
    
    private void requestUserLocation() {
        if (locationHelper.hasLocationPermission()) {
            getUserLocation();
        } else {
            locationHelper.requestLocationPermission(requireActivity());
        }
    }
    
    private void getUserLocation() {
        Log.d(TAG, "Getting user location...");
        
        locationHelper.getCurrentLocation(new LocationHelper.LocationListener() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                userLatitude = latitude;
                userLongitude = longitude;
                hasUserLocation = true;
                Log.d(TAG, "✅ User location: " + latitude + ", " + longitude);
                
                // Reload cinemas with distance calculation
                loadCinemas();
            }
            
            @Override
            public void onLocationError(String error) {
                Log.e(TAG, "❌ Location error: " + error);
                hasUserLocation = false;
            }
        });
    }
    
    private void calculateDistances(List<Cinema> cinemas) {
        for (Cinema cinema : cinemas) {
            if (cinema.getLatitude() != 0 && cinema.getLongitude() != 0) {
                double distance = locationHelper.calculateDistance(
                    userLatitude, userLongitude,
                    cinema.getLatitude(), cinema.getLongitude()
                );
                cinema.setDistance(distance);
                Log.d(TAG, cinema.getName() + " - Distance: " + String.format("%.2f km", distance));
            }
        }
        
        // Sort cinemas by distance (nearest first)
        cinemas.sort((c1, c2) -> {
            Double d1 = c1.getDistance();
            Double d2 = c2.getDistance();
            
            // Handle null distances (put at end)
            if (d1 == null || d1 == 0) return 1;
            if (d2 == null || d2 == 0) return -1;
            
            return Double.compare(d1, d2);
        });
        
        Log.d(TAG, "📍 Cinemas sorted by distance (nearest first)");
    }
    
    private void onCinemaClick(Cinema cinema) {
        // Open Google Maps with cinema location
        double lat = cinema.getLatitude();
        double lng = cinema.getLongitude();
        
        if (lat != 0 && lng != 0) {
            // Format: geo:latitude,longitude?q=latitude,longitude(label)
            String label = Uri.encode(cinema.getName());
            String uri = String.format("geo:%f,%f?q=%f,%f(%s)", lat, lng, lat, lng, label);
            
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps"); // Force Google Maps
            
            try {
                startActivity(intent);
                Log.d(TAG, "Opening Google Maps for: " + cinema.getName());
            } catch (Exception e) {
                // Fallback to browser if Google Maps not installed
                String browserUri = "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lng;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(browserUri));
                startActivity(browserIntent);
            }
        } else {
            Toast.makeText(requireContext(), "Không có thông tin vị trí", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showEmptyState() {
        if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.VISIBLE);
        if (rvCinemas != null) rvCinemas.setVisibility(View.GONE);
    }
    
    private void showContent() {
        if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.GONE);
        if (rvCinemas != null) rvCinemas.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LocationHelper.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                Log.w(TAG, "Location permission denied");
                hasUserLocation = false;
            }
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // LocationHelper doesn't need cleanup
    }
}
