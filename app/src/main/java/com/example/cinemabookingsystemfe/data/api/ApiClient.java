package com.example.cinemabookingsystemfe.data.api;

import android.content.Context;

import com.example.cinemabookingsystemfe.data.api.interceptors.AuthInterceptor;
import com.example.cinemabookingsystemfe.utils.Constants;
import com.example.cinemabookingsystemfe.utils.SharedPrefsManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ApiClient - Retrofit builder (Singleton)
 * 
 * TODO: Initialize trong Application class hoặc MainActivity
 */
public class ApiClient {
    
    private static ApiClient instance;
    private final Retrofit retrofit;
    private final ApiService apiService;
    
    private ApiClient(Context context) {
        // Gson configuration
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat(Constants.DATE_FORMAT_API)
                .create();
        
        // Logging Interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        // OkHttpClient with interceptors
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(SharedPrefsManager.getInstance(context)))
                .addInterceptor(loggingInterceptor)
                .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
        
        // Retrofit builder
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        
        apiService = retrofit.create(ApiService.class);
    }
    
    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context.getApplicationContext());
        }
        return instance;
    }
    
    public ApiService getApiService() {
        return apiService;
    }
}
