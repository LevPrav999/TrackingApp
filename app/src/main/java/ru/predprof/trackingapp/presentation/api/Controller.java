package ru.predprof.trackingapp.presentation.api;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.predprof.trackingapp.presentation.api.models.AllJson;

public class Controller implements Runnable {
    public static final String BASE_URL = "https://dt.miet.ru/ppo_it/api/watch/";
    public static int min_pulse = 0;
    public static int max_pulse = 0;
    public static int avg_pulse = 0;

    @Override
    public void run() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // пример работы
        APIService apiService = retrofit.create(APIService.class);
        apiService.getData().enqueue(new Callback<AllJson>() {
            @Override
            public void onResponse(@NonNull Call<AllJson> call, @NonNull Response<AllJson> response) {
                assert response.body() != null;
                Log.d("alksjfdhsjkfdvlb", Integer.toString(response.body().getAll().getPulse().getMin()));
                min_pulse = response.body().getAll().getPulse().getMin();
                max_pulse = response.body().getAll().getPulse().getMax();
                avg_pulse = response.body().getAll().getPulse().getAvg();

            }

            @Override
            public void onFailure(@NonNull Call<AllJson> call, @NonNull Throwable t) {
                Log.d("Something Wrong", call.toString());
            }
        });
    }
}
