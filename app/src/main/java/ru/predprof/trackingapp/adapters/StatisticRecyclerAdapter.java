package ru.predprof.trackingapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.models.Trip;

public class StatisticRecyclerAdapter extends RecyclerView.Adapter<StatisticRecyclerAdapter.TripsStatHolder> {
    List<Trip> trips;

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
        holder.dificult.setText(trip.difficultReal); // TODO add startpoint and endpoint to DB

//        holder.tv.setText(gr.name);
//        holder.tv1.setText(gr.id);

    }

    @Override
    public int getItemCount() {
        Log.d("tyyyyyyyyyyyy", Integer.toString(trips.size()));
        return trips.size();
    }

    public static class TripsStatHolder extends RecyclerView.ViewHolder {

        TextView dificult;

        public TripsStatHolder(@NonNull View itemView) {
            super(itemView);
            dificult = itemView.findViewById(R.id.complexity);


        }

    }
}