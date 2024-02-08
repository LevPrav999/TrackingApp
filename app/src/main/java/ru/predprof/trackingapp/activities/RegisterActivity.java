package ru.predprof.trackingapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.predprof.trackingapp.MainActivity;
import ru.predprof.trackingapp.R;
import ru.predprof.trackingapp.databinding.FragmentRegisterBinding;
import ru.predprof.trackingapp.sharedpreferences.SharedPreferencesManager;
import ru.predprof.trackingapp.utils.Replace;


public class RegisterActivity extends AppCompatActivity {

    private int level = -2;
    private int healthStatus = -3;

    private FragmentRegisterBinding binding;
    private SharedPreferencesManager preferenceManager;

    public RegisterActivity() {

    }

    private void initFields() {
        preferenceManager = new SharedPreferencesManager(this);
    }

    private void initFunc() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.level_choice_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.registerLevelSpinner.setAdapter(adapter);
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
        binding.registerHealthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                healthStatus = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.sendFormButton.setOnClickListener(listener -> {
            if(binding.registerPhoneNumber.length() < 12 || binding.registerPhoneNumber.length() > 12){
                Toast.makeText(getApplicationContext(),
                        "Неверно введён номер телефона",
                        Toast.LENGTH_LONG
                ).show();
            }
            else if(binding.registerName.length() < 3){
                Toast.makeText(getApplicationContext(),
                        "Введите имя",
                        Toast.LENGTH_LONG
                ).show();
            }
            else if (binding.registerHeight.length() != 3) {
                Toast.makeText(getApplicationContext(),
                        "Введите рост трёхзначным числом",
                        Toast.LENGTH_LONG
                ).show();
            } else if ((binding.registerWeight.length() != 4 || binding.registerWeight.length() != 5) && !binding.registerWeight.getText().toString().contains(".")) {
                Toast.makeText(getApplicationContext(),
                        "Введите вес в формате XXX.X или XX.X",
                        Toast.LENGTH_LONG
                ).show();
            } else if (level == -2) {
                Toast.makeText(getApplicationContext(),
                        "Вы не выбрали уровень подготовки",
                        Toast.LENGTH_LONG
                ).show();
            }
            else if (healthStatus == -3) {
                Toast.makeText(getApplicationContext(),
                        "Вы не выбрали состояние здоровья",
                        Toast.LENGTH_LONG
                ).show();
            }else {
                try{
                    float a = Float.parseFloat(binding.registerHeight.getText().toString());
                    float b = Float.parseFloat(binding.registerWeight.getText().toString());
                    long c = Long.parseLong(binding.registerPhoneNumber.getText().toString().replace("+", "").replace("\"", ""));

                    saveToDb();
                    Replace.replaceActivity(this, new MainActivity(), false);

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                            "Какой-то из параметров введён неверно",
                            Toast.LENGTH_LONG
                    ).show();
                }
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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initFields();
        initFunc();


    }
}