package ru.predprof.trackingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.predprof.trackingapp.NoBarActivity;
import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.databinding.ActivityMainBinding;
import ru.predprof.trackingapp.databinding.ActivityOnRouteBinding;
import ru.predprof.trackingapp.databinding.FragmentMapBinding;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;
import ru.predprof.trackingapp.utils.Counter;
import ru.predprof.trackingapp.utils.MapUtils;

public class OnRouteActivity extends AppCompatActivity
        implements
        OnMapReadyCallback, RoutingListener {

    private ActivityOnRouteBinding binding;
    private SharedPreferencesManager sharedPreferencesManager;

    private List<LatLng> arrayLatLng;
    private String routeName = "Название маршрута не задано";


    private static final int[] COLORS = new int[]{R.color.dark_orange};
    Location mLastLocation;
    LocationRequest mLocationRequest;

    private Counter counter;
    private MapUtils mapUtils;

    private LatLng markerLatLng = null;
    private LatLng startLatLng = null;
    private LatLng lastNearest = null;
    private List<Polyline> polylines;

    double maxToRewrite = 0.3d;

    private GoogleMap map;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    mLastLocation = location;
                    String speed = roundToOneSignificantDigit(new BigDecimal(mLastLocation.getSpeed())).toPlainString();
                    binding.userSpeed.setText(speed + " km/h");
                    if(arrayLatLng != null){
                        LatLng current = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        LatLng nearest = mapUtils.findNearestLatLng(arrayLatLng, current);
                        if(nearest != null){
                            lastNearest = nearest;
                            int index = arrayLatLng.indexOf(nearest);
                            List<LatLng> elementsToDelete = new ArrayList<>();
                            for(int i = 0; i <= index; i++){
                                elementsToDelete.add(arrayLatLng.get(i));
                            }
                            arrayLatLng.removeAll(elementsToDelete);
                            renderPolylineNew(arrayLatLng);

                            if (mapUtils.haversineDistance(nearest.latitude, nearest.longitude, current.latitude, current.longitude) > maxToRewrite){
                                getRouteToMarker(markerLatLng, current);
                                maxToRewrite+=0.3d;
                            }
                        }else if(lastNearest != null){
                            if (mapUtils.haversineDistance(lastNearest.latitude, lastNearest.longitude, current.latitude, current.longitude) > maxToRewrite){
                                getRouteToMarker(markerLatLng, current);
                                maxToRewrite+=0.3d;
                            }
                        }

                        addStepPolyline(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), startLatLng);
                        startLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    }
                    if(markerLatLng != null){
                        double distance = mapUtils.countDistanceBetweenToPoints(location.getLatitude(), location.getLongitude(), markerLatLng.latitude, markerLatLng.longitude);
                        if (distance < 0.0009d){
                            Toast.makeText(getApplicationContext(), "Вы приехали", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    };
    private FusedLocationProviderClient mFusedLocationClient;

    public static BigDecimal roundToOneSignificantDigit(BigDecimal d) {
        return d.setScale(d.signum() == 0 ? 0 : d.scale() - d.precision() + 1, RoundingMode.HALF_UP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnRouteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferencesManager = new SharedPreferencesManager(this);

        sharedPreferencesManager.saveInt("lastRouteStatus", 1);

        Bundle b = getIntent().getExtras();
        if(b != null){
            arrayLatLng = (ArrayList<LatLng>) b.getSerializable("polylines");

            routeName = b.getString("routeName");
        }

        counter = new Counter();
        mapUtils = new MapUtils();
        polylines = new ArrayList<>();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        binding.routePoints.setText(routeName);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);


        supportMapFragment.getMapAsync(this);

        binding.pauseButton.setOnClickListener(view -> {
            replaceActivity(1);
        });
        binding.endButton.setOnClickListener(view -> {
            replaceActivity(2);
        });

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
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
    }


    public void renderPolylineFirst(){

        startLatLng = arrayLatLng.get(0);
        markerLatLng = arrayLatLng.get(arrayLatLng.size()-1);

        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(COLORS[0]));
        polyOptions.width(10);
        polyOptions.addAll(arrayLatLng);
        Polyline polyline = map.addPolyline(polyOptions);
        polylines.add(polyline);
    }

    public void renderPolyline(ArrayList<Route> route){
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {


            startLatLng = route.get(i).getPoints().get(0);
            arrayLatLng = route.get(i).getPoints();
            markerLatLng = route.get(i).getPoints().get(route.get(i).getPoints().size()-1);

            int colorIndex = i % 4;
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = map.addPolyline(polyOptions);
            polylines.add(polyline);
        }
    }

    public void renderPolylineNew(List<LatLng> list){
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




    public void addStepPolyline(LatLng first, LatLng second){


        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.width(10);
        polyOptions.color(Color.GREEN);
        polyOptions.add(first);
        polyOptions.add(second);
        map.addPolyline(polyOptions);
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        renderPolyline(route);

        float a = ((float)(route.get(route.size()-1).getDistanceValue())) / 1000f / counter.countPersonalSpeed(1); // подставлять параметр из SP
        int b = (int) a;
        float c = a-b;

        String cRound = String.format("%.2g", c).replace(",", ".");
        float cRoundFloat = Float.parseFloat(cRound)*60f+5f;
        int cRoundInt = (int) cRoundFloat;
        String str = b+" Hours "+cRoundInt+" Mins";

        Toast.makeText(getApplicationContext(), "Distance - " + route.get(route.size()-1).getDistanceText()+ " : time - " + str , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRoutingCancelled() {

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

        renderPolylineFirst();
    }

    private void replaceActivity(int frag){

        Intent intent = new Intent(this, NoBarActivity.class);
        Bundle b = new Bundle();
        b.putInt("fragment", frag);
        b.putString("routeName", routeName);
        b.putSerializable("polylines", b.getSerializable("polylines"));
        intent.putExtras(b);
        startActivity(intent);
    }
}