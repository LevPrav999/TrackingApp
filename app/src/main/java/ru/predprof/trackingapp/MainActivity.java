package ru.predprof.trackingapp;

import android.Manifest;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import ru.predprof.trackingapp.activities.NoGpsActivity;
import ru.predprof.trackingapp.activities.NoInternetActivity;
import ru.predprof.trackingapp.activities.NoPermissionsActivity;
import ru.predprof.trackingapp.activities.RegisterActivity;
import ru.predprof.trackingapp.databinding.ActivityMainBinding;
import ru.predprof.trackingapp.fragments.EditRouteFragment;
import ru.predprof.trackingapp.fragments.MainAppFragment;
import ru.predprof.trackingapp.fragments.ProfileFragment;
import ru.predprof.trackingapp.fragments.RoutesFragment;
import ru.predprof.trackingapp.models.DefaultTrip;
import ru.predprof.trackingapp.presentation.api.Controller;
import ru.predprof.trackingapp.room.defaultTrips.DefaultRoomHandler;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;
import ru.predprof.trackingapp.utils.Replace;

public class MainActivity extends AppCompatActivity {


    private final MutableLiveData<Boolean> connection = new MutableLiveData<>();
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            Log.d("granted", "1");
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            Log.d("granted", "2");
                        } else {
                            Replace.replaceActivity(this, new NoPermissionsActivity(), true);
                        }
                    }
            );
    private ActivityMainBinding binding;
    private SharedPreferencesManager sharedPreferencesManager;

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void internetListener() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest request = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();

        connectivityManager.registerNetworkCallback(request, new ConnectivityManager.NetworkCallback() {

            public void onAvailable(@NonNull Network network) {

            }

            public void onLost(@NonNull Network network) {
                connection.postValue(false);
            }

            public void onUnavailable() {
                connection.postValue(false);
            }
        });
    }

    private boolean isGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isGPS()) {
            Replace.replaceActivity(this, new NoGpsActivity(), true);
        }

        if (!isNetworkConnected()) {
            Replace.replaceActivity(this, new NoInternetActivity(), true);
        }
        internetListener();

        connection.observe(this, con -> {
            if (!con) {
                Replace.replaceActivity(this, new NoInternetActivity(), true);
            }
        });

        sharedPreferencesManager = new SharedPreferencesManager(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        saveToDb();


        //sharedPreferencesManager.saveInt("lastRouteStatus", 0);
        if (sharedPreferencesManager.getInt("lastRouteStatus", 0) == 1) {
            Fragment f = new EditRouteFragment();
            Bundle b = new Bundle();
            b.putBoolean("isTripActive", true);
            f.setArguments(b);
            replace(f);
        } else {
            replace(new MainAppFragment());
            if (sharedPreferencesManager.getString("name", null) == null) {
                Replace.replaceActivity(this, new RegisterActivity(), true);
            } else {
                replace(new MainAppFragment());
            }
        }


        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });


        BottomNavigationView bottomNavigationView = binding.getterNavigation;
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.prof) {
                replace(new ProfileFragment());
            } else if (item.getItemId() == R.id.statistic) {
                replace(new MainAppFragment());
            } else {
                replace(new RoutesFragment());
            }
            return true;
        });
        Controller ct = new Controller(); // проверка работы api
        ct.run();
    }

    public void saveToDb() {
        Thread th = new Thread(() -> {
            List<DefaultTrip> list = DefaultRoomHandler.getInstance(getApplicationContext()).getAppDatabase().tripDao().getAll();
            if (list.size() == 0) {
                DefaultTrip trip = new DefaultTrip();
                trip.setStart_point("55.764647:37.605854");
                trip.setEnd_point("55.754238:37.634425");
                trip.setName("Красоты Москвы #1");
                trip.setDifficultAuto("Easy");

                DefaultRoomHandler.getInstance(getApplicationContext()).getAppDatabase().tripDao().insertAll(trip);

                DefaultTrip trip2 = new DefaultTrip();
                trip2.setStart_point("55.751863:37.601204");
                trip2.setEnd_point("55.768413:37.649324");
                trip2.setName("Красоты Москвы #2");
                trip2.setDifficultAuto("Medium");

                DefaultRoomHandler.getInstance(getApplicationContext()).getAppDatabase().tripDao().insertAll(trip2);

                DefaultTrip trip3 = new DefaultTrip();
                trip3.setStart_point("55.795116:37.616204");
                trip3.setEnd_point("55.704786:37.538074");
                trip3.setName("Красоты Москвы #3");
                trip3.setDifficultAuto("Hard");

                DefaultRoomHandler.getInstance(getApplicationContext()).getAppDatabase().tripDao().insertAll(trip3);
            }
        });

        th.start();
    }

    public void replace(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.map, fragment).commit();

    }


}
