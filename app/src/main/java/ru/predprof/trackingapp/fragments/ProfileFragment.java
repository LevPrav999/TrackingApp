package ru.predprof.trackingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.activities.EditProfileActivity;
import ru.predprof.trackingapp.adapters.ProfileRecyclerAdapter;
import ru.predprof.trackingapp.databinding.ProfileLayoutBinding;
import ru.predprof.trackingapp.models.Trip;
import ru.predprof.trackingapp.room.RoomHandler;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;
import ru.predprof.trackingapp.utils.Replace;

public class ProfileFragment extends Fragment {
    List<Trip> lst;
    List<Trip> bestTrips = new ArrayList<Trip>();
    ProfileRecyclerAdapter adapter;
    private ProfileLayoutBinding binding;
    private SharedPreferencesManager preferenceManager;

    private void initFields() {
        preferenceManager = new SharedPreferencesManager(requireActivity());
    }

    private void initFunc() {
        binding.profileNameSurname.setText(preferenceManager.getString("name", "Имя отсутствует"));
        binding.registerPhoneNumber.setText(preferenceManager.getString("phonenumber", "Номер отсутствует"));
        binding.registerHeight.setText(Integer.toString(preferenceManager.getInt("height", 0)));
        binding.registerWeight.setText(Float.toString(preferenceManager.getFloat("weight", 0)));
        int lavel = preferenceManager.getInt("level", 0);
        String[] stringLavels = getResources().getStringArray(R.array.level_choice_array);
        binding.registerLevel.setText(stringLavels[preferenceManager.getInt("level", 0)]);
        String[] stringHealth = getResources().getStringArray(R.array.health_choice_array);
        binding.registerHealth.setText(stringHealth[preferenceManager.getInt("healthStatus", 0)]);
        Thread th2 = new Thread(() -> {

            lst = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getAll();
            List<Trip> ended = new ArrayList<>();
            for (Trip el : lst) {
                if (Objects.equals(el.ended, "1")) {
                    el.setLenKm(el.getLenKm().replace("km", "").replace("mi", ""));
                    ended.add(el);
                }
            }
            Collections.sort(ended);
            if (!ended.isEmpty()) {
                bestTrips.add(ended.get(0));
            }
            if (ended.size() > 1) {
                bestTrips.add(ended.get(1));
            }
            if (ended.size() > 2) {
                bestTrips.add(ended.get(2));
            }

            binding.recycler.post(new Runnable() {
                @Override
                public void run() {
                    if (!bestTrips.isEmpty()) {
                        adapter = new ProfileRecyclerAdapter(bestTrips);
                        binding.recycler.setAdapter(adapter);
                        binding.recycler.setLayoutManager(new LinearLayoutManager((Context) getActivity()));
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        });

        th2.start();
        binding.editProfileData.setOnClickListener(view -> {
            Replace.replaceActivity(getActivity(), new EditProfileActivity(), false);
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ProfileLayoutBinding.inflate(getLayoutInflater());
        initFields();
        initFunc();

        return binding.getRoot();
    }


}
