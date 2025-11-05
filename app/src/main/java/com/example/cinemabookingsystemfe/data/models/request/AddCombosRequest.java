package com.example.cinemabookingsystemfe.data.models.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Request model for POST /api/bookings/{id}/add-combos
 */
public class AddCombosRequest {
    
    @SerializedName("combos")
    private List<ComboItem> combos;
    
    public AddCombosRequest(List<ComboItem> combos) {
        this.combos = combos;
    }
    
    public List<ComboItem> getCombos() {
        return combos;
    }
    
    public void setCombos(List<ComboItem> combos) {
        this.combos = combos;
    }
    
    /**
     * Nested class for combo item
     */
    public static class ComboItem {
        @SerializedName("comboid")
        private int comboId;
        
        @SerializedName("quantity")
        private int quantity;
        
        public ComboItem(int comboId, int quantity) {
            this.comboId = comboId;
            this.quantity = quantity;
        }
        
        public int getComboId() {
            return comboId;
        }
        
        public void setComboId(int comboId) {
            this.comboId = comboId;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
