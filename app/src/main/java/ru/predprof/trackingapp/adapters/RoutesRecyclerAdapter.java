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


public class RoutesRecyclerAdapter extends RecyclerView.Adapter<RoutesRecyclerAdapter.TripsHolder> {
    private final RoutesRecyclerAdapter.OnItemClickListener listener;
    List<Trip> trips;

    public RoutesRecyclerAdapter(List<Trip> trips, OnItemClickListener listener) {
        this.trips = trips;
        this.listener = listener;
    }


    @NonNull
    @Override
    public TripsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.routes_recyclers_item, parent, false);

        return new TripsHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsHolder holder, int position) {
        Trip trip = trips.get(position);
        Log.d("dfghghghnhgff", Integer.toString(trip.number));
        holder.name.setText(trip.name);
        holder.complexity.setText(trip.difficultAuto);
//        holder.tv.setText(gr.name);
//        holder.tv1.setText(gr.id);

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public interface OnItemClickListener {
        void onItemClick(RoutesRecyclerAdapter.TripsHolder item);
    }

    public static class TripsHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView complexity;

        public TripsHolder(@NonNull View itemView, final RoutesRecyclerAdapter.OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name_of_route);
            complexity = itemView.findViewById(R.id.complexity);


            itemView.setOnClickListener(l -> {
                listener.onItemClick(this);
            });

        }

    }
}
