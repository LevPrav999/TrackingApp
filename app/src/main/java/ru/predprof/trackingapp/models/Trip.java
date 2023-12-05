package ru.predprof.trackingapp.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Trip {
    @PrimaryKey(autoGenerate = true)
    public int number;

    @ColumnInfo(name = "len_km")
    public String lenKm; // Длительность в километрах

    @ColumnInfo(name = "time")
    public String time; // Время поездки

    @ColumnInfo(name = "avg_speed")
    public String avgSpeed; // Средняя скорость

    @ColumnInfo(name = "max_speed")
    public String maxSpeed; // Максимальная скорость

    @ColumnInfo(name = "data_pulse")
    public ArrayList<String> dataPulse; // Данные о пульсе

    @ColumnInfo(name = "dif_auto")
    public String difficultAuto; // Сложность расчётная

    @ColumnInfo(name = "dif_real")
    public String difficultReal; // Сложность реальная

}
