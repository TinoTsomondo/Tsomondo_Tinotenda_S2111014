// Defining the package
package com.example.skycast;

// Importing necessary classes and packages
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Class representing the Latest Observation Activity
public class LatestObservationActivity extends AppCompatActivity {

    // Declaration of instance variables
    private ListView observationListView;
    private String locationId;
    private String locationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_observation);

        // Get location name and ID from intent extras
        Intent intent = getIntent();
        if (intent != null) {
            locationName = intent.getStringExtra("LOCATION_NAME");
            locationId = intent.getStringExtra("LOCATION_ID");
            setTitle(locationName); // Set the title of the activity to the location name
            new FetchObservationDataTask().execute(locationId);
        }

        // Initialize observationListView
        observationListView = findViewById(R.id.ObservationListView);

        // Button to view 3-Day Forecast
        Button btn3DayForecast = findViewById(R.id.btn_3_day_forecast);
        btn3DayForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToForecastActivity();
            }
        });
    }

    // Method to navigate to ForecastActivity
    private void navigateToForecastActivity() {
        // Construct the URL for the 3-day forecast using the locationId
        String forecastUrl = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + locationId;

        // Start the ForecastActivity and pass the forecast URL and location name as extras
        Intent intent = new Intent(LatestObservationActivity.this, ForecastActivity.class);
        intent.putExtra("FORECAST_URL", forecastUrl);
        intent.putExtra("LOCATION_NAME", locationName);
        startActivity(intent);
    }

    // AsyncTask to fetch observation data from the API
    private class FetchObservationDataTask extends AsyncTask<String, Void, ArrayList<WeatherObservation>> {

        @Override
        protected ArrayList<WeatherObservation> doInBackground(String... locationIds) {
            ArrayList<WeatherObservation> observationsList = new ArrayList<>();
            try {
                String locationId = locationIds[0];
                URL url = new URL("https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/" + locationId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputStream);
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("item");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String title = element.getElementsByTagName("title").item(0).getTextContent();
                        String description = element.getElementsByTagName("description").item(0).getTextContent();
                        String pubDate = element.getElementsByTagName("pubDate").item(0).getTextContent();

                        // Parsing the description string to extract individual weather details
                        String[] items = description.split(", ");

                        ArrayList<WeatherDetail> details = new ArrayList<>();
                        for (String item : items) {
                            String[] parts = item.split(": ");
                            if (parts.length == 2) {
                                String detailTitle = parts[0].trim();
                                String detailValue = parts[1].trim();
                                details.add(new WeatherDetail(detailTitle, detailValue));
                            }
                        }

                        WeatherObservation observation = new WeatherObservation(title, details, pubDate);
                        observationsList.add(observation);
                    }
                }

                inputStream.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return observationsList;
        }

        @Override
        protected void onPostExecute(ArrayList<WeatherObservation> observationsList) {
            if (observationsList != null) {
                // Set up the adapter for the observationListView
                LatestObservationAdapter adapter = new LatestObservationAdapter(LatestObservationActivity.this, observationsList);
                observationListView.setAdapter(adapter);
                // Set item click listener for observationListView
                observationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Handle item click if needed
                    }
                });
            }
        }
    }
}
