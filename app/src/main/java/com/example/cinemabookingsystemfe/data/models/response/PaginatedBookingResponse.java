package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Paginated response wrapper for booking list
 */
public class PaginatedBookingResponse {
    @SerializedName("items")
    private List<BookingListResponse> items;

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

    public List<BookingListResponse> getItems() {
        return items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }
}
