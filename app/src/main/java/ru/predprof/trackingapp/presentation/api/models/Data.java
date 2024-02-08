package ru.predprof.trackingapp.presentation.api.models;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("pulse")
    private Pulse pulse;

    public Pulse getPulse() {
        return pulse;
    }
}
