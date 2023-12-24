package ru.predprof.trackingapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.predprof.trackingapp.databinding.NoBarActivityBinding;

public class NoBarActivity  extends AppCompatActivity {
    NoBarActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NoBarActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
    public void replace(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment, fragment).commit();

    }
}
