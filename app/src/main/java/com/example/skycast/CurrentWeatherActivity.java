// Import statements for necessary classes and libraries

// Defining the package and imports
package com.example.skycast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Intent;

// Class for the CurrentWeatherActivity, extending AppCompatActivity
public class CurrentWeatherActivity extends AppCompatActivity {

    // Declaration of TextViews and ImageView
    private TextView locationTextView;
    private TextView todayWeatherTextView;
    private TextView minTemperatureTextView;
    private TextView maxTemperatureTextView;
    private TextView windDirectionTextView;
    private TextView windSpeedTextView;
    private TextView visibilityTextView;
    private TextView pressureTextView;
    private TextView humidityTextView;
    private TextView uvRiskTextView;
    private TextView pollutionTextView;
    private TextView sunriseTextView;
    private TextView sunsetTextView;
    private TextView pubDateTextView;
    private ImageView weatherIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_weather);

        // Initialization of TextViews
        locationTextView = findViewById(R.id.locationTextView);
        todayWeatherTextView = findViewById(R.id.todayWeatherTextView);
        maxTemperatureTextView = findViewById(R.id.maxTemperatureTextView);
        minTemperatureTextView = findViewById(R.id.minTemperatureTextView);
        windDirectionTextView = findViewById(R.id.windDirectionTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        visibilityTextView = findViewById(R.id.visibilityTextView);
        pressureTextView = findViewById(R.id.pressureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        uvRiskTextView = findViewById(R.id.uvRiskTextView);
        pollutionTextView = findViewById(R.id.pollutionTextView);
        sunriseTextView = findViewById(R.id.sunriseTextView);
        sunsetTextView = findViewById(R.id.sunsetTextView);
        pubDateTextView = findViewById(R.id.pubDateTextView);

        // Initialization of ImageView for weather icon
        weatherIconImageView = findViewById(R.id.weatherIconImageView);

        // Finding ImageButtons for navigation
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnSettings = findViewById(R.id.btn_settings);
        ImageButton btnLocation = findViewById(R.id.btn_location);
        ImageButton btnMap = findViewById(R.id.btn_map);

        // Click listeners for navigation buttons
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do nothing since we are already in the CurrentWeatherActivity
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SettingsActivity
                startActivity(new Intent(CurrentWeatherActivity.this, SettingsActivity.class));
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LocationActivity
                startActivity(new Intent(CurrentWeatherActivity.this, LocationActivity.class));
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MapActivity
                startActivity(new Intent(CurrentWeatherActivity.this, MapActivity.class));
            }
        });

        String locationID = "934154";
        String rssFeedURL = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154";

        // Fetch weather data for the specified location
        new FetchWeatherDataTask().execute(rssFeedURL);
    }

    // AsyncTask to fetch weather data
    private class FetchWeatherDataTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... urls) {
            String[] weatherData = new String[14]; // Array to store location, today's weather, and various weather details
            try {
                String rssFeedURL = urls[0];
                URL url = new URL(rssFeedURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputStream);
                doc.getDocumentElement().normalize();

                // Extract location from the title
                String location = extractLocation(doc.getElementsByTagName("title").item(0).getTextContent());
                weatherData[0] = location;

                NodeList itemList = doc.getElementsByTagName("item");

                // Get data for the first item (today's weather)
                Node firstItem = itemList.item(0);
                if (firstItem.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) firstItem;
                    String todayWeather = extractTodayWeather(element.getElementsByTagName("title").item(0).getTextContent());
                    weatherData[1] = todayWeather;

                    String description = element.getElementsByTagName("description").item(0).getTextContent();
                    String[] weatherDetails = extractWeatherDetails(description);

                    // Replace "Minimum Temperature" with "Min °C" and "Maximum Temperature" with "Max °C"
                    for (int i = 0; i < weatherDetails.length; i++) {
                        if (weatherDetails[i].startsWith("Maximum Temperature")) {
                            weatherDetails[i] = weatherDetails[i].replace("Maximum Temperature", "Max °C");
                        } else if (weatherDetails[i].startsWith("Minimum Temperature")) {
                            weatherDetails[i] = weatherDetails[i].replace("Minimum Temperature", "Min °C");
                        }
                    }

                    // Assign weather details to respective array elements
                    System.arraycopy(weatherDetails, 0, weatherData, 2, weatherDetails.length);
                }

                inputStream.close();
                connection.disconnect();
            } catch (Exception e) {
                Log.e("CurrentWeatherActivity", "Error fetching weather data: " + e.getMessage());
            }
            return weatherData;
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            // Set TextViews with the fetched weather data
            locationTextView.setText(weatherData[0]);
            todayWeatherTextView.setText(weatherData[1]);

            // Loop through weather details and assign them to respective TextViews
            for (String detail : weatherData) {
                if (detail != null) {
                    if (detail.startsWith("Max")) {
                        minTemperatureTextView.setText(detail);
                    } else if (detail.startsWith("Min")) {
                        maxTemperatureTextView.setText(detail);
                    } else if (detail.contains("Wind Direction")) {
                        windDirectionTextView.setText(detail);
                    } else if (detail.contains("Wind Speed")) {
                        windSpeedTextView.setText(detail);
                    } else if (detail.contains("Visibility")) {
                        visibilityTextView.setText(detail);
                    } else if (detail.contains("Pressure")) {
                        pressureTextView.setText(detail);
                    } else if (detail.contains("Humidity")) {
                        humidityTextView.setText(detail);
                    } else if (detail.contains("UV Risk")) {
                        uvRiskTextView.setText(detail);
                    } else if (detail.contains("Pollution")) {
                        pollutionTextView.setText(detail);
                    } else if (detail.contains("Sunrise")) {
                        sunriseTextView.setText(detail);
                    } else if (detail.contains("Sunset")) {
                        sunsetTextView.setText(detail);
                    }
                }
            }

            // Fetch weather icon
            if (weatherData.length > 13) {
                fetchWeatherIcon(weatherData[13]);
            } else {
                Log.e("FetchWeatherDataTask", "Weather data array length is less than 14");
            }
        }

        // Method to fetch weather icon
        private void fetchWeatherIcon(String weatherCondition) {
            // Replace "apiKey" with your OpenWeather API key
            String apiKey = "670f0a6d6afdd526a5dc17588dce5d85";
            String iconUrl = "http://openweathermap.org/img/wn/" + weatherCondition + ".png";

            new FetchWeatherIconTask(new FetchWeatherIconTask.WeatherIconListener() {
                @Override
                public void onIconFetched(Bitmap icon) {
                    if (icon != null) {
                        // Set the fetched icon to the ImageView
                        weatherIconImageView.setImageBitmap(icon);
                    }
                }

                @Override
                public void onIconFetchFailed(Exception e) {
                    Log.e("FetchWeatherIconTask", "Error fetching weather icon: " + e.getMessage());
                }
            }).execute(iconUrl + "?appid=" + apiKey);
        }

        // Method to extract location from title
        private String extractLocation(String title) {
            // Extract location from the title
            String[] parts = title.split(" - ");
            if (parts.length > 1) {
                return parts[1].trim();
            }
            return title;
        }

        // Method to extract today's weather from title
        private String extractTodayWeather(String title) {
            // Extract the weather description from the title
            String[] parts = title.split(", ");
            if (parts.length > 0) {
                // The first part should contain the weather description
                return parts[0].split(":")[1].trim();
            }
            return "";
        }

        // Method to extract weather details from description
        private String[] extractWeatherDetails(String description) {
            // Extract relevant data from the description
            String[] items = description.split(", ");
            String[] weatherDetails = new String[items.length]; // Array to store weather details
            for (int i = 0; i < items.length; i++) {
                weatherDetails[i] = items[i].trim();
            }
            return weatherDetails;
        }
    }

    // AsyncTask to fetch weather icon
    private static class FetchWeatherIconTask extends AsyncTask<String, Void, Bitmap> {

        private WeatherIconListener listener;

        public FetchWeatherIconTask(WeatherIconListener listener) {
            this.listener = listener;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String iconUrl = urls[0];
            try {
                URL url = new URL(iconUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    Bitmap icon = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    return icon;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                listener.onIconFetched(bitmap);
            } else {
                listener.onIconFetchFailed(new Exception("Failed to fetch icon"));
            }
        }

        public interface WeatherIconListener {
            void onIconFetched(Bitmap icon);
            void onIconFetchFailed(Exception e);
        }
    }
}
