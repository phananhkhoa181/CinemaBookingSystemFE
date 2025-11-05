package com.example.cinemabookingsystemfe.ui.moviedetail.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabookingsystemfe.R;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.CinemaShowtimes;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.Showtime;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.ShowtimesDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ViewHolder> {

    private final List<ShowtimesDate> showtimeDates;
    private final OnShowtimeClickListener listener;

    public interface OnShowtimeClickListener {
        void onShowtimeClick(Showtime showtime);
    }

    public ShowtimeAdapter(List<ShowtimesDate> showtimeDates, OnShowtimeClickListener listener) {
        this.showtimeDates = showtimeDates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_showtime_full, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShowtimesDate dateItem = showtimeDates.get(position);
        holder.tvDate.setText("📅 " + dateItem.getDate());
        holder.containerCinemas.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());

        for (CinemaShowtimes cinema : dateItem.getCinemas()) {
            View cinemaView = inflater.inflate(R.layout.item_cinema_block, holder.containerCinemas, false);
            TextView tvCinemaName = cinemaView.findViewById(R.id.tvCinemaName);
            LinearLayout containerLanguages = cinemaView.findViewById(R.id.containerLanguages);

            tvCinemaName.setText("🎬 " + cinema.getName());

            // Group showtimes theo language
            Map<String, List<Showtime>> grouped = new LinkedHashMap<>();
            for (Showtime s : cinema.getShowtimes()) {
                grouped.computeIfAbsent(s.getLanguageType(), k -> new ArrayList<>()).add(s);
            }

            // Render từng nhóm language
            for (Map.Entry<String, List<Showtime>> entry : grouped.entrySet()) {
                View languageView = inflater.inflate(R.layout.item_language_block, containerLanguages, false);
                TextView tvLanguage = languageView.findViewById(R.id.tvLanguageType);
                LinearLayout containerTimes = languageView.findViewById(R.id.containerTimes);

                tvLanguage.setText(entry.getKey());

                for (Showtime s : entry.getValue()) {
                    View timeChip = inflater.inflate(R.layout.item_time_chip, containerTimes, false);
                    TextView tvTime = timeChip.findViewById(R.id.tvShowtime);
                    tvTime.setText(formatTimeRange(s.getStartTime(), s.getEndTime()));

                    timeChip.setOnClickListener(v -> {
                        if (listener != null) listener.onShowtimeClick(s);
                    });

                    containerTimes.addView(timeChip);
                }

                containerLanguages.addView(languageView);
            }

            holder.containerCinemas.addView(cinemaView);
        }
    }

    @Override
    public int getItemCount() {
        return showtimeDates != null ? showtimeDates.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        LinearLayout containerCinemas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            containerCinemas = itemView.findViewById(R.id.containerCinemas);
        }
    }

    private String formatTimeRange(String start, String end) {
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat out = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            return out.format(in.parse(start)) + " - " + out.format(in.parse(end));
        } catch (ParseException e) {
            return start + " - " + end;
        }
    }
}
