package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * PagedResult - Pagination wrapper matching API response
 * @param <T> Item type
 */
public class PagedResult<T> {
    
    @SerializedName("items")
    private List<T> items;
    
    @SerializedName("currentPage")
    private int currentPage;
    
    @SerializedName("pageSize")
    private int pageSize;
    
    @SerializedName("totalPages")
    private int totalPages;
    
    @SerializedName("totalItems")
    private int totalItems;
    
    @SerializedName("hasNextPage")
    private boolean hasNextPage;
    
    @SerializedName("hasPreviousPage")
    private boolean hasPreviousPage;
    
    public PagedResult() {}
    
    // Getters and Setters
    public List<T> getItems() {
        return items;
    }
    
    public void setItems(List<T> items) {
        this.items = items;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public int getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
    
    public boolean isHasNextPage() {
        return hasNextPage;
    }
    
    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
    
    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }
    
    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }
}
