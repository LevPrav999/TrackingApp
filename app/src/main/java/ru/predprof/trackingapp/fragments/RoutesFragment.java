package ru.predprof.trackingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import ru.predprof.trackingapp.databinding.RoutesLayoutBinding;

public class RoutesFragment extends Fragment {
    private RoutesLayoutBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = RoutesLayoutBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

}
