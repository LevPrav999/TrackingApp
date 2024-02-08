package ru.predprof.trackingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.activities.OnRouteActivity;
import ru.predprof.trackingapp.databinding.EditRouteLayoutBinding;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;
import ru.predprof.trackingapp.utils.Counter;

public class EditRouteFragment extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener, RoutingListener {
    Location mLastLocation;
    LocationRequest mLocationRequest;
    int complexity = 0;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getContext() != null) {
                    mLastLocation = location;
                }
            }
        }
    };
    private EditRouteLayoutBinding binding;
    private FusedLocationProviderClient mFusedLocationClient;
    private List<Polyline> polylines;
    private GoogleMap map;
    private Counter counter;
    private SharedPreferencesManager preferenceManager;

    private List<String> arrTrips = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        counter = new Counter();
        polylines = new ArrayList<>();
        preferenceManager = new SharedPreferencesManager(getContext());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = EditRouteLayoutBinding.inflate(getLayoutInflater());

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);


        supportMapFragment.getMapAsync(this);
        try {
            if (getArguments().getString("routeName") != null) {
                binding.routeName.setText(getArguments().getString("routeName"));
                binding.routeLength.setText(getArguments().getString("routeLength"));
                binding.routeComplexity.setText(getArguments().getString("routeComplexity"));
                binding.routeTime.setText(getArguments().getString("routeTime"));
            }
        } catch (Exception e) {
        }


        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        Thread th = new Thread(() -> {
            List<Trip> t = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getAll();
            h.post(() -> {
                for (Trip trip : t) {
                    arrTrips.add(trip.getName());
                }
            });
        });
        th.start();


        binding.buttonSave.setOnClickListener(l -> {
            if (binding.routeName.getText().toString().length() > 0) {
                if (arrTrips.contains(binding.routeName.getText().toString().trim())) {
                    Toast.makeText(getContext(), "Такое название маршрута уже существует", Toast.LENGTH_LONG).show();
                } else if (binding.routeLength.getText().toString().split(" ").length > 1) {
                    String lenKm = binding.routeLength.getText().toString().split(" ")[0];
                    String name = binding.routeName.getText().toString();
                    Trip trip = new Trip();
                    trip.setAvgSpeed("0");
                    trip.setTime("0");
                    trip.setLenKm(lenKm);
                    trip.setDataPulse(new ArrayList<>());
                    trip.setDifficultAuto(Integer.toString(complexity));
                    trip.setDifficultReal("0");
                    trip.setMaxSpeed("0");
                    Calendar calendar = Calendar.getInstance();
                    String date = new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
                    trip.setWeekDay(date);
                    trip.setDuration("0");
                    trip.setEnded("0");
                    trip.setName(name);
                    trip.setPolylinePoints(new ArrayList<>(polylines.get(0).getPoints()));

                    Thread th2 = new Thread(() -> {
                        RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().insertAll(trip);
                    });

                    th2.start();

                    replaceActivity(
                            trip,
                            polylines
                    );

                } else {
                    Toast.makeText(getContext(), "Поставьте метку", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Введите название", Toast.LENGTH_SHORT).show();
            }


        });


        return binding.getRoot();
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    public void renderPolyline(ArrayList<Route> route) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.dark_orange));
            polyOptions.width(10);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = map.addPolyline(polyOptions);
            polylines.add(polyline);
        }
    }

    public void renderPolylineNew(List<LatLng> list) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(R.color.dark_orange));
        polyOptions.width(10);
        polyOptions.addAll(list);
        Polyline polyline = map.addPolyline(polyOptions);
        polylines.add(polyline);
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        renderPolyline(route);

        float imt = preferenceManager.getFloat("imt", 0f);
        int level = preferenceManager.getInt("level", 0);
        int healthStatus = preferenceManager.getInt("healthStatus", 0);

        int personalLevel = counter.countPersonalLevel(imt, level, healthStatus);

        float a = ((float) (route.get(route.size() - 1).getDistanceValue())) / 1000f / counter.countPersonalSpeed(personalLevel);
        int b = (int) a;
        float c = a - b;

        String cRound = String.format("%.2g", c).replace(",", ".");
        float cRoundFloat = Float.parseFloat(cRound) * 60f + 5f;
        if (cRoundFloat >= 60) {
            cRoundFloat -= 60;
            b += 1;
        }
        int cRoundInt = (int) cRoundFloat;
        String str = b + ":" + cRoundInt / 10 + cRoundInt % 10;
        binding.routeLength.setText(route.get(route.size() - 1).getDistanceText());
        binding.routeTime.setText(str);
        complexity = counter.countLevelOfTravelInt(a);
        Log.d("fghjkl", Integer.toString(complexity));
        binding.routeComplexity.setText(counter.countLevelOfTravelStr(b));


    }

    @Override
    public void onRoutingCancelled() {

    }

    private void getRouteToMarker(LatLng pickupLatLng, LatLng locationLatLng) {
        if (pickupLatLng != null && mLastLocation != null) {
            Routing routing = new Routing.Builder()
                    .key("AIzaSyCK4y3tSLqGJD2GG4lCkMCDf-Cc6D-jvKU")
                    .travelMode(AbstractRouting.TravelMode.WALKING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(locationLatLng, pickupLatLng)
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        map.clear();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        getRouteToMarker(latLng, new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        map.addMarker(markerOptions);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        map.setMyLocationEnabled(true);
        map.setOnMapClickListener(this);

        Bundle b = getArguments();
        if (b != null) {
            boolean isTripActive = b.getBoolean("isTripActive", false);
            if (isTripActive) {
                Handler h = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                    }
                };

                Thread th = new Thread(() -> {
                    Trip t = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getlastTrip();
                    h.post(() -> {
                        binding.routeTime.setText(t.time);
                        binding.routeName.setText(t.name);
                        binding.routeComplexity.setText(t.difficultAuto);
                        binding.routeLength.setText(t.lenKm);
                        //renderPolylineNew(t.polylinePoints);
                    });
                });
                th.start();
            }
        }
    }

    private void replaceActivity(Trip tr, List<Polyline> polylines) {
        ArrayList<LatLng> polylinePoints = new ArrayList<>(polylines.get(0).getPoints());
        tr.setPolylinePoints(null);

        Intent intent = new Intent(this.getActivity(), OnRouteActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("trip", (Serializable) tr);
        b.putSerializable("polylines", polylinePoints);
        intent.putExtras(b);
        startActivity(intent);
        getActivity().finish();
    }
}
