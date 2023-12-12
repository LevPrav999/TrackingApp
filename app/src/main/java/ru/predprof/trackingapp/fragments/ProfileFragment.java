package ru.predprof.trackingapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.databinding.ProfileLayoutBinding;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;

public class ProfileFragment extends Fragment {
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
