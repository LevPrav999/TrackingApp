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

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "dif_auto")
    public String difficultAuto; // Сложность расчётная

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficultAuto() {
        return difficultAuto;
    }

    public void setDifficultAuto(String difficultAuto) {
        this.difficultAuto = difficultAuto;
    }

    @ColumnInfo(name = "start_point")
    public String start_point;

    @ColumnInfo(name = "end_point")
    public String end_point;

    @ColumnInfo(name = "polylinePoints")
    public ArrayList<LatLng> polylinePoints;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart_point() {
        return start_point;
    }

    public void setStart_point(String start_point) {
        this.start_point = start_point;
    }

    public String getEnd_point() {
        return end_point;
    }

    public void setEnd_point(String end_point) {
        this.end_point = end_point;
    }

    public ArrayList<LatLng> getPolylinePoints() {
        return polylinePoints;
    }

    public void setPolylinePoints(ArrayList<LatLng> polylinePoints) {
        this.polylinePoints = polylinePoints;
    }
}
