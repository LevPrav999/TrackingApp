package ru.predprof.trackingapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import ru.predprof.trackingapp.MainActivity;
import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.databinding.ActivityMainBinding;
import ru.predprof.trackingapp.databinding.ActivityNoPermissionsBinding;
import ru.predprof.trackingapp.utils.Replace;

public class NoPermissionsActivity extends AppCompatActivity {

    private ActivityNoPermissionsBinding binding;

    private boolean isGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoPermissionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.openSettings.setOnClickListener(l -> {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            Replace.replaceActivity(this, new MainActivity(), true);
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            Replace.replaceActivity(this, new MainActivity(), true);
                        }
                    }
            );
}