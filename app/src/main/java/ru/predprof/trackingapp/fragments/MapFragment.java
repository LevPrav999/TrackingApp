package ru.predprof.trackingapp.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.databinding.FragmentSupportMapBinding;
import ru.predprof.trackingapp.utils.Counter;
import ru.predprof.trackingapp.utils.MapUtils;

public class MapFragment extends Fragment
        implements
        OnMapReadyCallback, RoutingListener {


    private static final int[] COLORS = new int[]{R.color.dark_orange};
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private FragmentSupportMapBinding binding;

    private Counter counter;
    private MapUtils mapUtils;

    private LatLng markerLatLng = null;
    private LatLng startLatLng = null;
    private LatLng lastNearest = null;
    private List<LatLng> arrayLatLng = null;

    private GoogleMap map;
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
        binding = FragmentSupportMapBinding.inflate(getLayoutInflater());


        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);


        supportMapFragment.getMapAsync(this);

        binding.button.setOnClickListener(l -> {
            getRouteToMarker(
                    new LatLng(Double.parseDouble(binding.editText1.getText().toString().split(":")[0]), Double.parseDouble(binding.editText1.getText().toString().split(":")[1])),
                    new LatLng(Double.parseDouble(binding.editText2.getText().toString().split(":")[0]), Double.parseDouble(binding.editText2.getText().toString().split(":")[1]))
            );
        });

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
    }


    private void getRouteToMarker(LatLng pickupLatLng, LatLng locationLatLng) {
        Routing routing = new Routing.Builder()
                .key("AIzaSyCK4y3tSLqGJD2GG4lCkMCDf-Cc6D-jvKU")
                .travelMode(AbstractRouting.TravelMode.WALKING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(locationLatLng, pickupLatLng)
                .build();
        routing.execute();
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


    public void renderPolyline(ArrayList<Route> route) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {


            startLatLng = route.get(i).getPoints().get(0);
            arrayLatLng = route.get(i).getPoints();

            Log.d("eeeeeeee", arrayLatLng.toString());

            int colorIndex = i % 4;
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
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
        polyOptions.color(getResources().getColor(COLORS[0]));
        polyOptions.width(10);
        polyOptions.addAll(list);
        Polyline polyline = map.addPolyline(polyOptions);
        polylines.add(polyline);
    }


    public void addStepPolyline(LatLng first, LatLng second) {


        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.width(10);
        polyOptions.color(Color.GREEN);
        polyOptions.add(first);
        polyOptions.add(second);
        Polyline polyline = map.addPolyline(polyOptions);
        polylines.add(polyline);
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        renderPolyline(route);

        float a = ((float) (route.get(route.size() - 1).getDistanceValue())) / 1000f / counter.countPersonalSpeed(1); // подставлять параметр из SP
        int b = (int) a;
        float c = a - b;

        String cRound = String.format("%.2g", c).replace(",", ".");
        float cRoundFloat = Float.parseFloat(cRound) * 60f + 5f;
        int cRoundInt = (int) cRoundFloat;
        String str = b + " Hours " + cRoundInt + " Mins";

        Toast.makeText(getContext(), "Distance - " + route.get(route.size() - 1).getDistanceText() + " : time - " + str, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRoutingCancelled() {

    }
}