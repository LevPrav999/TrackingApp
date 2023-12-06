package ru.predprof.trackingapp.presentation.api;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.predprof.trackingapp.presentation.api.models.AllJson;

public interface APIService {
    @GET("")
    Call<AllJson> getData();
}
