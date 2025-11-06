package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * ApiResponse - Generic wrapper cho tất cả API responses
 * Backend uses "status" field, not "success"
 * @param <T> Data type
 */
public class ApiResponse<T> {
    @SerializedName(value = "success", alternate = {"isSuccess"})
    private boolean success;
    
    @SerializedName(value = "status", alternate = {"statusCode"})
    private int status;
    
    private String message;
    private T data;
    
    public ApiResponse() {}
    
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    public boolean isSuccess() {
        // Backend uses "status" field (200, 201, etc) instead of "success" boolean
        // Consider 2xx status codes as success
        return success || (status >= 200 && status < 300);
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
