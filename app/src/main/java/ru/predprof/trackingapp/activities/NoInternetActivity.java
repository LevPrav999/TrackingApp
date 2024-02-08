package ru.predprof.trackingapp.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import ru.predprof.trackingapp.MainActivity;
import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.utils.Replace;

public class NoInternetActivity extends AppCompatActivity {
    private final MutableLiveData<Boolean> connection = new MutableLiveData<>(false);


    public void internetListener() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest request = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();

        connectivityManager.registerNetworkCallback(request, new ConnectivityManager.NetworkCallback() {

            public void onAvailable(@NonNull Network network) {
                connection.postValue(true);
            }

            public void onLost(@NonNull Network network) {
            }

            public void onUnavailable() {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        internetListener();

        connection.observe(this, con -> {
            if (con) {
                Replace.replaceActivity(this, new MainActivity(), false);
                connection.removeObservers(this);
            }
        });

    }
}