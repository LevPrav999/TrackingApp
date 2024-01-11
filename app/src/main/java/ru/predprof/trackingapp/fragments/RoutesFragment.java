package ru.predprof.trackingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.adapters.RoutesRecyclerAdapter;
import ru.predprof.trackingapp.adapters.StatisticRecyclerAdapter;
import ru.predprof.trackingapp.databinding.RoutesLayoutBinding;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;

public class RoutesFragment extends Fragment {
    private RoutesLayoutBinding binding;
    List<Trip> lst;
    RoutesRecyclerAdapter adapter;
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
            List<Trip> no_ended = new ArrayList<>();
            for (Trip el : lst) {
                if (Objects.equals(el.ended, "0")) {
                    no_ended.add(el);
                }
            }

            binding.recyclerUserSRoutes.post(new Runnable() {
                @Override
                public void run() {
                    if (!lst.isEmpty()) {
                        adapter = new RoutesRecyclerAdapter(no_ended);
                        binding.recyclerUserSRoutes.setAdapter(adapter);
                        binding.recyclerUserSRoutes.setLayoutManager(new LinearLayoutManager((Context) getActivity()));
                        adapter.notifyDataSetChanged();
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
