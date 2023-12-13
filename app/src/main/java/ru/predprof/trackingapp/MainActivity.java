package ru.predprof.trackingapp;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import ru.predprof.trackingapp.databinding.ActivityMainBinding;
import ru.predprof.trackingapp.fragments.MapFragment;
import ru.predprof.trackingapp.fragments.ProfileFragment;
import ru.predprof.trackingapp.fragments.RegisterFragment;
import ru.predprof.trackingapp.fragments.RoutesFragment;
import ru.predprof.trackingapp.fragments.StatisticFragment;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferencesManager sharedPreferencesManager;
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION,false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            Log.d("granted", "1");
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            Log.d("granted", "2");
                        } else {
                            Log.d("granted", "3");
                        }
                    }
            );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (sharedPreferencesManager.getString("name", null) != null){
            replace(new RegisterFragment());
        } else {
            replace(new StatisticFragment());
        }



        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });


        Fragment fragment=new MapFragment();

        // Open fragment
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.map,fragment)
                .commit();

        /*

        BottomNavigationView bottomNavigationView = binding.getterNavigation;
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.prof) {
                replace(new ProfileFragment());
            } else if (item.getItemId() == R.id.statistic) {
                replace(new StatisticFragment());
            } else {
                replace(new RoutesFragment());
            }
            return true;
        });
        Thread th2 = new Thread(() -> { // Тест работы БД
            Trip trip = new Trip();
            trip.setAvgSpeed("10");
            trip.setTime("12:00");
            trip.setLenKm("1");
            trip.setDataPulse(new ArrayList<>());
            trip.setDifficultAuto("1");
            trip.setDifficultReal("1");
            trip.setMaxSpeed("14");
            trip.setWeekDay("0");
            RoomHandler.getInstance(getApplicationContext()).getAppDatabase().tripDao().insertAll(trip);
            List<Trip> lst = RoomHandler.getInstance(getApplicationContext()).getAppDatabase().tripDao().getAll();
            Log.d("avgSpeed", lst.get(lst.size() - 1).weekDay);
        });
        th2.start();


         */

    }

    public void replace(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.map, fragment).commit();


    }


}
