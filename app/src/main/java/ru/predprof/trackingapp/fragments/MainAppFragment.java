package ru.predprof.trackingapp.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.adapters.StatisticRecyclerAdapter;
import ru.predprof.trackingapp.databinding.MainAppLayoutBinding;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;

public class MainAppFragment extends Fragment {
    StatisticRecyclerAdapter adapter;
    List<Trip> lst;
    SharedPreferencesManager sharedPreferencesManager;
    private MainAppLayoutBinding binding;

    private void initFunc() {
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        binding.helloAndName.setText("Здравствуйте, " + sharedPreferencesManager.getString("name", "inkognito"));
        Thread th1 = new Thread(() -> {

            List<Trip> lst = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getAll();
            binding.routeLength.post(new Runnable() {
                @Override
                public void run() {
                    binding.lastRoute.setText(lst.get(lst.size() - 1).getStartPoint() + " - " + lst.get(lst.size() - 1).endPoint);
                    binding.routeLength.setText(lst.get(lst.size() - 1).lenKm);
                    binding.routeDuration.setText(lst.get(lst.size() - 1).time);
                    binding.routeEstimatedComplexity.setText(lst.get(lst.size() - 1).difficultAuto);
                    binding.routeUserRating.setText(lst.get(lst.size() - 1).difficultReal);
                    binding.routeAverageRating.setText(Integer.toString(
                            Integer.parseInt(lst.get(lst.size() - 1).difficultReal)
                    + Integer.parseInt(lst.get(lst.size() - 1).difficultAuto) / 2));

                }
            });
        });
        th1.start();
        binding.graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    String day;
                    switch ((int) value) {
                        case 0:
                            return "Вс";
                        case 2:
                            return "Пн";
                        case 4:
                            return "Вт";
                        case 6:
                            return "Ср";
                        case 8:
                            return "Чт";
                        case 10:
                            return "Пт";
                        case 12:
                            return "Сб";
                        default:
                            return null;

                    }
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        Thread th_gr = new Thread(() -> {

            List<Trip> lst = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getAll();
            Calendar calendar = Calendar.getInstance();

            String date = new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());


            int day_week = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DAY_OF_MONTH, -day_week);
            Date start_date = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            Date end_date = calendar.getTime();
            int[] hj = {0, 0, 0, 0, 0, 0, 0};
            binding.graph.post(new Runnable() {
                @Override
                public void run() {

                    for(Trip tr : lst) {
                        Date thedate = null;
                        try {
                            try {
                                thedate = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(tr.getWeekDay());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }


                            if (thedate.compareTo(start_date) >= 0 && thedate.compareTo(end_date) <= 0) {
                                Calendar cd = Calendar.getInstance();
                                cd.setTime(thedate);
                                hj[cd.get(Calendar.DAY_OF_WEEK) - 1] = hj[cd.get(Calendar.DAY_OF_WEEK)- 1] + 1;

                            }
                        } catch (Exception e) {
                            Log.d("ghjkl", tr.getWeekDay());
                        }
                    }
                    BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                            new DataPoint(0, hj[0]),
                            new DataPoint(2, hj[1]),
                            new DataPoint(4, hj[2]),
                            new DataPoint(6, hj[3]),
                            new DataPoint(8, hj[4]),
                            new DataPoint(10, hj[5]),
                            new DataPoint(12, hj[6])
                    });
                    binding.graph.getViewport().setXAxisBoundsManual(true);
                    binding.graph.getViewport().setMinX(0.0);
                    binding.graph.getViewport().setMaxX(12.0);
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

                }
            });
        });
        th_gr.start();


        Thread th2 = new Thread(() -> {

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
    public void replace(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.map, fragment).commit();

    }
}
