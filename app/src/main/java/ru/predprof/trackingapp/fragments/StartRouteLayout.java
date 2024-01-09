package ru.predprof.trackingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.predprof.trackingapp.MainActivity;
import ru.predprof.trackingapp.databinding.StartRouteLayoutBinding;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;

public class StartRouteLayout extends Fragment {
    private StartRouteLayoutBinding binding;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = StartRouteLayoutBinding.inflate(getLayoutInflater());

        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        sharedPreferencesManager.saveInt("lastRouteStatus", 1);

        return binding.getRoot();
    }
}
