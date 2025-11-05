package com.example.cinemabookingsystemfe.ui.moviedetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cinemabookingsystemfe.R;

public class MovieTrailerActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Watch Trailer");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);

        // ✅ Nhận trailerUrl từ Intent
        String trailerUrl = getIntent().getStringExtra("trailerUrl");
        if (trailerUrl == null || trailerUrl.isEmpty()) {
            finish();
            return;
        }

        // ✅ Cấu hình WebView
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        // ✅ Load trailer URL
        progressBar.setVisibility(View.VISIBLE);
        webView.loadUrl(trailerUrl);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
