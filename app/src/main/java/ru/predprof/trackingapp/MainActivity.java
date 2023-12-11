package ru.predprof.trackingapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import ru.predprof.trackingapp.databinding.ActivityMainBinding;
import ru.predprof.trackingapp.fragments.ProfileFragment;
import ru.predprof.trackingapp.fragments.RoutesFragment;
import ru.predprof.trackingapp.fragments.StatisticFragment;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // FragmentManager fragmentManager = getSupportFragmentManager();
        // FragmentTransaction transaction = fragmentManager.beginTransaction();
        // transaction.addToBackStack("back");
        // transaction.add(R.id.map, new EditProfileFragment()).commit();
        BottomNavigationView bottomNavigationView = binding.getterNavigation;
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.prof) {
                replace(new ProfileFragment());
            } else if (item.getItemId() == R.id.statistic) {
                replace(new RoutesFragment());
            } else {
                replace(new StatisticFragment());
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
            RoomHandler.getInstance(getApplicationContext()).getAppDatabase().tripDao().insertAll(trip);
        });
        th2.start();

    }

    public void replace(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.map, fragment).commit();


    }


}
