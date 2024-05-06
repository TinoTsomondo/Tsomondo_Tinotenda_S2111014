package com.example.skycast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {

    private GoogleMap googleMap;
    private Marker mauritiusMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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
                startActivity(new Intent(MapActivity.this, CurrentWeatherActivity.class));
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SettingsActivity
                startActivity(new Intent(MapActivity.this, SettingsActivity.class));
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LocationActivity
                startActivity(new Intent(MapActivity.this, LocationActivity.class));
            }
        });


        // Set up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    googleMap = map;
                    LatLng glasgow = new LatLng(55.8642, -4.2518);
                    LatLng london = new LatLng(51.5074, -0.1278);
                    LatLng newYork = new LatLng(40.7128, -74.0060);
                    LatLng mauritius = new LatLng(-20.3484, 57.5522); // Coordinates for Mauritius

                    Marker glasgowMarker = googleMap.addMarker(new MarkerOptions().position(glasgow).title("Glasgow"));
                    Marker londonMarker = googleMap.addMarker(new MarkerOptions().position(london).title("London"));
                    Marker newYorkMarker = googleMap.addMarker(new MarkerOptions().position(newYork).title("New York"));
                    mauritiusMarker = googleMap.addMarker(new MarkerOptions().position(mauritius).title("Mauritius")); // Add Mauritius marker

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 10));

                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if (marker.getTitle().equals("Mauritius")) { // Check if the marker is Mauritius
                                // Launch CurrentWeatherActivity for Mauritius
                                startActivity(new Intent(MapActivity.this, CurrentWeatherActivity.class));
                                return true;
                            } else {
                                Intent intent = new Intent(MapActivity.this, LatestObservationActivity.class);
                                intent.putExtra("LOCATION_NAME", marker.getTitle());
                                intent.putExtra("LOCATION_ID", getLocationId(marker.getTitle())); // Get location ID based on location name
                                startActivity(intent);
                                return true;
                            }
                        }
                    });
                }
            });
        }

        // Find the SearchView widget
        SearchView searchView = findViewById(R.id.search_view);
        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.equalsIgnoreCase("Mauritius")) {
                    // Show Mauritius marker and move the camera to it
                    mauritiusMarker.setVisible(true);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mauritiusMarker.getPosition(), 10));
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes if needed
                return false;
            }
        });
    }

    // Method to get location ID based on location name
    private String getLocationId(String locationName) {
        switch (locationName) {
            case "Glasgow":
                return "2648579";
            case "London":
                return "2643743";
            case "New York":
                return "5128581";
            // Add more cases for other locations
            default:
                return "";
        }
    }
}
