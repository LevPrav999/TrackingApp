package ru.predprof.trackingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.activities.OnRouteActivity;
import ru.predprof.trackingapp.databinding.StartRouteLayoutBinding;
import ru.predprof.trackingapp.models.DefaultTrip;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;
import ru.predprof.trackingapp.room.defaultTrips.DefaultRoomHandler;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;
import ru.predprof.trackingapp.utils.Counter;
import ru.predprof.trackingapp.utils.MapUtils;

public class StartRouteLayout extends Fragment implements
        OnMapReadyCallback, RoutingListener {
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private StartRouteLayoutBinding binding;
    private SharedPreferencesManager sharedPreferencesManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private List<Polyline> polylines;
    private GoogleMap map;
    private Counter counter;
    private MapUtils mapUtils;

    private LatLng startedPoint;
    private LatLng endedPoint;

    private Trip currentTrip;

    private boolean isError = false;


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getContext() != null && location != null && startedPoint != null && endedPoint != null) {
                    mLastLocation = location;
                    double distanceToStart = mapUtils.countDistanceBetweenToPoints(mLastLocation.getLatitude(), mLastLocation.getLongitude(), startedPoint.latitude, startedPoint.longitude);
                    double distanceToEnd = mapUtils.countDistanceBetweenToPoints(mLastLocation.getLatitude(), mLastLocation.getLongitude(), endedPoint.latitude, endedPoint.longitude);

                    if (distanceToStart > distanceToEnd) {
                        LatLng a = startedPoint;
                        startedPoint = endedPoint;
                        endedPoint = a;
                    }

                    double distanceFinal = mapUtils.countDistanceBetweenToPoints(mLastLocation.getLatitude(), mLastLocation.getLongitude(), startedPoint.latitude, startedPoint.longitude);

                    if (distanceFinal > 0.0009d) {
                        if (!isError) {
                            Toast.makeText(getContext(), "Вы слишком далеко от точки старта", Toast.LENGTH_LONG).show();
                            isError = true;
                            binding.startButton.setActivated(false);
                        }
                    } else {
                        isError = false;
                        binding.startButton.setActivated(true);
                    }


                }
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        polylines = new ArrayList<>();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        counter = new Counter();
        mapUtils = new MapUtils();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = StartRouteLayoutBinding.inflate(getLayoutInflater());

        sharedPreferencesManager = new SharedPreferencesManager(getContext());


        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);


        binding.startButton.setOnClickListener(l->{
            if(!isError){
                replaceActivity(currentTrip);
            }else{
                Toast.makeText(getContext(), "Вы слишком далеко от точки старта", Toast.LENGTH_LONG).show();
            }
        });

        binding.editButton.setOnClickListener(l -> {
            toEditFragment();
        });


        supportMapFragment.getMapAsync(this);

        return binding.getRoot();
    }

    private void getRoute(LatLng pickupLatLng, LatLng locationLatLng) {
        if (pickupLatLng != null && locationLatLng != null) {
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

    public void addStepPolyline(List<LatLng> list) {


        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.width(10);
        polyOptions.color(Color.GREEN);
        polyOptions.addAll(list);
        map.addPolyline(polyOptions);
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        renderPolyline(route);

        float imt = sharedPreferencesManager.getFloat("imt", 0f);
        int level = sharedPreferencesManager.getInt("level", 0);
        int healthStatus = sharedPreferencesManager.getInt("healthStatus", 0);

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
        String str = b + ":" + cRoundInt;

        if (getArguments().getBoolean("isDefaultRoute", false)) {
            currentTrip.setLenKm(route.get(route.size() - 1).getDistanceText().split(" ")[0]);
            currentTrip.setDuration(str);
        }

        binding.routeComplexity.setText(counter.countLevelOfTravelStr(b));


    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    @SuppressLint("MissingPermission")
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        map.setMyLocationEnabled(true);

        Bundle b = getArguments();
        if (b != null) {
            Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                }
            };

            Thread th = new Thread(() -> {
                if (b.getBoolean("isDefaultRoute", false)) {
                    String name = b.getString("routeName", "Не указано");
                    DefaultTrip trip = DefaultRoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getTripByName(name);

                    Trip tripToReplace = new Trip();
                    tripToReplace.setName(trip.name);
                    tripToReplace.setDifficultAuto(trip.difficultAutoInt);

                    h.post(() -> {
                        currentTrip = tripToReplace;
                        binding.editButton.setVisibility(View.GONE);
                        binding.routeName.setText(name);
                        String startPoint = trip.start_point;
                        String endPoint = trip.end_point;
                        if (startPoint.length() > 1 && endPoint.length() > 1) {
                            startedPoint = new LatLng(Double.parseDouble(startPoint.split(":")[0]), Double.parseDouble(startPoint.split(":")[1]));
                            endedPoint = new LatLng(Double.parseDouble(endPoint.split(":")[0]), Double.parseDouble(endPoint.split(":")[1]));

                            getRoute(startedPoint, endedPoint);
                        }
                    });
                } else {
                    String name = b.getString("routeName", "Не указано");
                    Trip trip = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getTripByName(name);

                    h.post(() -> {
                        currentTrip = trip;
                        binding.routeName.setText(name);
                        startedPoint = trip.getPolylinePoints().get(0);
                        endedPoint = trip.getPolylinePoints().get(trip.getPolylinePoints().size() - 1);
                        renderPolylineNew(trip.getPolylinePoints());
                        addStepPolyline(trip.getStepsPoints());

                    });
                }
            });
            th.start();

        }
    }

    private void replaceActivity(Trip tr) {
        ArrayList<LatLng> polylinePoints = new ArrayList<>(tr.getPolylinePoints() != null ? tr.getPolylinePoints() : polylines.get(0).getPoints());
        tr.setPolylinePoints(null);
        tr.setStepsPoints(null);

        Intent intent = new Intent(this.getActivity(), OnRouteActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("trip", (Serializable) tr);
        b.putSerializable("polylines", polylinePoints);
        intent.putExtras(b);
        startActivity(intent);
        getActivity().finish();
    }

    private void toEditFragment(){
        Fragment fragment = new EditRouteFragment();
        Bundle b = new Bundle();
        b.putString("routeName", currentTrip.name);
        b.putString("routeLength", currentTrip.getLenKm());
        b.putString("routeComplexity", currentTrip.getDifficultAuto());
        b.putString("routeTime", currentTrip.getDuration());

        fragment.setArguments(b);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.map, fragment);
        transaction.commit();
    }
}
