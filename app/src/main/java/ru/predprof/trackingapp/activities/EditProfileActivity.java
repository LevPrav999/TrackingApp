package ru.predprof.trackingapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ru.predprof.trackingapp.MainActivity;
import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.databinding.EditProfileLayoutBinding;
import ru.predprof.trackingapp.databinding.FragmentRegisterBinding;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;
import ru.predprof.trackingapp.utils.Replace;

public class EditProfileActivity extends AppCompatActivity {

    private int level = -2;
    private int healthStatus = -3;

    private EditProfileLayoutBinding binding;
    private SharedPreferencesManager preferenceManager;


    private void initFields() {
        preferenceManager = new SharedPreferencesManager(this);
    }

    private void initFunc() {
        binding.registerName.setText(preferenceManager.getString("name", "Имя отсутствует"));
        binding.registerPhoneNumber.setText(preferenceManager.getString("phonenumber", "Номер отсутствует"));
        binding.registerHeight.setText(Integer.toString(preferenceManager.getInt("height", 0)));
        binding.registerWeight.setText(Float.toString(preferenceManager.getFloat("weight", 0)));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.level_choice_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.registerLevelSpinner.setAdapter(adapter);
        binding.registerLevelSpinner.setSelection(preferenceManager.getInt("level", 0));
        binding.registerLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                level = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.health_choice_array,
                android.R.layout.simple_spinner_item
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.registerHealthSpinner.setAdapter(adapter2);
        binding.registerHealthSpinner.setSelection(preferenceManager.getInt("healthStatus", 0));
        binding.registerHealthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                healthStatus = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.cancel.setOnClickListener(l -> Replace.replaceActivity(this, new MainActivity(), true));
        binding.sendFormButton.setOnClickListener(listener -> {
            if (binding.registerHeight.length() != 3) {
                Log.d("eeeeeee", "Введите рост трёхзначным числом");
            } else if (binding.registerWeight.length() != 4) {
                Log.d("eeeeeee", "Введите вес в формате 86.3 или 72.0");
            } else if (level == -1) {
                Log.d("eeeeeee", "Вы не выбрали уровень подготовки");
            } else {
                saveToDb();
                Replace.replaceActivity(this, new MainActivity(), true);
                // navigate to another screen
            }
        });
    }

    private void saveToDb() {
        int height = Integer.parseInt(binding.registerHeight.getText().toString());
        float weight = Float.parseFloat(binding.registerWeight.getText().toString());
        float imt = weight / (float) (height * height);
        String name = binding.registerName.getText().toString();
        String tel_num = binding.registerPhoneNumber.getText().toString();
        String imtString = String.format("%.1g%n", imt);
        preferenceManager.saveString("name", name);
        preferenceManager.saveString("phonenumber", tel_num);
        preferenceManager.saveInt("height", height);
        preferenceManager.saveFloat("weight", weight);
        preferenceManager.saveFloat("imt", imt); // точное значение индекса массы тела
        preferenceManager.saveString("imtString", imtString); // строка для отображения на экране
        preferenceManager.saveInt("level", level);
        preferenceManager.saveInt("healthStatus", healthStatus);
        Log.d("eeeeeee", Integer.toString(preferenceManager.getInt("level", 0)));

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EditProfileLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initFields();
        initFunc();

    }


}