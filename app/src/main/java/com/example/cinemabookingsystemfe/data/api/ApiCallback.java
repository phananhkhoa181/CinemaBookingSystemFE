package com.example.cinemabookingsystemfe.data.api;

/**
 * ApiCallback - Generic callback interface for API responses
 * @param <T> Response data type
 */
public interface ApiCallback<T> {
    void onSuccess(T data);
    void onError(String errorMessage);
}
