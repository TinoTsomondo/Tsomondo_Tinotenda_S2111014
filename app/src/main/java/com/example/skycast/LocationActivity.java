// Defining the package
package com.example.skycast;

// Importing necessary classes and packages
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

// Activity for selecting location and navigating to latest observation activity
public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Find the ImageButtons for each icon
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnSettings = findViewById(R.id.btn_settings);
        ImageButton btnLocation = findViewById(R.id.btn_location);
        ImageButton btnMap = findViewById(R.id.btn_map);

        // Set click listeners for each icon
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CurrentWeatherActivity (home)
                startActivity(new Intent(LocationActivity.this, CurrentWeatherActivity.class));
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SettingsActivity
                startActivity(new Intent(LocationActivity.this, SettingsActivity.class));
            }
        });

        // Set click listener for the Location button
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do nothing since we are already in the LocationActivity
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MapActivity
                startActivity(new Intent(LocationActivity.this, MapActivity.class));
            }
        });

        // Add buttons for the six locations
        Button btnGlasgow = findViewById(R.id.btn_glasgow);
        Button btnLondon = findViewById(R.id.btn_london);
        Button btnNewYork = findViewById(R.id.btn_new_york);
        Button btnOman = findViewById(R.id.btn_oman);
        Button btnMauritius = findViewById(R.id.btn_mauritius);
        Button btnBangladesh = findViewById(R.id.btn_bangladesh);

        // Set background color for buttons
        btnGlasgow.setBackgroundColor(Color.parseColor("#15719f"));
        btnLondon.setBackgroundColor(Color.parseColor("#15719f"));
        btnNewYork.setBackgroundColor(Color.parseColor("#15719f"));
        btnOman.setBackgroundColor(Color.parseColor("#15719f"));
        btnMauritius.setBackgroundColor(Color.parseColor("#15719f"));
        btnBangladesh.setBackgroundColor(Color.parseColor("#15719f"));

        // Set click listeners for the location buttons
        btnGlasgow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLatestObservationActivity("Glasgow", "2648579");
            }
        });

        btnLondon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLatestObservationActivity("London", "2643743");
            }
        });

        btnNewYork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLatestObservationActivity("New York", "5128581");
            }
        });

        btnOman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLatestObservationActivity("Oman", "287286");
            }
        });

        btnMauritius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLatestObservationActivity("Mauritius", "934154");
            }
        });

        btnBangladesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLatestObservationActivity("Bangladesh", "1185241");
            }
        });
    }

    // Method to navigate to LatestObservationActivity with location name and ID
    private void navigateToLatestObservationActivity(String locationName, String locationId) {
        Intent intent = new Intent(LocationActivity.this, LatestObservationActivity.class);
        intent.putExtra("LOCATION_NAME", locationName);
        intent.putExtra("LOCATION_ID", locationId);
        startActivity(intent);
    }
}
