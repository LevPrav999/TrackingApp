package ru.predprof.trackingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.predprof.trackingapp.R;

public class NoPermissionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_permissions);
    }
}