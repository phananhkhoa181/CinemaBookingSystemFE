package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * ApiResponse - Generic wrapper cho tất cả API responses
 * Backend uses "isSuccess" and "statusCode" fields
 * @param <T> Data type
 */
public class ApiResponse<T> {
    @SerializedName("isSuccess")
    private boolean success;
    
    @SerializedName("statusCode")
    private int status;
    
    private String message;
    private T data;
    private Object errors; // Backend trả về errors array
    
    public ApiResponse() {}
    
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    public boolean isSuccess() {
        // Backend uses "isSuccess" boolean field
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
}
