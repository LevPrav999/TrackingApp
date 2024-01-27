package ru.predprof.trackingapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;

import ru.predprof.trackingapp.databinding.NoBarActivityBinding;
import ru.predprof.trackingapp.fragments.PauseRouteFragment;
import ru.predprof.trackingapp.fragments.RouteEndFragment;

public class NoBarActivity extends AppCompatActivity {
    NoBarActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NoBarActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle b = getIntent().getExtras();
        if (b != null && b.getInt("fragment") == 1) {
            Bundle b1 = new Bundle();
            b1.putString("routeName", b.getString("routeName"));
            b1.putSerializable("stepPoliline", (Serializable) b.getSerializable("stepPoliline"));
            b1.putSerializable("poliline", (Serializable) b.getSerializable("poliline"));
            PauseRouteFragment pauseRouteFragment = new PauseRouteFragment();
            pauseRouteFragment.setArguments(b1);
            replace(pauseRouteFragment);
        } else if (b != null && b.getInt("fragment") == 2) {
            Bundle b1 = new Bundle();

            b1.putString("routeName", b.getString("routeName"));
            b1.putSerializable("stepPoliline", (Serializable) b.getSerializable("stepPoliline"));
            b1.putSerializable("poliline", (Serializable) b.getSerializable("poliline"));
            b1.putSerializable("trip", b.getSerializable("trip"));
            b1.putInt("dur", b.getInt("dur"));
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
