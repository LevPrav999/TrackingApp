package ru.predprof.trackingapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

@Entity
public class DefaultTrip {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "start_point")
    public String start_point;

    @ColumnInfo(name = "end_point")
    public String end_point;

    @ColumnInfo(name = "polylinePoints")
    public ArrayList<LatLng> polylinePoints;


}
