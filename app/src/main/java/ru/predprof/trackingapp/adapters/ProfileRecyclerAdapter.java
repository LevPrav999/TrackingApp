package ru.predprof.trackingapp.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.models.Trip;


public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.TripsHolder> {
    List<Trip> trips;

    public ProfileRecyclerAdapter(List<Trip> trips) {
        this.trips = trips;
    }


    @NonNull
    @Override
    public TripsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.profile_recycler_item, parent, false);

        return new TripsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.date.setText(trip.weekDay);
        holder.achive.setText(trip.lenKm + "  km за " + trip.getTime() + " min");

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class TripsHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView achive;

        public TripsHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_recycler);
            achive = itemView.findViewById(R.id.achievement);


        }

    }
}
