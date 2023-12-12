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

    @ColumnInfo(name = "day_week")
    public String weekDay; // День недели

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getLenKm() {
        return lenKm;
    }

    public void setLenKm(String lenKm) {
        this.lenKm = lenKm;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public ArrayList<String> getDataPulse() {
        return dataPulse;
    }

    public void setDataPulse(ArrayList<String> dataPulse) {
        this.dataPulse = dataPulse;
    }

    public String getDifficultAuto() {
        return difficultAuto;
    }

    public void setDifficultAuto(String difficultAuto) {
        this.difficultAuto = difficultAuto;
    }

    public String getDifficultReal() {
        return difficultReal;
    }

    public void setDifficultReal(String difficultReal) {
        this.difficultReal = difficultReal;
    }
}
