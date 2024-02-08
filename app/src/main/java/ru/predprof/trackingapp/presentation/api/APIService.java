package ru.predprof.trackingapp.presentation.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import ru.predprof.trackingapp.presentation.api.models.AllJson;

public interface APIService {
    @Headers("x-access-tokens: az4fvf7nzi1XPIsYiMEu")
    @GET(".")
    Call<AllJson> getData();
}
