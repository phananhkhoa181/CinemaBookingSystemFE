package com.example.cinemabookingsystemfe.ui.moviedetail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.Showtime;

import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {

    private final Context context;
    private final List<Showtime> showtimeList;

    public ShowtimeAdapter(Context context, List<Showtime> showtimeList) {
        this.context = context;
        this.showtimeList = showtimeList;
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime s = showtimeList.get(position);
        holder.tvTime.setText(s.getStartTime().substring(11, 16)); // cắt ra hh:mm
//        holder.tvType.setText(s.getType() + " - " + s.getSubtitle());
//        holder.tvPrice.setText(String.format("%,d₫", s.getPrice()));
        holder.tvAuditorium.setText(s.getAuditorium().getName());
    }

    @Override
    public int getItemCount() {
        return showtimeList.size();
    }

    public static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvType, tvPrice, tvAuditorium;

        public ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAuditorium = itemView.findViewById(R.id.tvAuditorium);
        }
    }
}
