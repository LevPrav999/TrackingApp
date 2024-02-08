package ru.predprof.trackingapp.presentation.api.models;

import com.google.gson.annotations.SerializedName;

public class AllJson {
    @SerializedName("data")
    private Data data;

    public Data getAll() {
        return data;
    }
}
