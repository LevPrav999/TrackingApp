package ru.predprof.trackingapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.predprof.trackingapp.MainActivity;
import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.databinding.RouteEndLayoutBinding;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;

public class RouteEndFragment extends Fragment {
    private RouteEndLayoutBinding binding;
    private SharedPreferencesManager sharedPreferencesManager;
    int user_complexity = 0;

    private void initFunc(){
        Bundle b = getActivity().getIntent().getExtras();
        binding.lastRoute.setText(b.getString("routeName"));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.complexity,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDifficulty.setAdapter(adapter);
        binding.spinnerDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user_complexity = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.routeLength.setText(getArguments().getString("len"));
        binding.routeEstimatedComplexity.setText(getArguments().getString("dif_aut"));
        binding.lastRoute.setText(getArguments().getString("routeName"));
        binding.toMain.setOnClickListener(view -> {

        });

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RouteEndLayoutBinding.inflate(getLayoutInflater());

        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        initFunc();
        return binding.getRoot();
    }
}
