// Defining the package
package com.example.skycast;

// Importing necessary classes and packages
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

// MainActivity class responsible for the main screen of the application
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Finding the 'Get Started' button
        Button getStartedButton = findViewById(R.id.btn_get_started);

        // Setting white background color for the button
        getStartedButton.setBackgroundColor(getResources().getColor(android.R.color.white));

        // Setting a click listener for the 'Get Started' button
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting the CurrentWeatherActivity when the button is clicked
                startActivity(new Intent(MainActivity.this, CurrentWeatherActivity.class));
            }
        });
    }

}
