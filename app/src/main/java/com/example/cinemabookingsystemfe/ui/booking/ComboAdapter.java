package com.example.cinemabookingsystemfe.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.Combo;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ComboAdapter - Adapter for combo items in SelectComboActivity
 */
public class ComboAdapter extends RecyclerView.Adapter<ComboAdapter.ComboViewHolder> {

    private List<Combo> combos = new ArrayList<>();
    private Map<Integer, Integer> quantities = new HashMap<>(); // comboId -> quantity
    private OnQuantityChangeListener listener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged(Combo combo, int quantity);
    }

    public void setCombos(List<Combo> combos) {
        this.combos = combos;
        notifyDataSetChanged();
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.listener = listener;
    }

    public Map<Integer, Integer> getQuantities() {
        return quantities;
    }

    public void resetComboQuantity(int comboId) {
        quantities.put(comboId, 0);
        notifyDataSetChanged(); // Refresh all items to update UI
    }

    public Combo getComboById(int comboId) {
        for (Combo combo : combos) {
            if (combo.getComboId() == comboId) {
                return combo;
            }
        }
        return null;
    }

    public List<Combo> getAllCombos() {
        return new ArrayList<>(combos);
    }

    @NonNull
    @Override
    public ComboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_combo, parent, false);
        return new ComboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComboViewHolder holder, int position) {
        Combo combo = combos.get(position);
        holder.bind(combo);
    }

    @Override
    public int getItemCount() {
        return combos.size();
    }

    class ComboViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCombo;
        TextView tvComboName, tvComboDescription, tvComboPrice, tvQuantity;
        MaterialButton btnDecrease, btnIncrease;

        public ComboViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCombo = itemView.findViewById(R.id.ivCombo);
            tvComboName = itemView.findViewById(R.id.tvComboName);
            tvComboDescription = itemView.findViewById(R.id.tvComboDescription);
            tvComboPrice = itemView.findViewById(R.id.tvComboPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }

        public void bind(Combo combo) {
            // Set combo image based on imageUrl
            int imageResId = getImageResource(combo.getImageUrl());
            ivCombo.setImageResource(imageResId);

            // Set combo info
            tvComboName.setText(combo.getName());
            tvComboDescription.setText(combo.getDescription());

            // Format price
            DecimalFormat formatter = new DecimalFormat("#,###");
            tvComboPrice.setText(formatter.format(combo.getPrice()) + "đ");

            // Get current quantity
            int quantity = quantities.getOrDefault(combo.getComboId(), 0);
            tvQuantity.setText(String.valueOf(quantity));

            // Update button states
            updateButtonStates(quantity);

            // Decrease button
            btnDecrease.setOnClickListener(v -> {
                int currentQty = quantities.getOrDefault(combo.getComboId(), 0);
                if (currentQty > 0) {
                    int newQty = currentQty - 1;
                    quantities.put(combo.getComboId(), newQty);
                    tvQuantity.setText(String.valueOf(newQty));
                    updateButtonStates(newQty);

                    if (listener != null) {
                        listener.onQuantityChanged(combo, newQty);
                    }
                }
            });

            // Increase button
            btnIncrease.setOnClickListener(v -> {
                int currentQty = quantities.getOrDefault(combo.getComboId(), 0);
                if (currentQty < 10) { // Max 10 combos
                    int newQty = currentQty + 1;
                    quantities.put(combo.getComboId(), newQty);
                    tvQuantity.setText(String.valueOf(newQty));
                    updateButtonStates(newQty);

                    if (listener != null) {
                        listener.onQuantityChanged(combo, newQty);
                    }
                }
            });
        }

        private void updateButtonStates(int quantity) {
            btnDecrease.setEnabled(quantity > 0);
            btnIncrease.setEnabled(quantity < 10);
        }

        private int getImageResource(String imageUrl) {
            // Map imageUrl to drawable resource
            if (imageUrl == null) return R.drawable.combo1bap2nuoc;

            switch (imageUrl) {
                case "combo1bap2nuoc":
                    return R.drawable.combo1bap2nuoc;
                case "combo1bap1nuoc":
                    return R.drawable.combo1bap1nuoc;
                case "pepsi":
                    return R.drawable.pepsi;
                case "sprite":
                    return R.drawable.sprite;
                case "aqua":
                    return R.drawable.aqua;
                case "baprang":
                    return R.drawable.baprang;
                default:
                    return R.drawable.combo1bap2nuoc;
            }
        }
    }
}
