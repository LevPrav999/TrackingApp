package ru.predprof.trackingapp;

import android.os.Bundle;
import android.util.Log;

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
            Bundle b1 = new Bundle();
            Log.d("qwertyuiop", b.getString("len"));
            b1.putString("routeName", b.getString("routeName"));
            b1.putInt("route_id", b.getInt("route_id"));
            b1.putString("len", b.getString("len"));
            b1.putString("dif_aut", b.getString("dif_aut"));
            b1.putSerializable("polylines", b.getSerializable("polylines"));
            RouteEndFragment routeEndFragment = new RouteEndFragment();
            routeEndFragment.setArguments(b1);
            replace(routeEndFragment);
        }


    }
    public void replace(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment, fragment).commit();

    }
}
