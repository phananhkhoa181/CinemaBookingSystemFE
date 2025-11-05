package com.example.cinemabookingsystemfe.data.models.response;

import java.util.List;

public class SearchMovieData {
    private List<MovieItem> items;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private int totalItems;
    private boolean hasNextPage;
    private boolean hasPreviousPage;

    public SearchMovieData(List<MovieItem> items, int currentPage, int pageSize, int totalPages, int totalItems,
                           boolean hasNextPage, boolean hasPreviousPage) {
        this.items = items;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.hasNextPage = hasNextPage;
        this.hasPreviousPage = hasPreviousPage;
    }

    public List<MovieItem> getItems() { return items; }
    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }
    public int getTotalPages() { return totalPages; }
    public int getTotalItems() { return totalItems; }
    public boolean isHasNextPage() { return hasNextPage; }
    public boolean isHasPreviousPage() { return hasPreviousPage; }
}
