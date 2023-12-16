package ru.predprof.trackingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.predprof.trackingapp.databinding.PauseRouteLayoutBinding;

public class PauseRouteFragment extends Fragment {
    private PauseRouteLayoutBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = PauseRouteLayoutBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
}