package ru.predprof.trackingapp.fragments;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.directions.route.Segment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.databinding.FragmentMapBinding;
import ru.predprof.trackingapp.utils.Counter;
import ru.predprof.trackingapp.utils.MapUtils;


public class MapFragment extends Fragment
        implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener, RoutingListener {

    private static final int[] COLORS = new int[]{R.color.dark_orange};
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private FragmentMapBinding binding;

    private Counter counter;
    private MapUtils mapUtils;

    private LatLng markerLatLng = null;

    private GoogleMap map;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getContext() != null) {

                    mLastLocation = location;
                    if(markerLatLng != null){
                        double distance = mapUtils.countDistanceBetweenToPoints(location.getLatitude(), location.getLongitude(), markerLatLng.latitude, markerLatLng.longitude);
                        if (distance < 0.0017d){
                            Toast.makeText(getContext(), "Вы приехали", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    //map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    //map.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
            }
        }
    };
    private FusedLocationProviderClient mFusedLocationClient;
    private List<Polyline> polylines;


    public MapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        polylines = new ArrayList<>();
        counter = new Counter();
        mapUtils = new MapUtils();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(getLayoutInflater());


        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);


        supportMapFragment.getMapAsync(this);

        return binding.getRoot();
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

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        markerLatLng = latLng;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        map.clear();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        getRouteToMarker(latLng);
        map.addMarker(markerOptions);

    }

    private void getRouteToMarker(LatLng pickupLatLng) {
        if (pickupLatLng != null && mLastLocation != null) {
            Routing routing = new Routing.Builder()
                    .key("AIzaSyCK4y3tSLqGJD2GG4lCkMCDf-Cc6D-jvKU")
                    .travelMode(AbstractRouting.TravelMode.WALKING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), pickupLatLng)
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {

            List<Segment> segments = route.get(i).getSegments();
            for (Segment s : segments) {
                Log.d("route", s.getInstruction());
                Log.d("route", String.valueOf(s.getDistance()));
            }

            int colorIndex = i % 4;
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = map.addPolyline(polyOptions);
            polylines.add(polyline);

            float a = ((float)(route.get(i).getDistanceValue())) / 1000f / counter.countPersonalSpeed(1); // подставлять параметр из SP
            int b = (int) a;
            float c = a-b;

            String cRound = String.format("%.2g", c).replace(",", ".");
            float cRoundFloat = Float.parseFloat(cRound)*60f+5f;
            int cRoundInt = (int) cRoundFloat;
            String str = b+" Hours "+cRoundInt+" Mins";

            Toast.makeText(getContext(), "Route " + (i + 1) + ": distance - " + route.get(i)    .getDistanceText()+ " : time - " + str , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }
}