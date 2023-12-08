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
import ru.predprof.trackingapp.databinding.EditProfileLayoutBinding;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;

public class EditProfileFragment extends Fragment  implements AdapterView.OnItemSelectedListener {

    private int level = -2;
    private int healthStatus = -3;

    private EditProfileLayoutBinding binding;
    private SharedPreferencesManager preferenceManager;


    private void initFields(){
        preferenceManager = new SharedPreferencesManager(requireActivity());
    }

    private void initFunc(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.level_choice_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.registerLevelSpinner.setAdapter(adapter);
        binding.registerLevelSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.health_choice_array,
                android.R.layout.simple_spinner_item
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.registerHealthSpinner.setAdapter(adapter2);
        binding.registerHealthSpinner.setOnItemSelectedListener(this);


        binding.sendFormButton.setOnClickListener(listener -> {
            if (binding.registerHeight.length() != 3){
                Log.d("eeeeeee", "Введите рост трёхзначным числом");
            }else if(binding.registerWeight.length() != 4){
                Log.d("eeeeeee", "Введите вес в формате 86.3 или 72.0");
            }else if(level == -1){
                Log.d("eeeeeee", "Вы не выбрали уровень подготовки");
            }else{
                saveToDb();
                // navigate to another screen
            }
        });
    }
    private void saveToDb(){
        float height = Float.parseFloat(binding.registerHeight.getText().toString());
        float weight = Float.parseFloat(binding.registerWeight.getText().toString());
        float imt = weight/(height*height);
        String name = binding.registerName.getText().toString();
        String tel_num = binding.registerPhoneNumber.getText().toString();
        String imtString = String.format("%.1g%n", imt);
        preferenceManager.saveString("name", name);
        preferenceManager.saveString("phonenumber", tel_num);
        preferenceManager.saveFloat("height", height);
        preferenceManager.saveFloat("weight", weight);
        preferenceManager.saveFloat("imt", imt); // точное значение индекса массы тела
        preferenceManager.saveString("imtString", imtString); // строка для отображения на экране
        preferenceManager.saveInt("level", level);
        preferenceManager.saveInt("healthStatus", healthStatus);
        Log.d("eeeeeee", preferenceManager.getString("name", "lllll"));

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = EditProfileLayoutBinding.inflate(getLayoutInflater());

        initFields();
        initFunc();

        return binding.getRoot();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(view.getId() == R.id.registerLevelSpinner){
            level = i-1;
        }else{
            healthStatus = i - 2;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}