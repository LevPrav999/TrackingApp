package ru.predprof.trackingapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ru.predprof.trackingapp.MainActivity;
import ru.predprof.trackingapp.NoBarActivity;
import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.databinding.RouteEndLayoutBinding;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;
import ru.predprof.trackingapp.room.TripDao;
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
        int route_id = getArguments().getInt("route_id");
        binding.toMain.setOnClickListener(view -> {
            Thread th = new Thread(() -> {
                Trip a = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getTripById(route_id);
                Trip trip = new Trip();
                trip.setAvgSpeed("0");
                trip.setTime("0");
                Log.d("sdgfd", a.lenKm);
                trip.setLenKm(a.getLenKm());
                trip.setDataPulse(new ArrayList<>());
                trip.setDifficultAuto(a.getDifficultAuto());
                trip.setDifficultReal(Integer.toString(user_complexity));
                trip.setMaxSpeed("0");
                Calendar calendar = Calendar.getInstance();
                String date = new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
                trip.setWeekDay(date);
                trip.setDuration("0");
                trip.setEnded("1");
                trip.setName(a.getName());

                RoomHandler.getInstance(this.getContext()).getAppDatabase().tripDao().insertAll(trip);
                binding.getRoot().post(this::replaceActivity);

            });
            th.start();
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
    private void replaceActivity(){
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(intent);
        this.getActivity().finish();

    }
}
