package com.example.cinemabookingsystemfe.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.Seat;

import java.util.ArrayList;
import java.util.List;

/**
 * SeatRowAdapter - Adapter cho mỗi hàng ghế (A, B, C...)
 * Mỗi row có: Row Label + Grid 10 ghế
 */
public class SeatRowAdapter extends RecyclerView.Adapter<SeatRowAdapter.RowViewHolder> {

    private List<List<Seat>> seatRows = new ArrayList<>(); // List of rows, mỗi row có 10 ghế
    private String[] rowLabels = {"A", "B", "C", "D", "E", "F", "G", "H"};
    private SeatAdapter.OnSeatClickListener listener;

    public SeatRowAdapter(SeatAdapter.OnSeatClickListener listener) {
        this.listener = listener;
    }

    public void setSeatRows(List<List<Seat>> seatRows) {
        this.seatRows = seatRows;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_seat_row, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
        holder.bind(rowLabels[position], seatRows.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return seatRows.size();
    }

    static class RowViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView rvSeats;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            rvSeats = itemView.findViewById(R.id.rvSeats);
        }

        public void bind(String rowLabel, List<Seat> seats, SeatAdapter.OnSeatClickListener listener) {
            // Setup horizontal grid cho 10 ghế
            SeatAdapter seatAdapter = new SeatAdapter(listener);
            seatAdapter.setSeats(seats);

            GridLayoutManager layoutManager = new GridLayoutManager(itemView.getContext(), 10);
            rvSeats.setLayoutManager(layoutManager);
            rvSeats.setAdapter(seatAdapter);
        }
    }
}
