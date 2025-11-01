package com.example.cinemabookingsystemfe.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.cinemabookingsystemfe.R;

/**
 * HomeFragment - Trang chủ (Banner, Now Showing, Coming Soon)
 * 
 * TODO:
 * - Create layout: fragment_home.xml
 * - ViewPager2 for banner with auto-scroll
 * - RecyclerView HORIZONTAL for Now Showing movies
 * - RecyclerView HORIZONTAL for Coming Soon movies
 * - Implement MovieAdapter
 * - Implement BannerAdapter
 * 
 * ASSIGNED TO: Developer 2
 * PRIORITY: HIGH
 * REFER TO: docs-FE/03-Screens/02-Main.md (Line 150-800)
 */
public class HomeFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // TODO: Implement
        
        return view;
    }
}
