package ru.predprof.trackingapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.predprof.trackingapp.MainActivity;
import ru.predprof.trackingapp.databinding.RouteEndLayoutBinding;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;

public class RouteEndFragment extends Fragment {
    private RouteEndLayoutBinding binding;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RouteEndLayoutBinding.inflate(getLayoutInflater());

        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        sharedPreferencesManager.saveInt("lastRouteStatus", 0);
        Bundle b = getActivity().getIntent().getExtras();
        binding.lastRoute.setText(b.getString("routeName"));

        return binding.getRoot();
    }
}
