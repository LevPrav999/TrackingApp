package ru.predprof.trackingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.predprof.trackingapp.MainActivity;
import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.databinding.RouteEndLayoutBinding;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.presentation.api.Controller;
import ru.predprof.trackingapp.room.RoomHandler;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;

public class RouteEndFragment extends Fragment implements
        OnMapReadyCallback {
    int user_complexity = 0;
    int max = 0;
    int min = 0;
    int avg = 0;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

        }
    };
    private RouteEndLayoutBinding binding;
    private SharedPreferencesManager sharedPreferencesManager;
    private GoogleMap map;
    private Trip endedTrip;
    private ArrayList<LatLng> polylines;
    private ArrayList<LatLng> steps;
    private FusedLocationProviderClient mFusedLocationClient;

    private void initFunc() {
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

        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        Thread th = new Thread(() -> {
            Trip trip = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getTripByName(getArguments().getString("routeName"));
            h.post(() -> {
                endedTrip = trip;
                binding.routeLength.setText(trip.getLenKm());
                binding.routeEstimatedComplexity.setText(trip.getDifficultAuto());
                binding.lastRoute.setText(trip.getName());
                binding.routeDuration.setText(Integer.toString(b.getInt("dur") / 60) + " min");
            });
        });

        th.start();

        binding.toMain.setOnClickListener(view -> {
            Thread th2 = new Thread(() -> {
                Trip trip = new Trip();
                trip.setAvgSpeed(endedTrip.getAvgSpeed());
                trip.setTime(Integer.toString(b.getInt("dur") / 60));
                Log.d("asdfghjkl", trip.getTime());
                trip.setLenKm(endedTrip.getLenKm());
                trip.setDataPulse(new ArrayList<>());
                trip.setDifficultAuto(endedTrip.getDifficultAuto());
                trip.setDifficultReal(Integer.toString(user_complexity));
                trip.setMaxSpeed(endedTrip.getMaxSpeed());
                Calendar calendar = Calendar.getInstance();
                String date = new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
                trip.setWeekDay(date);
                trip.setDuration(endedTrip.getDuration());
                trip.setEnded("1");
                trip.setName(endedTrip.getName());
                trip.setPolylinePoints(polylines);
                trip.setStepsPoints(steps);
                RoomHandler.getInstance(this.getContext()).getAppDatabase().tripDao().deleteAllByName(endedTrip.getName());
                RoomHandler.getInstance(this.getContext()).getAppDatabase().tripDao().insertAll(trip);
                binding.getRoot().post(this::replaceActivity);

            });
            th2.start();
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        polylines = (ArrayList<LatLng>) getArguments().getSerializable("poliline");
        steps = (ArrayList<LatLng>) getArguments().getSerializable("stepPoliline");
        Controller controller = new Controller();
        controller.run();
        int max = controller.max_pulse;
        int min = controller.min_pulse;
        int avg = controller.avg_pulse;
        Log.d("qwertyuiop", Integer.toString(max));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RouteEndLayoutBinding.inflate(getLayoutInflater());

        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        sharedPreferencesManager.saveInt("lastRouteStatus", 0);
        initFunc();

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);


        supportMapFragment.getMapAsync(this);
        return binding.getRoot();
    }

    private void replaceActivity() {
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(intent);
        this.getActivity().finish();

    }

    @Override
    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        map.setMyLocationEnabled(true);

        renderPolylineNew((ArrayList<LatLng>) getArguments().getSerializable("poliline"));
        addStepPolyline((ArrayList<LatLng>) getArguments().getSerializable("stepPoliline"));
    }

    public void renderPolylineNew(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(R.color.dark_orange));
        polyOptions.width(10);
        polyOptions.addAll(list);
        map.addPolyline(polyOptions);
    }

    public void addStepPolyline(List<LatLng> list) {


        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.width(10);
        polyOptions.color(Color.GREEN);
        polyOptions.addAll(list);
        map.addPolyline(polyOptions);
    }
}
