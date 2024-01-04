package ru.predprof.trackingapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ru.predprof.trackingapp.databinding.NoBarActivityBinding;
import ru.predprof.trackingapp.fragments.PauseRouteFragment;
import ru.predprof.trackingapp.fragments.RouteEndFragment;

public class NoBarActivity  extends AppCompatActivity {
    NoBarActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NoBarActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle b = getIntent().getExtras();
        if(b != null && b.getInt("fragment") == 1){
            replace(new PauseRouteFragment());
        } else if (b != null && b.getInt("fragment") == 2) {
            replace(new RouteEndFragment());
        }


    }
    public void replace(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment, fragment).commit();

    }
}
