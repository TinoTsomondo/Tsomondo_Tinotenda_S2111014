package com.example.skycast;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private ImageView btnHome;
    private ImageView btnLocation;
    private ImageView btnMap;
    private ImageView btnMorningUpdate;
    private ImageView btnEveningUpdate;

    private int morningHour = 8;
    private int morningMinute = 0;
    private int eveningHour = 20;
    private int eveningMinute = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Find the ImageViews for each icon
        btnHome = findViewById(R.id.btn_home);
        ImageView btnSettings = findViewById(R.id.btn_settings);
        btnLocation = findViewById(R.id.btn_location);
        btnMap = findViewById(R.id.btn_map);
        btnMorningUpdate = findViewById(R.id.btn_morning_update);
        btnEveningUpdate = findViewById(R.id.btn_evening_update);

        // Set click listeners for each icon
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CurrentWeatherActivity (home)
                startActivity(new Intent(SettingsActivity.this, CurrentWeatherActivity.class));
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do nothing or handle additional behavior if needed
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LocationActivity
                startActivity(new Intent(SettingsActivity.this, LocationActivity.class));
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MapActivity
                startActivity(new Intent(SettingsActivity.this, MapActivity.class));
            }
        });

        // Set click listeners for morning and evening update buttons
        btnMorningUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(true);
            }
        });

        btnEveningUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(false);
            }
        });
    }

    private void showTimePickerDialog(final boolean isMorning) {
        Calendar calendar = Calendar.getInstance();
        int hour = isMorning ? morningHour : eveningHour;
        int minute = isMorning ? morningMinute : eveningMinute;

        TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (isMorning) {
                            morningHour = hourOfDay;
                            morningMinute = minute;
                        } else {
                            eveningHour = hourOfDay;
                            eveningMinute = minute;
                        }
                        saveSettings();
                        Toast.makeText(SettingsActivity.this, "Time set successfully", Toast.LENGTH_SHORT).show();
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void saveSettings() {
        SharedPreferences preferences = getSharedPreferences("MySettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("MorningHour", morningHour);
        editor.putInt("MorningMinute", morningMinute);
        editor.putInt("EveningHour", eveningHour);
        editor.putInt("EveningMinute", eveningMinute);
        editor.apply();
    }
}
