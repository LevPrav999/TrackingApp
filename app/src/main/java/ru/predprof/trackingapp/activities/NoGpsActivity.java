package ru.predprof.trackingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;

import ru.predprof.trackingapp.MainActivity;
import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.utils.Replace;

public class NoGpsActivity extends AppCompatActivity {

    private MutableLiveData<Boolean> isGps = new MutableLiveData<>(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_gps);

        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        registerReceiver(gpsSwitchStateReceiver, filter);

        isGps.observe(this, enabled ->{
            if(enabled){
                Replace.replaceActivity(this, new MainActivity(), false);
            }
        });
    }

    private BroadcastReceiver gpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (isGpsEnabled) {
                    isGps.postValue(true);
                }
            }
        }
    };
}