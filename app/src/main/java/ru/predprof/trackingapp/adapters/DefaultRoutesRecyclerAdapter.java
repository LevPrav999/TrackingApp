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
import ru.predprof.trackingapp.models.DefaultTrip;

public class DefaultRoutesRecyclerAdapter extends RecyclerView.Adapter<DefaultRoutesRecyclerAdapter.TripsHolder> {
    List<DefaultTrip> trips;
    private final OnItemClickListener listener;

    public DefaultRoutesRecyclerAdapter(List<DefaultTrip> trips, OnItemClickListener listener) {
        this.trips = trips;
        this.listener = listener;
    }


    @NonNull
    @Override
    public DefaultRoutesRecyclerAdapter.TripsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.routes_recyclers_item, parent, false);

        return new DefaultRoutesRecyclerAdapter.TripsHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DefaultRoutesRecyclerAdapter.TripsHolder holder, int position) {
        DefaultTrip trip = trips.get(position);
        Log.d("dfghghghnhgff", Integer.toString(trip.id));
        holder.name.setText(trip.name);
        holder.complexity.setText(trip.difficultAuto);

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class TripsHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView complexity;

        public TripsHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name_of_route);
            complexity = itemView.findViewById(R.id.complexity);

            itemView.setOnClickListener(l->{
                listener.onItemClick(this);
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(DefaultRoutesRecyclerAdapter.TripsHolder item);
    }
}
