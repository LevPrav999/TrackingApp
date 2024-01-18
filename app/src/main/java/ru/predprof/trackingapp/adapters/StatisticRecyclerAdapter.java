package ru.predprof.trackingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.models.Trip;

public class StatisticRecyclerAdapter extends RecyclerView.Adapter<StatisticRecyclerAdapter.TripsStatHolder> {
    List<Trip> trips;
    Context context;
    OnClickShowListener onClickShowListener;

    public StatisticRecyclerAdapter(OnClickShowListener onClickShowListener) {
        this.onClickShowListener = onClickShowListener;
        this.context = context;

    }

    public StatisticRecyclerAdapter(List<Trip> trips) {
        this.trips = trips;
    }


    @NonNull
    @Override
    public TripsStatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.main_recycler_item, parent, false);

        return new TripsStatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsStatHolder holder, int position) {
        Trip trip = trips.get(position);
        if (Objects.equals(trip.difficultReal, "0")) {
            holder.dificult.setText(trip.difficultAuto);
        } else {
            holder.dificult.setText(trip.difficultReal);
        }
        holder.startPoint.setText(trip.getName());

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public interface OnClickShowListener {
        void onClickShowListener(int position);

    }

    public static class TripsStatHolder extends RecyclerView.ViewHolder {

        TextView dificult;
        TextView startPoint;
        TextView endPoint;

        public TripsStatHolder(@NonNull View itemView) {
            super(itemView);
            dificult = itemView.findViewById(R.id.complexity);
            startPoint = itemView.findViewById(R.id.departure_point);
            endPoint = itemView.findViewById(R.id.arrivals_point);


        }

    }
}