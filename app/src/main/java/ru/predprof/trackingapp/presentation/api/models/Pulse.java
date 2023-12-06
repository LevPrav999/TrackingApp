package ru.predprof.trackingapp.presentation.api.models;

import com.google.gson.annotations.SerializedName;

public class Pulse {
    public Pulse(int min, int max, int avg) {
        this.min = min;
        this.max = max;
        this.avg = avg;
    }

    @SerializedName("min")
    private int min;

    @SerializedName("max")
    private int max;

    @SerializedName("avg")
    private int avg;

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getAvg() {
        return avg;
    }
}
