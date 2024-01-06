package ru.predprof.trackingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import ru.predprof.trackingapp.MainActivity;
import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.activities.OnRouteActivity;
import ru.predprof.trackingapp.databinding.EditRouteLayoutBinding;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;
import ru.predprof.trackingapp.utils.Counter;

public class EditRouteFragment extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener, RoutingListener {
    private EditRouteLayoutBinding binding;

    private FusedLocationProviderClient mFusedLocationClient;

    Location mLastLocation;
    LocationRequest mLocationRequest;
    private List<Polyline> polylines;
    private GoogleMap map;
    private Counter counter;
    private int complexity = 0;

    private SharedPreferencesManager preferenceManager;


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

        binding.buttonSave.setOnClickListener(l -> {
            if (binding.routeName.getText().toString().length() > 0) {
                Thread th = new Thread(() -> {
                    Trip trip = new Trip();
                    trip.setAvgSpeed("0");
                    trip.setTime("0");
                    trip.setLenKm(binding.routeLength.getText().toString().substring(0, binding.routeLength.getText().toString().length() - 3));
                    trip.setDataPulse(new ArrayList<>());
                    trip.setDifficultAuto(Integer.toString(complexity));
                    trip.setDifficultReal("0");
                    trip.setMaxSpeed("0");
                    Calendar calendar = Calendar.getInstance();
                    String date = new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
                    trip.setWeekDay(date);
                    trip.setDuration("0");
                    trip.setName(binding.routeName.getText().toString());

                    RoomHandler.getInstance(this.getContext()).getAppDatabase().tripDao().insertAll(trip);

                });
                th.start();
            } else {
                Toast.makeText(getContext(), "Введите название", Toast.LENGTH_SHORT).show();
            }

            replaceActivity(
                    binding.routeName.getText().toString(),
                    polylines
            );
        });

        return binding.getRoot();
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    public void renderPolyline(ArrayList<Route> route){
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {


            int colorIndex = i % 4;
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.dark_orange));
            polyOptions.width(10);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = map.addPolyline(polyOptions);
            polylines.add(polyline);
        }
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        renderPolyline(route);

        float imt = preferenceManager.getFloat("imt", 0f);
        int level = preferenceManager.getInt("level", 0);
        int healthStatus = preferenceManager.getInt("healthStatus", 0);

        int personalLevel = counter.countPersonalLevel(imt, level, healthStatus);

        float a = ((float)(route.get(route.size()-1).getDistanceValue())) / 1000f / counter.countPersonalSpeed(personalLevel);
        int b = (int) a;
        float c = a-b;

        String cRound = String.format("%.2g", c).replace(",", ".");
        float cRoundFloat = Float.parseFloat(cRound)*60f+5f;
        if (cRoundFloat >= 60) {
            cRoundFloat -= 60;
            b += 1;
        }
        int cRoundInt = (int) cRoundFloat;
        String str = b+":"+cRoundInt;
        binding.routeLength.setText(route.get(route.size()-1).getDistanceText());
        binding.routeTime.setText(str);
        complexity = b;
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
    }
    private void replaceActivity(String routeName, List<Polyline> polylines){
        ArrayList<LatLng> polylinePoints = new ArrayList<>(polylines.get(0).getPoints());

        Intent intent = new Intent(this.getActivity(), OnRouteActivity.class);
        Bundle b = new Bundle();
        b.putString("routeName", routeName);
        b.putSerializable("polylines", polylinePoints);
        intent.putExtras(b);
        startActivity(intent);
        getActivity().finish();
    }
}
