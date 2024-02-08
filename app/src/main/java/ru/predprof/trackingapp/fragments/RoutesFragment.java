package ru.predprof.trackingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.adapters.DefaultRoutesRecyclerAdapter;
import ru.predprof.trackingapp.adapters.RoutesRecyclerAdapter;
import ru.predprof.trackingapp.databinding.RoutesLayoutBinding;
import ru.predprof.trackingapp.models.DefaultTrip;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;
import ru.predprof.trackingapp.room.defaultTrips.DefaultRoomHandler;

public class RoutesFragment extends Fragment {
    List<Trip> lst;
    List<DefaultTrip> lst2;
    RoutesRecyclerAdapter adapter;
    DefaultRoutesRecyclerAdapter adapter2;
    private RoutesLayoutBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = RoutesLayoutBinding.inflate(getLayoutInflater());
        binding.createNewWay.setOnClickListener(a -> replace(new EditRouteFragment()));
        Thread th2 = new Thread(() -> {

            lst = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getAll();
            lst2 = DefaultRoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getAll();
            List<Trip> no_ended = new ArrayList<>();
            for (Trip el : lst) {
                if (Objects.equals(el.ended, "1")) {
                    no_ended.add(el);
                }
            }


            binding.recyclerUserSRoutes.post(new Runnable() {
                @Override
                public void run() {
                    if (!no_ended.isEmpty()) {

                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        binding.recyclerUserSRoutes.setLayoutManager(llm);
                        adapter = new RoutesRecyclerAdapter(no_ended, new RoutesRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(RoutesRecyclerAdapter.TripsHolder item) {
                                Fragment f = new StartRouteLayout();
                                Bundle b = new Bundle();
                                TextView textView = item.itemView.findViewById(R.id.name_of_route);
                                b.putString("routeName", textView.getText().toString());
                                b.putBoolean("isDefaultRoute", false);
                                f.setArguments(b);
                                replace(f);
                            }
                        });
                        binding.recyclerUserSRoutes.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            binding.recyclerRoutesOfApp.post(new Runnable() {
                @Override
                public void run() {
                    if (!lst2.isEmpty()) {

                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        binding.recyclerRoutesOfApp.setLayoutManager(llm);
                        adapter2 = new DefaultRoutesRecyclerAdapter(lst2, new DefaultRoutesRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(DefaultRoutesRecyclerAdapter.TripsHolder item) {
                                Fragment f = new StartRouteLayout();
                                Bundle b = new Bundle();
                                TextView textView = item.itemView.findViewById(R.id.name_of_route);
                                b.putString("routeName", textView.getText().toString());
                                b.putBoolean("isDefaultRoute", true);
                                f.setArguments(b);
                                replace(f);
                            }
                        });
                        binding.recyclerRoutesOfApp.setAdapter(adapter2);
                        adapter2.notifyDataSetChanged();
                    }
                }
            });
        });

        th2.start();

        return binding.getRoot();
    }

    public void replace(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.map, fragment).commit();

    }
}
