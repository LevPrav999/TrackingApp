package ru.predprof.trackingapp.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

import ru.predprof.trackingapp.adapters.StatisticRecyclerAdapter;
import ru.predprof.trackingapp.databinding.MainAppLayoutBinding;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;

public class StatisticFragment extends Fragment {
    StatisticRecyclerAdapter adapter;
    List<Trip> lst;
    private MainAppLayoutBinding binding;

    private void initFunc() {
//        Thread th2 = new Thread(() -> { // Тест работы БД
//
//            List<Trip> lst = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getAll();
//            binding.routeLength.setText(lst.get(lst.size() - 1).lenKm);
//            binding.routeDuration.setText(lst.get(lst.size() - 1).time);
//            binding.routeEstimatedComplexity.setText(lst.get(lst.size() - 1).difficultAuto);
//            binding.routeUserRating.setText(lst.get(lst.size() - 1).difficultReal);
////            binding.routeAverageRating.setText(); TODO итоговый рейтинг (нет в БД)
////            Log.d("time", lst.get(lst.size() - 1).avgSpeed);
//        });
//        th2.start();
        binding.graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    String day;
                    switch ((int) value) {
                        case 0:
                            return "Пн";
                        case 1:
                            return "Вт";
                        case 2:
                            return "Ср";
                        case 3:
                            return "Чт";
                        case 4:
                            return "Пт";
                        case 5:
                            return "Сб";
                        case 6:
                            return "Вс";
                        default:
                            return "Пн";
                    }
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 6),
                new DataPoint(6, 6)
        });
        binding.graph.addSeries(series);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(174, 91, 15);
            }
        });
        series.setSpacing(30);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.WHITE);
        Thread th2 = new Thread(() -> { // Тест работы БД

            lst = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getAll();

            binding.recycler.post(new Runnable() {
                @Override
                public void run() {
                    adapter = new StatisticRecyclerAdapter(lst);
                    binding.recycler.setAdapter(adapter);
                    binding.recycler.setLayoutManager(new LinearLayoutManager((Context) getActivity()));
                    adapter.notifyDataSetChanged();
                }
            });
        });

        th2.start();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = MainAppLayoutBinding.inflate(getLayoutInflater());
        initFunc();
        return binding.getRoot();
    }
}
