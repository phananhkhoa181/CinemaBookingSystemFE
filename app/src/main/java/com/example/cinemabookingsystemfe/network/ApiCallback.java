package com.example.cinemabookingsystemfe.network;

public interface ApiCallback<T> {
    void onSuccess(T data);
    void onError(String errorMessage);
}
