package com.example.cinemabookingsystemfe.data.models.response;

import java.util.List;

/**
 * PagedResult - Pagination wrapper
 * @param <T> Item type
 */
public class PagedResult<T> {
    private List<T> items;
    private int totalCount;
    private int page;
    private int pageSize;
    private int totalPages;
    
    public PagedResult() {}
    
    // Getters and Setters
    public List<T> getItems() {
        return items;
    }
    
    public void setItems(List<T> items) {
        this.items = items;
    }
    
    public int getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
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
}
