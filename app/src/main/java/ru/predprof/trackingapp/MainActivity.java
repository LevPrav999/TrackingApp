package ru.predprof.trackingapp;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


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
