package ru.predprof.trackingapp;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import ru.predprof.trackingapp.databinding.ActivityMainBinding;
import ru.predprof.trackingapp.fragments.EditProfileFragment;
import ru.predprof.trackingapp.fragments.RegisterFragment;
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


}
