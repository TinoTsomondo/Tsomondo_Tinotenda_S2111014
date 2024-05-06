// Import statements for necessary classes and libraries

// Defining the package and imports
package com.example.skycast;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;

// Class for the ForecastActivity, extending AppCompatActivity
public class ForecastActivity extends AppCompatActivity {

    // Declaration of ListView and TextView variables
    private ListView forecastListView1;
    private ListView forecastListView2;
    private ListView forecastListView3;
    private TextView detailedViewClickable;
    private TextView forecastTitle;
    private String locationID;
    private String forecastUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // Retrieve location ID and forecast URL from intent extras
        locationID = getIntent().getStringExtra("LOCATION_ID");
        forecastUrl = getIntent().getStringExtra("FORECAST_URL");

        // Initialize forecast list views
        forecastListView1 = findViewById(R.id.forecastListView1);
        forecastListView2 = findViewById(R.id.forecastListView2);
        forecastListView3 = findViewById(R.id.forecastListView3);

        // Initialize clickable TextView
        detailedViewClickable = findViewById(R.id.detailedViewClickable);
        detailedViewClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start CurrentWeatherActivity
                Intent intent = new Intent(ForecastActivity.this, CurrentWeatherActivity.class);
                // Pass location ID and URL as intent extras
                intent.putExtra("LOCATION_ID", locationID);
                intent.putExtra("FORECAST_URL", forecastUrl);
                // Start the activity
                startActivity(intent);
            }
        });

        // Initialize forecast title TextView
        forecastTitle = findViewById(R.id.forecastTitle);

        // Fetch forecast data
        new FetchForecastDataTask().execute(forecastUrl);
    }

    // AsyncTask to fetch forecast data
    private class FetchForecastDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            ArrayList<String> forecastList = new ArrayList<>();
            String forecastTitleText = "";
            try {
                String forecastUrl = urls[0];
                URL url = new URL(forecastUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputStream);
                doc.getDocumentElement().normalize();

                // Extract forecast title
                Element channel = (Element) doc.getElementsByTagName("channel").item(0);
                forecastTitleText = channel.getElementsByTagName("title").item(0).getTextContent();
                // Extract location from forecast title
                String location = extractLocation(forecastTitleText);
                forecastTitleText = "3 Day " + location;

                NodeList itemList = doc.getElementsByTagName("item");

                for (int i = 0; i < itemList.getLength(); i++) {
                    Node node = itemList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String title = element.getElementsByTagName("title").item(0).getTextContent();
                        String description = element.getElementsByTagName("description").item(0).getTextContent();

                        // Extract relevant data from title and description
                        String titleData = extractTitleData(title);
                        String descriptionData = extractDescriptionData(description);

                        forecastList.add(titleData + "\n" + descriptionData);
                    }
                }

                inputStream.close();
                connection.disconnect();
            } catch (Exception e) {
                Log.e("ForecastActivity", "Error fetching forecast data: " + e.getMessage());
            }

            // Set forecast title
            final String finalForecastTitleText = forecastTitleText;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    forecastTitle.setText(finalForecastTitleText);
                }
            });

            return forecastList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> forecastList) {
            // Assuming each day's forecast consists of multiple lines
            ArrayList<ArrayList<String>> dailyForecasts = new ArrayList<>();

            // Split the forecastList into separate lists for each day's forecast
            ArrayList<String> currentDayForecast = new ArrayList<>();
            for (String forecast : forecastList) {
                // Assuming each day's forecast starts with a day name followed by a newline character
                if (forecast.contains("\n")) {
                    dailyForecasts.add(currentDayForecast);
                    currentDayForecast = new ArrayList<>();
                }
                currentDayForecast.add(forecast);
            }
            // Add the last day's forecast if it's not already added
            if (!currentDayForecast.isEmpty()) {
                dailyForecasts.add(currentDayForecast);
            }

            // Create adapters for each day's forecast and set them to their respective containers
            for (int i = 0; i < dailyForecasts.size(); i++) {
                ArrayList<String> dayForecast = dailyForecasts.get(i);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ForecastActivity.this,
                        android.R.layout.simple_list_item_1, dayForecast);

                // Assuming you have containers for each day's forecast with IDs like forecastContainer1, forecastContainer2, etc.
                ListView listView;
                switch (i) {
                    case 1:
                        listView = forecastListView1;
                        break;
                    case 2:
                        listView = forecastListView2;
                        break;
                    case 3:
                        listView = forecastListView3;
                        break;
                    default:
                        listView = null;
                        break;
                }
                if (listView != null) {
                    listView.setAdapter(adapter);
                }
            }
        }

        // Method to extract data from title
        private String extractTitleData(String title) {
            // Find the index of the first occurrence of "Minimum Temperature"
            int endIndex = title.indexOf("Minimum Temperature");

            // If "Minimum Temperature" is found, extract the substring from the start of the title to endIndex
            if (endIndex != -1) {
                String extractedTitle = title.substring(0, endIndex).trim();
                // Split the extracted title at the colon (":") and format it accordingly
                String[] parts = extractedTitle.split(":");

                StringBuilder builder = new StringBuilder();
                for (String part : parts) {
                    // Append each part with a newline character for formatting
                    builder.append(part.trim()).append("\n");
                }

                // Remove the trailing newline character and any leading or trailing spaces
                return builder.toString().trim();
            } else {
                // If "Minimum Temperature" is not found, return the original title
                return title;
            }
        }

        // Method to extract data from description
        private String extractDescriptionData(String description) {
            // Extract relevant data from the description
            String[] items = description.split(", ");

            StringBuilder builder = new StringBuilder();
            for (String item : items) {
                // Check if the item starts with any of the specified prefixes
                if (item.startsWith("Maximum Temperature") || item.startsWith("Minimum Temperature") ||
                        item.startsWith("Wind Direction") || item.startsWith("Wind Speed") ||
                        item.startsWith("Visibility")) {
                    // Append the item with a newline character for formatting
                    builder.append(item).append("\n");
                }
            }

            // Remove the trailing newline character
            return builder.toString().trim();
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
    }
}
