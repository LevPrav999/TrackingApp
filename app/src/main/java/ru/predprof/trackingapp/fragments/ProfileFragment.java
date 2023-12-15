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
        binding.editProfileData.setOnClickListener(view -> {
            // TODO переход на страницу редактирования профиля
        });
        Thread th2 = new Thread(() -> { // Тест работы БД

            lst = RoomHandler.getInstance(getContext()).getAppDatabase().tripDao().getAll();
            Collections.sort(lst);
            bestTrips.add(lst.get(0));
            bestTrips.add(lst.get(1));
            bestTrips.add(lst.get(2));

            binding.recycler.post(new Runnable() {
                @Override
                public void run() {
                    adapter = new ProfileRecyclerAdapter(bestTrips);
                    binding.recycler.setAdapter(adapter);
                    binding.recycler.setLayoutManager(new LinearLayoutManager((Context) getActivity()));
                    adapter.notifyDataSetChanged();
                }
            });
//            Log.d("tyyyyyyyyyyyy", Integer.toString(lst.size()));
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
